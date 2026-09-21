package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ui.theme.SoloBackground
import com.example.ui.theme.SoloCyanGlow
import com.example.ui.theme.SoloElectricBlue
import com.example.ui.theme.SoloHoloBorder
import com.example.ui.theme.SoloHoloWindowBg
import com.example.ui.theme.SoloManaGreen
import com.example.ui.theme.SoloMonarchPurple
import com.example.ui.theme.SoloObsidian
import com.example.ui.theme.SoloPenaltyRed
import com.example.ui.theme.SoloRankGold
import com.example.ui.theme.SoloSurface
import com.example.ui.theme.SoloSurfaceHover
import com.example.ui.theme.SoloSurfaceVariant
import com.example.ui.theme.SoloTextMuted
import com.example.ui.theme.SoloTextPrimary
import com.example.ui.theme.SoloTextSecondary
import com.example.ui.theme.SoloCard3dPlate
import com.example.ui.theme.SoloCard3dPlateRaised
import com.example.ui.theme.SoloCardBottomShadow
import androidx.compose.ui.draw.shadow

@Composable
fun SoloHoloCard(
    title: String? = null,
    modifier: Modifier = Modifier,
    borderColor: Color = SoloElectricBlue,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = 6.dp,
                shape = CutCornerShape(topStart = 12.dp, bottomEnd = 12.dp, topEnd = 4.dp, bottomStart = 4.dp),
                ambientColor = borderColor.copy(alpha = 0.3f),
                spotColor = borderColor.copy(alpha = 0.5f)
            )
            .clip(CutCornerShape(topStart = 12.dp, bottomEnd = 12.dp, topEnd = 4.dp, bottomStart = 4.dp))
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        SoloCard3dPlateRaised,
                        SoloCard3dPlate,
                        SoloCardBottomShadow
                    )
                )
            )
            .border(
                BorderStroke(
                    1.2.dp,
                    Brush.verticalGradient(
                        listOf(
                            borderColor.copy(alpha = 0.85f),
                            borderColor.copy(alpha = 0.35f),
                            SoloCardBottomShadow
                        )
                    )
                ),
                CutCornerShape(topStart = 12.dp, bottomEnd = 12.dp, topEnd = 4.dp, bottomStart = 4.dp)
            )
            .padding(16.dp)
    ) {
        Column {
            if (!title.isNullOrBlank()) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                ) {
                    Text(
                        text = title.uppercase(),
                        color = borderColor,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.8.sp,
                        fontFamily = FontFamily.Monospace
                    )
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .shadow(2.dp, CircleShape)
                            .background(borderColor, CircleShape)
                    )
                }
            }
            content()
        }
    }
}

@Composable
fun SoloRankBadge(rank: String, modifier: Modifier = Modifier) {
    val (badgeBg, textColor) = when (rank) {
        "Shadow Monarch" -> SoloMonarchPurple to Color(0xFFF1E6FF)
        "S-Rank" -> SoloRankGold to Color(0xFF1E1300)
        "A-Rank" -> Color(0xFFE11D48) to Color.White
        "B-Rank" -> SoloElectricBlue to SoloObsidian
        "C-Rank" -> SoloManaGreen to SoloObsidian
        "D-Rank" -> SoloCyanGlow to SoloObsidian
        else -> Color(0xFF64748B) to Color.White
    }

    Box(
        modifier = modifier
            .clip(CutCornerShape(4.dp))
            .background(badgeBg)
            .border(BorderStroke(1.dp, textColor.copy(alpha = 0.5f)), CutCornerShape(4.dp))
            .padding(horizontal = 10.dp, vertical = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = rank.uppercase(),
            color = textColor,
            fontWeight = FontWeight.Black,
            fontSize = 11.sp,
            letterSpacing = 1.2.sp,
            fontFamily = FontFamily.Monospace
        )
    }
}

@Composable
fun SoloMeterBar(
    label: String,
    current: Int,
    max: Int,
    barColor: Color,
    modifier: Modifier = Modifier
) {
    val progress = if (max > 0) (current.toFloat() / max.toFloat()).coerceIn(0f, 1f) else 0f

    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label,
                color = SoloTextSecondary,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp,
                fontFamily = FontFamily.Monospace
            )
            Text(
                text = "$current / $max",
                color = SoloTextPrimary,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                fontFamily = FontFamily.Monospace
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(CutCornerShape(2.dp))
                .background(SoloSurfaceVariant)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(progress)
                    .height(8.dp)
                    .background(
                        Brush.horizontalGradient(
                            listOf(barColor.copy(alpha = 0.7f), barColor)
                        )
                    )
            )
        }
    }
}

@Composable
fun RestTimerFloatingHud(
    secondsRemaining: Int,
    onCancel: () -> Unit,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(
        visible = secondsRemaining > 0,
        enter = fadeIn(),
        exit = fadeOut(),
        modifier = modifier
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .border(BorderStroke(1.5.dp, SoloElectricBlue), RoundedCornerShape(12.dp)),
            colors = CardDefaults.cardColors(containerColor = SoloSurface.copy(alpha = 0.95f)),
            shape = RoundedCornerShape(12.dp)
        ) {
            Row(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 12.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Timer,
                        contentDescription = "Rest Timer",
                        tint = SoloElectricBlue,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "SYSTEM REST COUNTDOWN",
                            color = SoloElectricBlue,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        )
                        Text(
                            text = "${secondsRemaining}s REMAINING",
                            color = SoloTextPrimary,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Black,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                }
                IconButton(onClick = onCancel) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Cancel",
                        tint = SoloTextSecondary
                    )
                }
            }
        }
    }
}

@Composable
fun SystemNotificationDialog(
    message: String,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(BorderStroke(2.dp, SoloElectricBlue), CutCornerShape(12.dp)),
            shape = CutCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = SoloObsidian)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "[ SYSTEM NOTIFICATION ]",
                        color = SoloElectricBlue,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.5.sp,
                        fontFamily = FontFamily.Monospace
                    )
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .background(SoloElectricBlue, RoundedCornerShape(4.dp))
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = message,
                    color = SoloTextPrimary,
                    fontSize = 14.sp,
                    lineHeight = 20.sp,
                    fontFamily = FontFamily.Monospace
                )
                Spacer(modifier = Modifier.height(20.dp))
                Button(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = SoloElectricBlue),
                    shape = CutCornerShape(6.dp)
                ) {
                    Text(
                        text = "CONFIRM",
                        color = SoloObsidian,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.5.sp,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }
        }
    }
}
