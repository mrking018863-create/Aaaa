package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.automirrored.filled.VolumeMute
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.example.data.db.ChatMessageEntity
import com.example.ui.components.SoloHoloCard
import com.example.ui.theme.SoloBackground
import com.example.ui.theme.SoloCyanGlow
import com.example.ui.theme.SoloElectricBlue
import com.example.ui.theme.SoloHoloWindowBg
import com.example.ui.theme.SoloMonarchPurple
import com.example.ui.theme.SoloObsidian
import com.example.ui.theme.SoloPenaltyRed
import com.example.ui.theme.SoloRankGold
import com.example.ui.theme.SoloSurface
import com.example.ui.theme.SoloSurfaceVariant
import com.example.ui.theme.SoloTextMuted
import com.example.ui.theme.SoloTextPrimary
import com.example.ui.theme.SoloTextSecondary
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun SystemChatScreen(
    messages: List<ChatMessageEntity>,
    isGenerating: Boolean,
    isVoiceMuted: Boolean,
    onSendMessage: (String) -> Unit,
    onToggleVoice: () -> Unit,
    onSpeakMessage: (String) -> Unit
) {
    var inputText by remember { mutableStateOf("") }
    val listState = rememberLazyListState()

    LaunchedEffect(messages.size, isGenerating) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SoloObsidian)
            .imePadding()
    ) {
        // System Voice & Status Bar
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(SoloSurface.copy(alpha = 0.9f))
                .border(BorderStroke(1.dp, SoloElectricBlue.copy(alpha = 0.3f)))
                .padding(horizontal = 16.dp, vertical = 10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .background(SoloElectricBlue, RoundedCornerShape(5.dp))
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = "THE SYSTEM [ADMINISTRATOR]",
                            color = SoloElectricBlue,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace,
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = "Sentient AI • Home Gym Oversight",
                            color = SoloTextSecondary,
                            fontSize = 10.sp,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onToggleVoice) {
                        Icon(
                            imageVector = if (isVoiceMuted) Icons.AutoMirrored.Filled.VolumeMute else Icons.AutoMirrored.Filled.VolumeUp,
                            contentDescription = "Toggle System Voice",
                            tint = if (isVoiceMuted) SoloTextMuted else SoloElectricBlue
                        )
                    }
                }
            }
        }

        // Quick Suggestion Prompts
        val scrollState = rememberScrollState()
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(scrollState)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            QuickPromptChip("🔥 Reality check: motivate me") { onSendMessage("System, give me a harsh reality check and motivate me to train.") }
            QuickPromptChip("💪 Push-up form critique") { onSendMessage("How do I maintain perfect form during my daily 100 push-ups?") }
            QuickPromptChip("⚡ Sore muscles cure") { onSendMessage("My chest and legs are extremely sore. Should I train or rest?") }
            QuickPromptChip("📋 Recommend home routine") { onSendMessage("Recommend a calisthenics workout split for my home gym training.") }
        }

        // Chat Message List
        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(top = 8.dp, bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(messages) { message ->
                ChatMessageItem(
                    message = message,
                    onSpeak = { onSpeakMessage(message.text) }
                )
            }

            if (isGenerating) {
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(16.dp),
                            color = SoloElectricBlue,
                            strokeWidth = 2.dp
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "The System is calculating response...",
                            color = SoloElectricBlue,
                            fontSize = 12.sp,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                }
            }
        }

        // Input Field
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(SoloSurface)
                .border(BorderStroke(1.dp, SoloSurfaceVariant))
                .padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = inputText,
                    onValueChange = { inputText = it },
                    placeholder = {
                        Text(
                            text = "Inquire with The System...",
                            color = SoloTextMuted,
                            fontSize = 12.sp,
                            fontFamily = FontFamily.Monospace
                        )
                    },
                    modifier = Modifier.weight(1f),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = SoloTextPrimary,
                        unfocusedTextColor = SoloTextPrimary,
                        focusedBorderColor = SoloElectricBlue,
                        unfocusedBorderColor = SoloSurfaceVariant,
                        focusedContainerColor = SoloObsidian,
                        unfocusedContainerColor = SoloObsidian
                    ),
                    shape = CutCornerShape(4.dp),
                    singleLine = false,
                    maxLines = 3
                )
                Spacer(modifier = Modifier.width(8.dp))
                IconButton(
                    onClick = {
                        if (inputText.isNotBlank() && !isGenerating) {
                            onSendMessage(inputText)
                            inputText = ""
                        }
                    },
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CutCornerShape(4.dp))
                        .background(if (inputText.isNotBlank() && !isGenerating) SoloElectricBlue else SoloSurfaceVariant)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Send,
                        contentDescription = "Send",
                        tint = if (inputText.isNotBlank() && !isGenerating) SoloObsidian else SoloTextMuted
                    )
                }
            }
        }
    }
}

@Composable
fun QuickPromptChip(label: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .clip(CutCornerShape(4.dp))
            .background(SoloSurfaceVariant.copy(alpha = 0.8f))
            .border(BorderStroke(1.dp, SoloElectricBlue.copy(alpha = 0.4f)), CutCornerShape(4.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 10.dp, vertical = 6.dp)
    ) {
        Text(
            text = label,
            color = SoloCyanGlow,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            fontFamily = FontFamily.Monospace
        )
    }
}

@Composable
fun ChatMessageItem(
    message: ChatMessageEntity,
    onSpeak: () -> Unit
) {
    val isSystem = message.sender == "SYSTEM"
    val time = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date(message.timestamp))

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = if (isSystem) Alignment.Start else Alignment.End
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 4.dp)
        ) {
            Text(
                text = if (isSystem) "[THE SYSTEM]" else "[HUNTER]",
                color = if (isSystem) SoloElectricBlue else SoloMonarchPurple,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = time,
                color = SoloTextMuted,
                fontSize = 10.sp,
                fontFamily = FontFamily.Monospace
            )
            if (isSystem) {
                Spacer(modifier = Modifier.width(6.dp))
                Icon(
                    imageVector = Icons.Default.GraphicEq,
                    contentDescription = "Speak",
                    tint = SoloElectricBlue.copy(alpha = 0.7f),
                    modifier = Modifier
                        .size(14.dp)
                        .clickable(onClick = onSpeak)
                )
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .clip(CutCornerShape(topStart = 8.dp, bottomEnd = 8.dp))
                .background(if (isSystem) SoloHoloWindowBg else SoloSurfaceVariant)
                .border(
                    BorderStroke(
                        1.dp,
                        if (message.isSystemAlert) SoloRankGold
                        else if (isSystem) SoloElectricBlue.copy(alpha = 0.7f)
                        else SoloMonarchPurple.copy(alpha = 0.5f)
                    ),
                    CutCornerShape(topStart = 8.dp, bottomEnd = 8.dp)
                )
                .padding(12.dp)
        ) {
            Text(
                text = message.text,
                color = SoloTextPrimary,
                fontSize = 13.sp,
                lineHeight = 19.sp,
                fontFamily = FontFamily.Monospace
            )
        }
    }
}
