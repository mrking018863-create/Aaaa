package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.db.PlayerStatsEntity
import com.example.data.db.WorkoutLogEntity
import com.example.ui.components.SoloHoloCard
import com.example.ui.components.SoloMeterBar
import com.example.ui.components.SoloRankBadge
import com.example.ui.theme.SoloCyanGlow
import com.example.ui.theme.SoloElectricBlue
import com.example.ui.theme.SoloManaGreen
import com.example.ui.theme.SoloMonarchPurple
import com.example.ui.theme.SoloObsidian
import com.example.ui.theme.SoloPenaltyRed
import com.example.ui.theme.SoloRankGold
import com.example.ui.theme.SoloSurfaceVariant
import com.example.ui.theme.SoloTextMuted
import com.example.ui.theme.SoloTextPrimary
import com.example.ui.theme.SoloTextSecondary
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun StatusWindowScreen(
    stats: PlayerStatsEntity?,
    recentLogs: List<WorkoutLogEntity>,
    onAllocateStat: (String) -> Unit
) {
    val s = stats ?: PlayerStatsEntity()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(SoloObsidian)
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 96.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Holographic Header
        item {
            SoloHoloCard(title = "[ STATUS WINDOW ]", borderColor = SoloElectricBlue) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "NAME: ${s.playerName.uppercase()}",
                            color = SoloTextPrimary,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Black,
                            fontFamily = FontFamily.Monospace
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "JOB / TITLE: ${s.title.uppercase()}",
                            color = SoloMonarchPurple,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                    SoloRankBadge(rank = s.hunterRank)
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Level and EXP
                SoloMeterBar(
                    label = "LEVEL ${s.level} (EXP)",
                    current = s.currentExp,
                    max = s.maxExp,
                    barColor = SoloElectricBlue
                )

                Spacer(modifier = Modifier.height(12.dp))

                // HP, MP, Fatigue
                SoloMeterBar(
                    label = "HP (HEALTH POINTS)",
                    current = s.hp,
                    max = s.maxHp,
                    barColor = SoloPenaltyRed
                )
                Spacer(modifier = Modifier.height(8.dp))
                SoloMeterBar(
                    label = "MP (MANA POINTS)",
                    current = s.mp,
                    max = s.maxMp,
                    barColor = SoloCyanGlow
                )
                Spacer(modifier = Modifier.height(8.dp))
                SoloMeterBar(
                    label = "FATIGUE GAUGE",
                    current = s.fatigue,
                    max = 100,
                    barColor = if (s.fatigue > 70) SoloPenaltyRed else SoloRankGold
                )
            }
        }

        // Core Attributes / Stat Allocation
        item {
            SoloHoloCard(
                title = "[ ATTRIBUTES & PHYSICAL STATS ]",
                borderColor = if (s.statPoints > 0) SoloRankGold else SoloElectricBlue
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "AVAILABLE STAT POINTS:",
                        color = SoloTextSecondary,
                        fontSize = 12.sp,
                        fontFamily = FontFamily.Monospace
                    )
                    Text(
                        text = "${s.statPoints} PTS",
                        color = if (s.statPoints > 0) SoloRankGold else SoloTextMuted,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Black,
                        fontFamily = FontFamily.Monospace
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                StatRowItem(
                    name = "STR (STRENGTH)",
                    desc = "Increases muscular force & push-up capacity",
                    value = s.str,
                    icon = Icons.Default.Bolt,
                    accentColor = SoloPenaltyRed,
                    canAdd = s.statPoints > 0,
                    onAdd = { onAllocateStat("STR") }
                )

                StatRowItem(
                    name = "AGI (AGILITY)",
                    desc = "Increases explosive speed & cardio stamina",
                    value = s.agi,
                    icon = Icons.Default.Speed,
                    accentColor = SoloElectricBlue,
                    canAdd = s.statPoints > 0,
                    onAdd = { onAllocateStat("AGI") }
                )

                StatRowItem(
                    name = "VIT (VITALITY)",
                    desc = "Expands maximum HP & speeds muscle recovery",
                    value = s.vit,
                    icon = Icons.Default.Shield,
                    accentColor = SoloManaGreen,
                    canAdd = s.statPoints > 0,
                    onAdd = { onAllocateStat("VIT") }
                )

                StatRowItem(
                    name = "INT (INTELLIGENCE)",
                    desc = "Expands MP & neurological mind-muscle connection",
                    value = s.intel,
                    icon = Icons.Default.Psychology,
                    accentColor = SoloMonarchPurple,
                    canAdd = s.statPoints > 0,
                    onAdd = { onAllocateStat("INT") }
                )

                StatRowItem(
                    name = "PER (PERCEPTION)",
                    desc = "Sharpens workout kinetic form & muscle mind awareness",
                    value = s.per,
                    icon = Icons.Default.Visibility,
                    accentColor = SoloRankGold,
                    canAdd = s.statPoints > 0,
                    onAdd = { onAllocateStat("PER") }
                )
            }
        }

        // Hunter Skills & Buffs
        item {
            SoloHoloCard(title = "[ ACTIVE HUNTER BUFFS & TITLES ]") {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    BuffRow("Monarch's Conditioning", "Daily Quest workout streak: ${s.streakDays} Days active")
                    BuffRow("Iron Body (Passive)", "Resistance to fatigue increased by ${s.vit * 2}%")
                    BuffRow("Shadow Domain (Latent)", "High rank gym calisthenics unlocks shadowy monarch presence")
                }
            }
        }

        // Recent Workout History
        item {
            SoloHoloCard(title = "[ RECENT TRAINING LOGS ]") {
                if (recentLogs.isEmpty()) {
                    Text(
                        text = "No training logs registered yet. Execute reps in the Daily Quest tab.",
                        color = SoloTextMuted,
                        fontSize = 12.sp,
                        fontFamily = FontFamily.Monospace
                    )
                } else {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        recentLogs.take(5).forEach { log ->
                            WorkoutLogItem(log)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun StatRowItem(
    name: String,
    desc: String,
    value: Int,
    icon: ImageVector,
    accentColor: Color,
    canAdd: Boolean,
    onAdd: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clip(CutCornerShape(4.dp))
            .background(SoloSurfaceVariant.copy(alpha = 0.4f))
            .border(BorderStroke(0.8.dp, accentColor.copy(alpha = 0.3f)), CutCornerShape(4.dp))
            .padding(horizontal = 10.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
            Icon(
                imageVector = icon,
                contentDescription = name,
                tint = accentColor,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(
                    text = name,
                    color = SoloTextPrimary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace
                )
                Text(
                    text = desc,
                    color = SoloTextSecondary,
                    fontSize = 10.sp,
                    fontFamily = FontFamily.Monospace
                )
            }
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "$value",
                color = accentColor,
                fontSize = 16.sp,
                fontWeight = FontWeight.Black,
                fontFamily = FontFamily.Monospace
            )
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                onClick = onAdd,
                enabled = canAdd,
                modifier = Modifier.size(30.dp),
                contentPadding = PaddingValues(0.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (canAdd) SoloRankGold else SoloTextMuted
                ),
                shape = CutCornerShape(4.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Point",
                    tint = SoloObsidian,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}

@Composable
fun BuffRow(title: String, detail: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "• $title",
            color = SoloCyanGlow,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Monospace
        )
        Text(
            text = detail,
            color = SoloTextSecondary,
            fontSize = 11.sp,
            fontFamily = FontFamily.Monospace
        )
    }
}

@Composable
fun WorkoutLogItem(log: WorkoutLogEntity) {
    val dateStr = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date(log.timestamp))

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "[$dateStr]",
                color = SoloTextMuted,
                fontSize = 11.sp,
                fontFamily = FontFamily.Monospace
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "${log.exerciseName} (+${log.count} reps)",
                color = SoloTextPrimary,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                fontFamily = FontFamily.Monospace
            )
        }
        Text(
            text = "+${log.expEarned} EXP",
            color = SoloElectricBlue,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Monospace
        )
    }
}
