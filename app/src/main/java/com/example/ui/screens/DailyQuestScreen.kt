package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.HourglassBottom
import androidx.compose.material.icons.filled.NordicWalking
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material.icons.filled.SportsGymnastics
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.db.DailyQuestEntity
import com.example.data.db.PlayerStatsEntity
import com.example.ui.components.SoloHoloCard
import com.example.ui.components.SoloMeterBar
import com.example.ui.components.SoloRankBadge
import com.example.ui.components.HunterQuestCard
import com.example.ui.components.Game3DCard
import com.example.ui.components.Game3DButton
import com.example.ui.theme.SoloBackground
import com.example.ui.theme.SoloCyanGlow
import com.example.ui.theme.SoloElectricBlue
import com.example.ui.theme.SoloHoloWindowBg
import com.example.ui.theme.SoloManaGreen
import com.example.ui.theme.SoloMonarchPurple
import com.example.ui.theme.SoloObsidian
import com.example.ui.theme.SoloPenaltyRed
import com.example.ui.theme.SoloRankGold
import com.example.ui.theme.SoloSurface
import com.example.ui.theme.SoloSurfaceVariant
import com.example.ui.theme.SoloTextMuted
import com.example.ui.theme.SoloTextPrimary
import com.example.ui.theme.SoloTextSecondary
import com.example.ui.theme.SoloCard3dPlateRaised
import com.example.ui.theme.SoloCardBottomShadow

@Composable
fun DailyQuestScreen(
    stats: PlayerStatsEntity?,
    quest: DailyQuestEntity?,
    onLogExercise: (String, Int) -> Unit,
    onToggleExercise: (String) -> Unit = {},
    onStartRestTimer: (Int) -> Unit,
    onClaimRewards: () -> Unit
) {
    val q = quest ?: DailyQuestEntity(date = "Today")

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(SoloObsidian)
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 96.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Hero Banner with Solo Leveling aesthetic
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(170.dp)
                    .clip(CutCornerShape(topStart = 16.dp, bottomEnd = 16.dp))
                    .border(
                        BorderStroke(1.5.dp, SoloElectricBlue.copy(alpha = 0.8f)),
                        CutCornerShape(topStart = 16.dp, bottomEnd = 16.dp)
                    )
            ) {
                Image(
                    painter = painterResource(id = R.drawable.solo_hunter_banner),
                    contentDescription = "Solo Leveling Night Gym",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                // Dark gradient overlay
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                listOf(
                                    Color.Transparent,
                                    SoloObsidian.copy(alpha = 0.85f),
                                    SoloObsidian
                                )
                            )
                        )
                )
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(14.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        SoloRankBadge(rank = stats?.hunterRank ?: "E-Rank")
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "LEVEL ${stats?.level ?: 1}",
                            color = SoloElectricBlue,
                            fontWeight = FontWeight.Black,
                            fontSize = 13.sp,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "THE SYSTEM: HOME AWAKENING",
                        color = SoloTextPrimary,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Black,
                        fontFamily = FontFamily.Monospace,
                        letterSpacing = 1.sp
                    )
                }
            }
        }

        // Daily Quest Hologram Header
        item {
            SoloHoloCard(
                title = "[ QUEST: PREPARING TO BECOME STRONG ]",
                borderColor = if (q.isCompleted) SoloManaGreen else SoloElectricBlue
            ) {
                Text(
                    text = "Goal: Perform daily physical calibration to reconstruct muscle vessels and accumulate monarch energy.",
                    color = SoloTextSecondary,
                    fontSize = 12.sp,
                    fontFamily = FontFamily.Monospace,
                    lineHeight = 18.sp
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Penalty Quest Warning
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(CutCornerShape(6.dp))
                        .background(SoloPenaltyRed.copy(alpha = 0.15f))
                        .border(BorderStroke(1.dp, SoloPenaltyRed.copy(alpha = 0.5f)), CutCornerShape(6.dp))
                        .padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = "Penalty Warning",
                        tint = SoloPenaltyRed,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "PENALTY WARNING: Incomplete quest before midnight triggers 4-hour survival in the Poison Desert!",
                        color = SoloPenaltyRed,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }
        }

        // Quest Objectives: 4 Core Exercises displayed as 3D Game Hunter Quest Cards
        item {
            HunterQuestCard(
                title = "PUSH-UPS",
                category = "STRENGTH",
                rewardPoints = 25,
                icon = Icons.Default.FitnessCenter,
                current = q.pushupsDone,
                target = q.pushupsTarget,
                accentColor = SoloElectricBlue,
                onCheckedChange = { onToggleExercise("PUSHUPS") },
                onQuickLog = { count -> onLogExercise("PUSHUPS", count) }
            )
        }

        item {
            HunterQuestCard(
                title = "SIT-UPS / CORE",
                category = "CORE DEFENSE",
                rewardPoints = 20,
                icon = Icons.Default.SelfImprovement,
                current = q.situpsDone,
                target = q.situpsTarget,
                accentColor = SoloMonarchPurple,
                onCheckedChange = { onToggleExercise("SITUPS") },
                onQuickLog = { count -> onLogExercise("SITUPS", count) }
            )
        }

        item {
            HunterQuestCard(
                title = "SQUATS",
                category = "VITALITY",
                rewardPoints = 25,
                icon = Icons.Default.SportsGymnastics,
                current = q.squatsDone,
                target = q.squatsTarget,
                accentColor = SoloCyanGlow,
                onCheckedChange = { onToggleExercise("SQUATS") },
                onQuickLog = { count -> onLogExercise("SQUATS", count) }
            )
        }

        item {
            HunterQuestCard(
                title = "HOME CARDIO (JUMPING JACKS)",
                category = "AGILITY",
                rewardPoints = 30,
                icon = Icons.Default.NordicWalking,
                current = q.cardioDone,
                target = q.cardioTarget,
                accentColor = SoloRankGold,
                onCheckedChange = { onToggleExercise("CARDIO") },
                onQuickLog = { count -> onLogExercise("CARDIO", count) }
            )
        }

        // Rest Timer Quick Triggers
        item {
            SoloHoloCard(title = "[ SYSTEM REST INTERVAL CONTROLLER ]") {
                Text(
                    text = "Optimal hypertrophy recovery between sets:",
                    color = SoloTextSecondary,
                    fontSize = 12.sp,
                    fontFamily = FontFamily.Monospace
                )
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    RestTimerButton("30s REST", 30, onStartRestTimer, Modifier.weight(1f))
                    RestTimerButton("60s REST", 60, onStartRestTimer, Modifier.weight(1f))
                    RestTimerButton("90s REST", 90, onStartRestTimer, Modifier.weight(1f))
                }
            }
        }

        // Quest Completion & Rewards
        item {
            SoloHoloCard(
                title = "[ QUEST REWARDS ]",
                borderColor = if (q.isCompleted) SoloRankGold else SoloElectricBlue
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    RewardRow("1. Status Recovery", "Dispels 100% Fatigue & Restores HP/MP")
                    RewardRow("2. Stat Points", "+3 Unallocated Attribute Points")
                    RewardRow("3. Blessed Random Box", "C-Tier or Higher Artifact Loot")

                    Spacer(modifier = Modifier.height(8.dp))

                    if (q.isCompleted) {
                        if (q.isRewardClaimed) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = "Claimed",
                                    tint = SoloManaGreen,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "REWARDS ALREADY CLAIMED FOR TODAY",
                                    color = SoloManaGreen,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    fontFamily = FontFamily.Monospace
                                )
                            }
                        } else {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(CutCornerShape(8.dp))
                                    .background(
                                        Brush.verticalGradient(
                                            listOf(
                                                SoloRankGold,
                                                Color(0xFFD97706)
                                            )
                                        )
                                    )
                                    .border(
                                        BorderStroke(1.5.dp, Color(0xFFFEF08A)),
                                        CutCornerShape(8.dp)
                                    )
                                    .clickable { onClaimRewards() }
                                    .padding(vertical = 14.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "★ CLAIM DAILY QUEST REWARDS ★",
                                    color = SoloObsidian,
                                    fontWeight = FontWeight.Black,
                                    fontSize = 14.sp,
                                    fontFamily = FontFamily.Monospace,
                                    letterSpacing = 1.4.sp
                                )
                            }
                        }
                    } else {
                        Text(
                            text = "[STATUS: IN PROGRESS - COMPLETE ALL OBJECTIVES TO UNLOCK REWARDS]",
                            color = SoloTextMuted,
                            fontSize = 11.sp,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ExerciseQuestItem(
    title: String,
    icon: ImageVector,
    current: Int,
    target: Int,
    accentColor: Color,
    onLog: (Int) -> Unit
) {
    val isDone = current >= target

    SoloHoloCard(borderColor = if (isDone) SoloManaGreen else accentColor) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CutCornerShape(4.dp))
                        .background(accentColor.copy(alpha = 0.2f))
                        .border(BorderStroke(1.dp, accentColor), CutCornerShape(4.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = title,
                        tint = accentColor,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = title,
                        color = SoloTextPrimary,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )
                    Text(
                        text = if (isDone) "COMPLETED [100%]" else "REMAINING: ${target - current} REPS",
                        color = if (isDone) SoloManaGreen else SoloTextSecondary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }

            if (isDone) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Completed",
                    tint = SoloManaGreen,
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        SoloMeterBar(
            label = "TARGET PROGRESS",
            current = current,
            max = target,
            barColor = if (isDone) SoloManaGreen else accentColor
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Quick log buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            LogButton("+5", enabled = !isDone) { onLog(5) }
            LogButton("+10", enabled = !isDone) { onLog(10) }
            LogButton("+25", enabled = !isDone) { onLog(25) }
            LogButton("+50", enabled = !isDone) { onLog(50) }
        }
    }
}

@Composable
fun LogButton(
    label: String,
    enabled: Boolean,
    onClick: () -> Unit
) {
    OutlinedButton(
        onClick = onClick,
        enabled = enabled,
        modifier = Modifier.height(34.dp),
        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
        border = BorderStroke(1.dp, if (enabled) SoloElectricBlue else SoloTextMuted),
        shape = CutCornerShape(4.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = SoloSurfaceVariant.copy(alpha = 0.7f),
            contentColor = SoloElectricBlue
        )
    ) {
        Text(
            text = label,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Monospace
        )
    }
}

@Composable
fun RestTimerButton(
    label: String,
    seconds: Int,
    onStart: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .height(38.dp)
            .clip(CutCornerShape(4.dp))
            .background(
                Brush.verticalGradient(
                    listOf(
                        SoloCard3dPlateRaised,
                        SoloCardBottomShadow
                    )
                )
            )
            .border(BorderStroke(1.2.dp, SoloElectricBlue.copy(alpha = 0.65f)), CutCornerShape(4.dp))
            .clickable { onStart(seconds) }
            .padding(horizontal = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Default.Timer,
                contentDescription = null,
                tint = SoloElectricBlue,
                modifier = Modifier.size(14.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = label,
                color = SoloElectricBlue,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace
            )
        }
    }
}

@Composable
fun RewardRow(title: String, desc: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            color = SoloRankGold,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Monospace
        )
        Text(
            text = desc,
            color = SoloTextSecondary,
            fontSize = 11.sp,
            fontFamily = FontFamily.Monospace
        )
    }
}
