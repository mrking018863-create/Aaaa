package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.SoloBackground
import com.example.ui.theme.SoloCard3dPlate
import com.example.ui.theme.SoloCard3dPlateRaised
import com.example.ui.theme.SoloCardBottomShadow
import com.example.ui.theme.SoloCardTopBevel
import com.example.ui.theme.SoloCyanGlow
import com.example.ui.theme.SoloElectricBlue
import com.example.ui.theme.SoloHoloBorder
import com.example.ui.theme.SoloManaGreen
import com.example.ui.theme.SoloMonarchPurple
import com.example.ui.theme.SoloObsidian
import com.example.ui.theme.SoloPenaltyRed
import com.example.ui.theme.SoloRankGold
import com.example.ui.theme.SoloSurfaceHover
import com.example.ui.theme.SoloSurfaceVariant
import com.example.ui.theme.SoloTextMuted
import com.example.ui.theme.SoloTextPrimary
import com.example.ui.theme.SoloTextSecondary

/**
 * 3D Isometric Holographic System Container for Solo Leveling Game Aesthetic.
 * Features beveled borders, simulated top-lit lighting, drop shadows, and sci-fi angular cuts.
 */
@Composable
fun Game3DCard(
    modifier: Modifier = Modifier,
    borderColor: Color = SoloElectricBlue,
    isCompleted: Boolean = false,
    content: @Composable () -> Unit
) {
    val activeBorder = if (isCompleted) SoloManaGreen else borderColor
    val glowColor = if (isCompleted) SoloManaGreen.copy(alpha = 0.25f) else borderColor.copy(alpha = 0.15f)

    Box(
        modifier = modifier
            .fillMaxWidth()
            // 3D Shadow layer
            .shadow(
                elevation = 8.dp,
                shape = CutCornerShape(topStart = 14.dp, bottomEnd = 14.dp, topEnd = 4.dp, bottomStart = 4.dp),
                ambientColor = activeBorder,
                spotColor = activeBorder
            )
            .clip(CutCornerShape(topStart = 14.dp, bottomEnd = 14.dp, topEnd = 4.dp, bottomStart = 4.dp))
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        SoloCard3dPlateRaised,
                        SoloCard3dPlate,
                        SoloCardBottomShadow
                    )
                )
            )
            // Bevel simulated 3D edge: top highlight and cyber borders
            .border(
                BorderStroke(
                    width = 1.5.dp,
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            activeBorder.copy(alpha = 0.85f),
                            activeBorder.copy(alpha = 0.35f),
                            SoloCardBottomShadow
                        )
                    )
                ),
                CutCornerShape(topStart = 14.dp, bottomEnd = 14.dp, topEnd = 4.dp, bottomStart = 4.dp)
            )
            .drawBehind {
                // Subtle holographic sci-fi top-edge bevel line
                drawLine(
                    color = activeBorder.copy(alpha = 0.6f),
                    start = Offset(20f, 2f),
                    end = Offset(size.width - 20f, 2f),
                    strokeWidth = 2f
                )
                // Left 3D light accent notch
                drawLine(
                    color = activeBorder.copy(alpha = 0.4f),
                    start = Offset(2f, 20f),
                    end = Offset(2f, size.height - 20f),
                    strokeWidth = 2f
                )
            }
            .padding(14.dp)
    ) {
        content()
    }
}

/**
 * 3D Game-like Daily Hunter Quest Card.
 * Displays title, reward points, completion status checkbox, progress meter, and quick-action triggers.
 */
@Composable
fun HunterQuestCard(
    title: String,
    category: String,
    rewardPoints: Int,
    current: Int,
    target: Int,
    icon: ImageVector,
    accentColor: Color = SoloElectricBlue,
    onCheckedChange: (Boolean) -> Unit,
    onQuickLog: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val isCompleted = current >= target
    val progress = if (target > 0) (current.toFloat() / target.toFloat()).coerceIn(0f, 1f) else 0f

    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.985f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "press_scale"
    )

    val cardBorder by animateColorAsState(
        targetValue = if (isCompleted) SoloManaGreen else accentColor,
        animationSpec = tween(durationMillis = 250),
        label = "border_color"
    )

    Game3DCard(
        modifier = modifier
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .testTag("quest_card_${title.lowercase().replace(" ", "_")}"),
        borderColor = cardBorder,
        isCompleted = isCompleted
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Header Row: Quest Badge, Category & 3D Status Checkbox
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Left: 3D Holographic Icon & Title Info
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    // 3D Embossed Icon Emblem
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .shadow(4.dp, CutCornerShape(6.dp), spotColor = cardBorder)
                            .clip(CutCornerShape(6.dp))
                            .background(
                                Brush.verticalGradient(
                                    listOf(
                                        cardBorder.copy(alpha = 0.28f),
                                        SoloObsidian
                                    )
                                )
                            )
                            .border(BorderStroke(1.2.dp, cardBorder), CutCornerShape(6.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = title,
                            tint = cardBorder,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "QUEST",
                                color = SoloCyanGlow,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace,
                                letterSpacing = 1.2.sp
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "• $category",
                                color = SoloTextMuted,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Medium,
                                fontFamily = FontFamily.Monospace
                            )
                        }
                        Text(
                            text = title,
                            color = SoloTextPrimary,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.ExtraBold,
                            fontFamily = FontFamily.Monospace,
                            letterSpacing = 0.5.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                // Right: 3D Game Checkbox with reward chip
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End
                ) {
                    // Reward Point Pill
                    Box(
                        modifier = Modifier
                            .clip(CutCornerShape(topStart = 4.dp, bottomEnd = 4.dp))
                            .background(
                                Brush.horizontalGradient(
                                    listOf(
                                        SoloRankGold.copy(alpha = 0.2f),
                                        SoloRankGold.copy(alpha = 0.08f)
                                    )
                                )
                            )
                            .border(BorderStroke(1.dp, SoloRankGold.copy(alpha = 0.7f)), CutCornerShape(topStart = 4.dp, bottomEnd = 4.dp))
                            .padding(horizontal = 7.dp, vertical = 3.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Diamond,
                                contentDescription = "Reward Points",
                                tint = SoloRankGold,
                                modifier = Modifier.size(11.dp)
                            )
                            Spacer(modifier = Modifier.width(3.dp))
                            Text(
                                text = "+$rewardPoints PTS",
                                color = SoloRankGold,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    // 3D Game Checkbox Button
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .shadow(3.dp, RoundedCornerShape(6.dp))
                            .clip(RoundedCornerShape(6.dp))
                            .background(
                                if (isCompleted)
                                    Brush.verticalGradient(listOf(SoloManaGreen, Color(0xFF047857)))
                                else
                                    Brush.verticalGradient(listOf(SoloCard3dPlateRaised, SoloCardBottomShadow))
                            )
                            .border(
                                BorderStroke(
                                    1.5.dp,
                                    if (isCompleted) SoloManaGreen else cardBorder.copy(alpha = 0.5f)
                                ),
                                RoundedCornerShape(6.dp)
                            )
                            .clickable(
                                interactionSource = interactionSource,
                                indication = null
                            ) {
                                onCheckedChange(!isCompleted)
                            }
                            .testTag("checkbox_${title.lowercase().replace(" ", "_")}"),
                        contentAlignment = Alignment.Center
                    ) {
                        if (isCompleted) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Quest Completed",
                                tint = SoloObsidian,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 3D Sci-Fi Segmented Progress Meter Bar
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (isCompleted) "STATUS: CLEARED" else "PROGRESS: $current / $target REPS",
                        color = if (isCompleted) SoloManaGreen else SoloTextSecondary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace,
                        letterSpacing = 0.8.sp
                    )
                    Text(
                        text = "${(progress * 100).toInt()}%",
                        color = if (isCompleted) SoloManaGreen else cardBorder,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Black,
                        fontFamily = FontFamily.Monospace
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                // 3D Meter Track with Inset Depth
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(10.dp)
                        .clip(RoundedCornerShape(3.dp))
                        .background(SoloCardBottomShadow)
                        .border(BorderStroke(1.dp, cardBorder.copy(alpha = 0.35f)), RoundedCornerShape(3.dp))
                ) {
                    if (progress > 0f) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(progress)
                                .height(10.dp)
                                .clip(RoundedCornerShape(2.dp))
                                .background(
                                    Brush.horizontalGradient(
                                        listOf(
                                            cardBorder.copy(alpha = 0.7f),
                                            cardBorder
                                        )
                                    )
                                )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Interactive Quick Increment Rep Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Game3DButton(
                    label = "+5",
                    enabled = !isCompleted,
                    accentColor = cardBorder,
                    modifier = Modifier.weight(1f),
                    onClick = { onQuickLog(5) }
                )
                Game3DButton(
                    label = "+10",
                    enabled = !isCompleted,
                    accentColor = cardBorder,
                    modifier = Modifier.weight(1f),
                    onClick = { onQuickLog(10) }
                )
                Game3DButton(
                    label = "+25",
                    enabled = !isCompleted,
                    accentColor = cardBorder,
                    modifier = Modifier.weight(1f),
                    onClick = { onQuickLog(25) }
                )
                Game3DButton(
                    label = "MAX",
                    enabled = !isCompleted,
                    accentColor = SoloRankGold,
                    modifier = Modifier.weight(1f),
                    onClick = { onQuickLog(target - current) }
                )
            }
        }
    }
}

/**
 * Tactile 3D Sci-Fi Button with beveled edges and physical click depth.
 */
@Composable
fun Game3DButton(
    label: String,
    enabled: Boolean,
    accentColor: Color = SoloElectricBlue,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val yOffset by animateFloatAsState(
        targetValue = if (isPressed && enabled) 2f else 0f,
        animationSpec = spring(stiffness = Spring.StiffnessHigh),
        label = "button_press_y"
    )

    Box(
        modifier = modifier
            .height(34.dp)
            .graphicsLayer { translationY = yOffset }
            .clip(CutCornerShape(4.dp))
            .background(
                if (enabled) {
                    Brush.verticalGradient(
                        listOf(
                            SoloCard3dPlateRaised,
                            SoloCardBottomShadow
                        )
                    )
                } else {
                    Brush.verticalGradient(
                        listOf(
                            SoloSurfaceVariant.copy(alpha = 0.4f),
                            SoloObsidian
                        )
                    )
                }
            )
            .border(
                BorderStroke(
                    1.dp,
                    if (enabled) accentColor.copy(alpha = 0.65f) else SoloTextMuted.copy(alpha = 0.25f)
                ),
                CutCornerShape(4.dp)
            )
            .clickable(
                enabled = enabled,
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
            .testTag("game_btn_$label"),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            color = if (enabled) SoloTextPrimary else SoloTextMuted,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Monospace
        )
    }
}
