package com.example.data

import com.example.data.db.AppDatabase
import com.example.data.db.ChatMessageEntity
import com.example.data.db.DailyQuestEntity
import com.example.data.db.PlayerStatsEntity
import com.example.data.db.WorkoutLogEntity
import kotlinx.coroutines.flow.Flow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class SoloGymRepository(private val database: AppDatabase) {
    private val playerDao = database.playerStatsDao()
    private val questDao = database.dailyQuestDao()
    private val workoutDao = database.workoutDao()
    private val chatDao = database.chatMessageDao()

    val playerStats: Flow<PlayerStatsEntity?> = playerDao.getPlayerStats()
    val recentWorkouts: Flow<List<WorkoutLogEntity>> = workoutDao.getRecentWorkouts()
    val chatMessages: Flow<List<ChatMessageEntity>> = chatDao.getAllMessages()

    fun getTodayDateString(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return sdf.format(Date())
    }

    fun getDailyQuest(date: String = getTodayDateString()): Flow<DailyQuestEntity?> {
        return questDao.getDailyQuest(date)
    }

    suspend fun initializeIfNeeded() {
        val today = getTodayDateString()
        val quest = questDao.getDailyQuestDirect(today)
        if (quest == null) {
            questDao.insertOrUpdate(
                DailyQuestEntity(
                    date = today,
                    pushupsDone = 0,
                    pushupsTarget = 100,
                    situpsDone = 0,
                    situpsTarget = 100,
                    squatsDone = 0,
                    squatsTarget = 100,
                    cardioDone = 0,
                    cardioTarget = 100,
                    isCompleted = false,
                    isRewardClaimed = false
                )
            )
        }
    }

    suspend fun initPlayerIfEmpty() {
        playerDao.insertOrUpdate(
            PlayerStatsEntity(
                id = 1,
                playerName = "Hunter",
                title = "The Awakened",
                hunterRank = "E-Rank",
                level = 1,
                currentExp = 0,
                maxExp = 100,
                hp = 100,
                maxHp = 100,
                mp = 50,
                maxMp = 50,
                fatigue = 0,
                str = 10,
                agi = 10,
                vit = 10,
                intel = 10,
                per = 10,
                statPoints = 5,
                streakDays = 1,
                lastCompletedDate = ""
            )
        )
        // Add initial system greeting
        chatDao.insertMessage(
            ChatMessageEntity(
                sender = "SYSTEM",
                text = "[NOTIFICATION]\nYou have been chosen as the sole Player. The System is now synchronized with your physical vessel. Complete your Daily Quests to grow stronger, or face the Penalty Zone.",
                isSystemAlert = true
            )
        )
    }

    suspend fun logExercise(
        exerciseType: String, // "PUSHUPS", "SITUPS", "SQUATS", "CARDIO"
        reps: Int,
        currentStats: PlayerStatsEntity?,
        currentQuest: DailyQuestEntity?
    ): LogResult {
        val today = getTodayDateString()
        val quest = currentQuest ?: DailyQuestEntity(date = today)

        var newPushups = quest.pushupsDone
        var newSitups = quest.situpsDone
        var newSquats = quest.squatsDone
        var newCardio = quest.cardioDone

        var category = "STRENGTH"
        when (exerciseType) {
            "PUSHUPS" -> {
                newPushups = (newPushups + reps).coerceAtMost(quest.pushupsTarget)
                category = "STRENGTH"
            }
            "SITUPS" -> {
                newSitups = (newSitups + reps).coerceAtMost(quest.situpsTarget)
                category = "CORE"
            }
            "SQUATS" -> {
                newSquats = (newSquats + reps).coerceAtMost(quest.squatsTarget)
                category = "VITALITY"
            }
            "CARDIO" -> {
                newCardio = (newCardio + reps).coerceAtMost(quest.cardioTarget)
                category = "AGILITY"
            }
        }

        val allTargetsMet = newPushups >= quest.pushupsTarget &&
                newSitups >= quest.situpsTarget &&
                newSquats >= quest.squatsTarget &&
                newCardio >= quest.cardioTarget

        val updatedQuest = quest.copy(
            pushupsDone = newPushups,
            situpsDone = newSitups,
            squatsDone = newSquats,
            cardioDone = newCardio,
            isCompleted = allTargetsMet
        )
        questDao.insertOrUpdate(updatedQuest)

        val expGain = reps * 2
        val fatigueInc = (reps / 10).coerceAtLeast(1)

        // Workout log
        workoutDao.logWorkout(
            WorkoutLogEntity(
                exerciseName = exerciseType,
                count = reps,
                category = category,
                expEarned = expGain
            )
        )

        // Update player stats
        var leveledUp = false
        var newRank: String? = null
        if (currentStats != null) {
            var exp = currentStats.currentExp + expGain
            var lvl = currentStats.level
            var maxExp = currentStats.maxExp
            var statPoints = currentStats.statPoints
            var rank = currentStats.hunterRank
            var title = currentStats.title

            while (exp >= maxExp) {
                exp -= maxExp
                lvl += 1
                maxExp = (maxExp * 1.35f).toInt()
                statPoints += 3
                leveledUp = true

                // Update rank based on level
                val computedRank = when {
                    lvl >= 50 -> "Shadow Monarch"
                    lvl >= 40 -> "S-Rank"
                    lvl >= 30 -> "A-Rank"
                    lvl >= 20 -> "B-Rank"
                    lvl >= 10 -> "C-Rank"
                    lvl >= 5 -> "D-Rank"
                    else -> "E-Rank"
                }
                if (computedRank != rank) {
                    rank = computedRank
                    newRank = rank
                }

                if (lvl >= 10 && title == "The Awakened") {
                    title = "Wolf Slayer"
                } else if (lvl >= 30 && title == "Wolf Slayer") {
                    title = "Demon Castle Conqueror"
                } else if (lvl >= 50) {
                    title = "Monarch of Shadows"
                }
            }

            val updatedStats = currentStats.copy(
                level = lvl,
                currentExp = exp,
                maxExp = maxExp,
                statPoints = statPoints,
                hunterRank = rank,
                title = title,
                fatigue = (currentStats.fatigue + fatigueInc).coerceAtMost(100)
            )
            playerDao.insertOrUpdate(updatedStats)

            if (leveledUp) {
                chatDao.insertMessage(
                    ChatMessageEntity(
                        sender = "SYSTEM",
                        text = "[NOTIFICATION: LEVEL UP!]\nPlayer has reached Level $lvl! You have been granted 3 Stat Points. Current Rank: $rank.",
                        isSystemAlert = true
                    )
                )
            }
        }

        return LogResult(
            leveledUp = leveledUp,
            allCompleted = allTargetsMet && !quest.isCompleted,
            newRank = newRank
        )
    }

    suspend fun toggleQuestExercise(
        exerciseType: String,
        currentStats: PlayerStatsEntity?,
        currentQuest: DailyQuestEntity?
    ): LogResult {
        val quest = currentQuest ?: DailyQuestEntity(date = getTodayDateString())
        val isCurrentlyCompleted = when (exerciseType) {
            "PUSHUPS" -> quest.pushupsDone >= quest.pushupsTarget
            "SITUPS" -> quest.situpsDone >= quest.situpsTarget
            "SQUATS" -> quest.squatsDone >= quest.squatsTarget
            "CARDIO" -> quest.cardioDone >= quest.cardioTarget
            else -> false
        }

        return if (isCurrentlyCompleted) {
            // Uncheck: set back to 0
            val updated = when (exerciseType) {
                "PUSHUPS" -> quest.copy(pushupsDone = 0, isCompleted = false)
                "SITUPS" -> quest.copy(situpsDone = 0, isCompleted = false)
                "SQUATS" -> quest.copy(squatsDone = 0, isCompleted = false)
                "CARDIO" -> quest.copy(cardioDone = 0, isCompleted = false)
                else -> quest
            }
            questDao.insertOrUpdate(updated)
            LogResult(leveledUp = false, allCompleted = false, newRank = null)
        } else {
            // Check: complete the remaining reps
            val remaining = when (exerciseType) {
                "PUSHUPS" -> quest.pushupsTarget - quest.pushupsDone
                "SITUPS" -> quest.situpsTarget - quest.situpsDone
                "SQUATS" -> quest.squatsTarget - quest.squatsDone
                "CARDIO" -> quest.cardioTarget - quest.cardioDone
                else -> 0
            }
            if (remaining > 0) {
                logExercise(exerciseType, remaining, currentStats, quest)
            } else {
                LogResult(leveledUp = false, allCompleted = false, newRank = null)
            }
        }
    }

    suspend fun allocateStat(stat: String, stats: PlayerStatsEntity) {
        if (stats.statPoints <= 0) return
        val updated = when (stat) {
            "STR" -> stats.copy(str = stats.str + 1, statPoints = stats.statPoints - 1)
            "AGI" -> stats.copy(agi = stats.agi + 1, statPoints = stats.statPoints - 1)
            "VIT" -> {
                val newVit = stats.vit + 1
                val newMaxHp = 100 + (newVit - 10) * 15
                stats.copy(vit = newVit, maxHp = newMaxHp, hp = (stats.hp + 15).coerceAtMost(newMaxHp), statPoints = stats.statPoints - 1)
            }
            "INT" -> {
                val newInt = stats.intel + 1
                val newMaxMp = 50 + (newInt - 10) * 10
                stats.copy(intel = newInt, maxMp = newMaxMp, mp = (stats.mp + 10).coerceAtMost(newMaxMp), statPoints = stats.statPoints - 1)
            }
            "PER" -> stats.copy(per = stats.per + 1, statPoints = stats.statPoints - 1)
            else -> stats
        }
        playerDao.insertOrUpdate(updated)
    }

    suspend fun claimRewards(stats: PlayerStatsEntity, quest: DailyQuestEntity) {
        val today = getTodayDateString()
        questDao.insertOrUpdate(quest.copy(isRewardClaimed = true))

        var exp = stats.currentExp + 200
        var lvl = stats.level
        var maxExp = stats.maxExp
        var statPoints = stats.statPoints + 3

        while (exp >= maxExp) {
            exp -= maxExp
            lvl += 1
            maxExp = (maxExp * 1.35f).toInt()
            statPoints += 3
        }

        val updated = stats.copy(
            level = lvl,
            currentExp = exp,
            maxExp = maxExp,
            statPoints = statPoints,
            hp = stats.maxHp,
            mp = stats.maxMp,
            fatigue = 0, // Full Status Recovery!
            streakDays = stats.streakDays + 1,
            lastCompletedDate = today
        )
        playerDao.insertOrUpdate(updated)

        chatDao.insertMessage(
            ChatMessageEntity(
                sender = "SYSTEM",
                text = "[REWARD ACQUIRED: DAILY QUEST COMPLETE]\n- Status Recovery: Fatigue fully restored to 0%\n- Stat Points: +3 Granted\n- Blessed Random Box: Acquired",
                isSystemAlert = true
            )
        )
    }

    suspend fun addChatMessage(sender: String, message: String, isSystemAlert: Boolean = false) {
        chatDao.insertMessage(
            ChatMessageEntity(
                sender = sender,
                text = message,
                isSystemAlert = isSystemAlert
            )
        )
    }
}

data class LogResult(
    val leveledUp: Boolean,
    val allCompleted: Boolean,
    val newRank: String?
)
