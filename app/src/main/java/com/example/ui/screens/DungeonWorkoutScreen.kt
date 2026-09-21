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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Timer
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.SoloHoloCard
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

data class Dungeon(
    val id: String,
    val title: String,
    val rank: String,
    val description: String,
    val exercises: List<DungeonExercise>,
    val expReward: Int
)

data class DungeonExercise(
    val name: String,
    val type: String, // PUSHUPS, SQUATS, SITUPS, CARDIO
    val sets: String,
    val repsPerSet: Int,
    val targetMuscle: String
)

val DUNGEONS = listOf(
    Dungeon(
        id = "d1",
        title = "GATE: GOBLIN OUTPOST",
        rank = "E-Rank",
        description = "Beginner home calisthenics awakening. Suitable for early vessel conditioning.",
        expReward = 80,
        exercises = listOf(
            DungeonExercise("Standard Push-ups", "PUSHUPS", "3 Sets x 15 Reps", 15, "Chest & Triceps"),
            DungeonExercise("Bodyweight Squats", "SQUATS", "3 Sets x 20 Reps", 20, "Quads & Glutes"),
            DungeonExercise("Core Crunches", "SITUPS", "3 Sets x 20 Reps", 20, "Abdominals"),
            DungeonExercise("Jumping Jacks", "CARDIO", "3 Sets x 30 Reps", 30, "Cardiovascular")
        )
    ),
    Dungeon(
        id = "d2",
        title = "GATE: GORGON CAVERN",
        rank = "D-Rank",
        description = "Intermediate hypertrophy & stamina conditioning. Pushes past early limits.",
        expReward = 150,
        exercises = listOf(
            DungeonExercise("Diamond Push-ups", "PUSHUPS", "3 Sets x 12 Reps", 12, "Triceps & Inner Chest"),
            DungeonExercise("Walking Lunges", "SQUATS", "3 Sets x 15 Reps", 15, "Hamstrings & Balance"),
            DungeonExercise("Bicycle Crunches", "SITUPS", "3 Sets x 25 Reps", 25, "Obliques & Core"),
            DungeonExercise("Mountain Climbers", "CARDIO", "3 Sets x 35 Reps", 35, "Full Body Stamina")
        )
    ),
    Dungeon(
        id = "d3",
        title = "GATE: CERBERUS LAIR",
        rank = "C-Rank",
        description = "Advanced monarch preparation. High resistance home workout circuit.",
        expReward = 250,
        exercises = listOf(
            DungeonExercise("Pike Push-ups", "PUSHUPS", "4 Sets x 12 Reps", 12, "Deltoids & Traps"),
            DungeonExercise("Bulgarian Split Squats", "SQUATS", "4 Sets x 12 Reps", 12, "Single Leg Strength"),
            DungeonExercise("Hanging/Lying Leg Raises", "SITUPS", "4 Sets x 15 Reps", 15, "Lower Abs"),
            DungeonExercise("Burpees", "CARDIO", "4 Sets x 15 Reps", 15, "Maximum Heart Rate")
        )
    ),
    Dungeon(
        id = "d4",
        title = "GATE: DEMON CASTLE BOSS RAID",
        rank = "B-Rank",
        description = "Extreme trial for high-tier hunters. Explosive strength and relentless tenacity.",
        expReward = 400,
        exercises = listOf(
            DungeonExercise("Explosive Clapping Push-ups", "PUSHUPS", "5 Sets x 10 Reps", 10, "Fast-Twitch Muscle"),
            DungeonExercise("Pistol Squats (Assisted)", "SQUATS", "4 Sets x 8 Reps", 8, "Peak Leg Power"),
            DungeonExercise("Hollow Body Hold (45s)", "SITUPS", "4 Sets x 20 Reps", 20, "Total Core Tension"),
            DungeonExercise("High Knees Sprint", "CARDIO", "5 Sets x 40 Reps", 40, "Monarch Endurance")
        )
    )
)

@Composable
fun DungeonWorkoutScreen(
    onLogExercise: (String, Int) -> Unit,
    onStartRestTimer: (Int) -> Unit
) {
    var selectedDungeon by remember { mutableStateOf<Dungeon?>(null) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(SoloObsidian)
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 96.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            SoloHoloCard(title = "[ SYSTEM DUNGEON RAID DIRECTORY ]") {
                Text(
                    text = "Select an awakened Gate to conduct targeted home gym training. Completing gate sets logs directly to your Daily Quest and yields bonus combat EXP.",
                    color = SoloTextSecondary,
                    fontSize = 12.sp,
                    fontFamily = FontFamily.Monospace,
                    lineHeight = 18.sp
                )
            }
        }

        items(DUNGEONS) { dungeon ->
            DungeonCard(
                dungeon = dungeon,
                isExpanded = selectedDungeon?.id == dungeon.id,
                onToggle = {
                    selectedDungeon = if (selectedDungeon?.id == dungeon.id) null else dungeon
                },
                onCompleteExercise = { ex ->
                    onLogExercise(ex.type, ex.repsPerSet)
                    onStartRestTimer(45)
                }
            )
        }
    }
}

@Composable
fun DungeonCard(
    dungeon: Dungeon,
    isExpanded: Boolean,
    onToggle: () -> Unit,
    onCompleteExercise: (DungeonExercise) -> Unit
) {
    val borderColor = when (dungeon.rank) {
        "B-Rank" -> SoloMonarchPurple
        "C-Rank" -> SoloPenaltyRed
        "D-Rank" -> SoloCyanGlow
        else -> SoloElectricBlue
    }

    SoloHoloCard(borderColor = borderColor) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = dungeon.title,
                    color = SoloTextPrimary,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = dungeon.description,
                    color = SoloTextSecondary,
                    fontSize = 11.sp,
                    fontFamily = FontFamily.Monospace
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            SoloRankBadge(rank = dungeon.rank)
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "EXP REWARD: +${dungeon.expReward} EXP",
                color = SoloRankGold,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace
            )

            Button(
                onClick = onToggle,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isExpanded) SoloSurfaceVariant else borderColor
                ),
                shape = CutCornerShape(4.dp),
                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
            ) {
                Text(
                    text = if (isExpanded) "CLOSE GATE" else "ENTER GATE",
                    color = if (isExpanded) SoloTextPrimary else SoloObsidian,
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp,
                    fontFamily = FontFamily.Monospace
                )
            }
        }

        if (isExpanded) {
            Spacer(modifier = Modifier.height(14.dp))
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "[ DUNGEON TRAINING PROTOCOL ]",
                    color = SoloElectricBlue,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace
                )

                dungeon.exercises.forEach { ex ->
                    ExerciseRowItem(ex, onComplete = { onCompleteExercise(ex) })
                }
            }
        }
    }
}

@Composable
fun ExerciseRowItem(
    exercise: DungeonExercise,
    onComplete: () -> Unit
) {
    var completedCount by remember { mutableStateOf(0) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(CutCornerShape(4.dp))
            .background(SoloSurfaceVariant.copy(alpha = 0.6f))
            .border(BorderStroke(1.dp, SoloElectricBlue.copy(alpha = 0.25f)), CutCornerShape(4.dp))
            .padding(horizontal = 10.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = exercise.name,
                color = SoloTextPrimary,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace
            )
            Text(
                text = "${exercise.sets} • Target: ${exercise.targetMuscle}",
                color = SoloTextSecondary,
                fontSize = 10.sp,
                fontFamily = FontFamily.Monospace
            )
            if (completedCount > 0) {
                Text(
                    text = "Sets Logged: $completedCount",
                    color = SoloManaGreen,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace
                )
            }
        }

        OutlinedButton(
            onClick = {
                completedCount += 1
                onComplete()
            },
            shape = CutCornerShape(4.dp),
            border = BorderStroke(1.dp, SoloElectricBlue),
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = SoloElectricBlue.copy(alpha = 0.15f),
                contentColor = SoloElectricBlue
            ),
            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = "Log Set",
                modifier = Modifier.size(14.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "+${exercise.repsPerSet}",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace
            )
        }
    }
}
