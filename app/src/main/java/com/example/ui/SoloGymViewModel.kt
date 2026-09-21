package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.LogResult
import com.example.data.SoloGymRepository
import com.example.data.ai.SystemAIEngine
import com.example.data.db.AppDatabase
import com.example.data.db.ChatMessageEntity
import com.example.data.db.DailyQuestEntity
import com.example.data.db.PlayerStatsEntity
import com.example.data.db.WorkoutLogEntity
import com.example.data.speech.SystemSpeechManager
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SoloGymViewModel(application: Application) : AndroidViewModel(application) {
    private val database = AppDatabase.getDatabase(application)
    private val repository = SoloGymRepository(database)
    private val aiEngine = SystemAIEngine()
    private val speechManager = SystemSpeechManager(application)

    val playerStats: StateFlow<PlayerStatsEntity?> = repository.playerStats
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val dailyQuest: StateFlow<DailyQuestEntity?> = repository.getDailyQuest()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val chatMessages: StateFlow<List<ChatMessageEntity>> = repository.chatMessages
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val recentWorkouts: StateFlow<List<WorkoutLogEntity>> = repository.recentWorkouts
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _isGenerating = MutableStateFlow(false)
    val isGenerating: StateFlow<Boolean> = _isGenerating.asStateFlow()

    private val _isVoiceMuted = MutableStateFlow(false)
    val isVoiceMuted: StateFlow<Boolean> = _isVoiceMuted.asStateFlow()

    private val _restTimerSeconds = MutableStateFlow(0)
    val restTimerSeconds: StateFlow<Int> = _restTimerSeconds.asStateFlow()

    private val _systemAlert = MutableStateFlow<String?>(null)
    val systemAlert: StateFlow<String?> = _systemAlert.asStateFlow()

    private var timerJob: Job? = null

    init {
        viewModelScope.launch {
            repository.initializeIfNeeded()
            // Check if player stats initialized
            if (playerStats.value == null) {
                repository.initPlayerIfEmpty()
            }
        }
    }

    fun toggleVoiceMute() {
        _isVoiceMuted.value = !_isVoiceMuted.value
        speechManager.isMuted = _isVoiceMuted.value
        if (_isVoiceMuted.value) {
            speechManager.stop()
        }
    }

    fun dismissSystemAlert() {
        _systemAlert.value = null
    }

    fun logExercise(exerciseType: String, count: Int) {
        viewModelScope.launch {
            val currentStats = playerStats.value
            val currentQuest = dailyQuest.value
            val result: LogResult = repository.logExercise(exerciseType, count, currentStats, currentQuest)

            if (result.leveledUp) {
                val lvlMsg = "Level Up! You have unlocked additional attribute points."
                _systemAlert.value = "[LEVEL UP DETECTED]\n$lvlMsg"
                speechManager.speak("Player Level Up. Strength increases. Allocate your attribute points.")
            } else if (result.allCompleted) {
                val completeMsg = "Daily Quest Objectives cleared! Claim your rewards."
                _systemAlert.value = "[QUEST COMPLETED]\n$completeMsg"
                speechManager.speak("Daily Quest completed. Status recovery and rewards are ready to be claimed.")
            }
        }
    }

    fun toggleQuestExercise(exerciseType: String) {
        viewModelScope.launch {
            val currentStats = playerStats.value
            val currentQuest = dailyQuest.value
            val result = repository.toggleQuestExercise(exerciseType, currentStats, currentQuest)

            if (result.leveledUp) {
                val lvlMsg = "Level Up! You have unlocked additional attribute points."
                _systemAlert.value = "[LEVEL UP DETECTED]\n$lvlMsg"
                speechManager.speak("Player Level Up. Strength increases. Allocate your attribute points.")
            } else if (result.allCompleted) {
                val completeMsg = "Daily Quest Objectives cleared! Claim your rewards."
                _systemAlert.value = "[QUEST COMPLETED]\n$completeMsg"
                speechManager.speak("Daily Quest completed. Status recovery and rewards are ready to be claimed.")
            }
        }
    }

    fun allocateStat(stat: String) {
        viewModelScope.launch {
            playerStats.value?.let { current ->
                repository.allocateStat(stat, current)
            }
        }
    }

    fun claimRewards() {
        viewModelScope.launch {
            val stats = playerStats.value ?: return@launch
            val quest = dailyQuest.value ?: return@launch
            if (quest.isCompleted && !quest.isRewardClaimed) {
                repository.claimRewards(stats, quest)
                _systemAlert.value = "[STATUS FULLY RECOVERED]\nFatigue reset to 0%. +3 Stat Points awarded!"
                speechManager.speak("Status recovery successful. All fatigue dispelled. Growth continues.")
            }
        }
    }

    fun startRestTimer(seconds: Int) {
        timerJob?.cancel()
        _restTimerSeconds.value = seconds
        timerJob = viewModelScope.launch {
            while (_restTimerSeconds.value > 0) {
                delay(1000)
                _restTimerSeconds.value -= 1
            }
            speechManager.speak("Rest period concluded. Return to your set, Hunter.")
        }
    }

    fun cancelRestTimer() {
        timerJob?.cancel()
        _restTimerSeconds.value = 0
    }

    fun sendChatMessage(text: String) {
        if (text.isBlank() || _isGenerating.value) return
        viewModelScope.launch {
            _isGenerating.value = true
            repository.addChatMessage(sender = "HUNTER", message = text)

            val currentStats = playerStats.value
            val currentQuest = dailyQuest.value
            val response = aiEngine.getSystemResponse(text, currentStats, currentQuest)

            repository.addChatMessage(sender = "SYSTEM", message = response)
            _isGenerating.value = false

            // Voice speak response
            speechManager.speak(response)
        }
    }

    fun speakSystemText(text: String) {
        speechManager.speak(text)
    }

    override fun onCleared() {
        super.onCleared()
        speechManager.shutdown()
    }
}
