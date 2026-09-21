package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Assignment
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.SoloGymViewModel
import com.example.ui.components.RestTimerFloatingHud
import com.example.ui.components.SystemNotificationDialog
import com.example.ui.screens.DailyQuestScreen
import com.example.ui.screens.DungeonWorkoutScreen
import com.example.ui.screens.StatusWindowScreen
import com.example.ui.screens.SystemChatScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.SoloElectricBlue
import com.example.ui.theme.SoloObsidian
import com.example.ui.theme.SoloSurface
import com.example.ui.theme.SoloTextMuted
import com.example.ui.theme.SoloTextPrimary
import com.example.ui.theme.SoloTextSecondary

enum class SoloNavTab(val label: String, val icon: ImageVector) {
    DAILY_QUEST("QUEST", Icons.AutoMirrored.Filled.Assignment),
    STATUS("STATUS", Icons.Default.Shield),
    DUNGEONS("DUNGEON", Icons.Default.FitnessCenter),
    SYSTEM("SYSTEM", Icons.AutoMirrored.Filled.Chat)
}

class MainActivity : ComponentActivity() {
    private val viewModel: SoloGymViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                SoloGymApp(viewModel)
            }
        }
    }
}

@Composable
fun SoloGymApp(viewModel: SoloGymViewModel) {
    var selectedTab by remember { mutableStateOf(SoloNavTab.DAILY_QUEST) }

    val stats by viewModel.playerStats.collectAsStateWithLifecycle()
    val quest by viewModel.dailyQuest.collectAsStateWithLifecycle()
    val chatMessages by viewModel.chatMessages.collectAsStateWithLifecycle()
    val recentWorkouts by viewModel.recentWorkouts.collectAsStateWithLifecycle()
    val isGenerating by viewModel.isGenerating.collectAsStateWithLifecycle()
    val isVoiceMuted by viewModel.isVoiceMuted.collectAsStateWithLifecycle()
    val restTimerSeconds by viewModel.restTimerSeconds.collectAsStateWithLifecycle()
    val systemAlert by viewModel.systemAlert.collectAsStateWithLifecycle()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = SoloObsidian,
        bottomBar = {
            NavigationBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(68.dp)
                    .border(BorderStroke(1.dp, SoloElectricBlue.copy(alpha = 0.25f))),
                containerColor = SoloSurface
            ) {
                SoloNavTab.values().forEach { tab ->
                    val isSelected = selectedTab == tab
                    NavigationBarItem(
                        selected = isSelected,
                        onClick = { selectedTab = tab },
                        icon = {
                            Icon(
                                imageVector = tab.icon,
                                contentDescription = tab.label,
                                modifier = Modifier.size(20.dp)
                            )
                        },
                        label = {
                            Text(
                                text = tab.label,
                                fontSize = 10.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                fontFamily = FontFamily.Monospace,
                                letterSpacing = 1.sp
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = SoloObsidian,
                            selectedTextColor = SoloElectricBlue,
                            indicatorColor = SoloElectricBlue,
                            unselectedIconColor = SoloTextMuted,
                            unselectedTextColor = SoloTextMuted
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Crossfade(targetState = selectedTab, label = "TabCrossfade") { tab ->
                when (tab) {
                    SoloNavTab.DAILY_QUEST -> {
                        DailyQuestScreen(
                            stats = stats,
                            quest = quest,
                            onLogExercise = { type, count -> viewModel.logExercise(type, count) },
                            onToggleExercise = { type -> viewModel.toggleQuestExercise(type) },
                            onStartRestTimer = { s -> viewModel.startRestTimer(s) },
                            onClaimRewards = { viewModel.claimRewards() }
                        )
                    }
                    SoloNavTab.STATUS -> {
                        StatusWindowScreen(
                            stats = stats,
                            recentLogs = recentWorkouts,
                            onAllocateStat = { stat -> viewModel.allocateStat(stat) }
                        )
                    }
                    SoloNavTab.DUNGEONS -> {
                        DungeonWorkoutScreen(
                            onLogExercise = { type, count -> viewModel.logExercise(type, count) },
                            onStartRestTimer = { s -> viewModel.startRestTimer(s) }
                        )
                    }
                    SoloNavTab.SYSTEM -> {
                        SystemChatScreen(
                            messages = chatMessages,
                            isGenerating = isGenerating,
                            isVoiceMuted = isVoiceMuted,
                            onSendMessage = { text -> viewModel.sendChatMessage(text) },
                            onToggleVoice = { viewModel.toggleVoiceMute() },
                            onSpeakMessage = { text -> viewModel.speakSystemText(text) }
                        )
                    }
                }
            }

            // Floating Rest Timer HUD if active
            RestTimerFloatingHud(
                secondsRemaining = restTimerSeconds,
                onCancel = { viewModel.cancelRestTimer() },
                modifier = Modifier.align(Alignment.BottomCenter)
            )

            // System Notification Dialog for Level-Up or Quest Complete
            systemAlert?.let { alertText ->
                SystemNotificationDialog(
                    message = alertText,
                    onDismiss = { viewModel.dismissSystemAlert() }
                )
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(text = "Hello $name!", modifier = modifier)
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MyApplicationTheme { Greeting("Android") }
}
