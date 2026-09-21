package com.example.data.speech

import android.content.Context
import android.speech.tts.TextToSpeech
import java.util.Locale

class SystemSpeechManager(context: Context) : TextToSpeech.OnInitListener {
    private var tts: TextToSpeech? = TextToSpeech(context.applicationContext, this)
    private var isReady = false
    var isMuted = false

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = tts?.setLanguage(Locale.US)
            if (result != TextToSpeech.LANG_MISSING_DATA && result != TextToSpeech.LANG_NOT_SUPPORTED) {
                tts?.setPitch(0.92f) // Slightly deeper, authoritative system tone
                tts?.setSpeechRate(1.02f)
                isReady = true
            }
        }
    }

    fun speak(text: String) {
        if (isMuted || !isReady) return
        // Clean out brackets and tags for speech flow
        val cleanText = text
            .replace("\\[.*?\\]".toRegex(), "")
            .replace("*", "")
            .replace("#", "")
            .trim()
        if (cleanText.isNotBlank()) {
            tts?.speak(cleanText, TextToSpeech.QUEUE_FLUSH, null, "SystemVoice_${System.currentTimeMillis()}")
        }
    }

    fun stop() {
        tts?.stop()
    }

    fun shutdown() {
        tts?.stop()
        tts?.shutdown()
        tts = null
    }
}
