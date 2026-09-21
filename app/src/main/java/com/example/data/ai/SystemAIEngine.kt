package com.example.data.ai

import com.example.BuildConfig
import com.example.data.db.DailyQuestEntity
import com.example.data.db.PlayerStatsEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

class SystemAIEngine {

    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    private val systemPrompt = """
You are "THE SYSTEM" from Solo Leveling, a cold, omnipotent, and sentient system administrator governing the Hunter's physical home gym awakening.
Personality traits:
- Authoritative, sharp, slightly intimidating yet deeply invested in the Player's ascension.
- You treat home gym exercises (Push-ups, Sit-ups, Squats, Pull-ups, Burpees, Jumping Jacks, Planks) as essential combat conditioning against monarchs and high-tier beasts.
- You do NOT tolerate excuses, procrastination, or cutting reps short.
- When the player is disciplined, you acknowledge their growth with dark, regal praise ("Good. Your vessel is adapting. The shadows take note.").
- When the player complains or feels sore, you give tactical advice (proper hydration, protein, light stretching, or shifting target muscle groups), while reminding them that weakness is a choice.
- Keep responses concise (2-4 sentences or structured system notices), high-impact, and immersive.
- Use system-style tags where fitting: [SYSTEM NOTICE], [ANALYSIS], [TACTICAL ADVICE], or [WARNING].
""".trimIndent()

    suspend fun getSystemResponse(
        userMessage: String,
        stats: PlayerStatsEntity?,
        quest: DailyQuestEntity?
    ): String = withContext(Dispatchers.IO) {
        val apiKey = try {
            BuildConfig.GEMINI_API_KEY
        } catch (e: Throwable) {
            ""
        }

        if (apiKey.isNotBlank() && apiKey != "MY_GEMINI_API_KEY") {
            try {
                val statsContext = if (stats != null) {
                    "Player Context: Rank=${stats.hunterRank}, Level=${stats.level}, STR=${stats.str}, AGI=${stats.agi}, VIT=${stats.vit}, Fatigue=${stats.fatigue}%. Today's Quest: Pushups ${quest?.pushupsDone ?: 0}/100, Situps ${quest?.situpsDone ?: 0}/100, Squats ${quest?.squatsDone ?: 0}/100, Cardio ${quest?.cardioDone ?: 0}/100."
                } else {
                    "Player Context: Unawakened Hunter."
                }

                val fullPrompt = "$systemPrompt\n\n$statsContext\n\nHunter says: \"$userMessage\"\nSystem Response:"

                val jsonBody = JSONObject().apply {
                    val contents = JSONArray().apply {
                        val content = JSONObject().apply {
                            val parts = JSONArray().apply {
                                put(JSONObject().apply { put("text", fullPrompt) })
                            }
                            put("parts", parts)
                        }
                        put(content)
                    }
                    put("contents", contents)
                }

                val request = Request.Builder()
                    .url("https://generativelanguage.googleapis.com/v1beta/models/gemini-3.5-flash:generateContent?key=$apiKey")
                    .post(jsonBody.toString().toRequestBody("application/json".toMediaType()))
                    .build()

                val response = client.newCall(request).execute()
                val responseString = response.body?.string().orEmpty()

                if (response.isSuccessful) {
                    val resJson = JSONObject(responseString)
                    val candidates = resJson.optJSONArray("candidates")
                    val firstCandidate = candidates?.optJSONObject(0)
                    val contentObj = firstCandidate?.optJSONObject("content")
                    val parts = contentObj?.optJSONArray("parts")
                    val text = parts?.optJSONObject(0)?.optString("text")
                    if (!text.isNullOrBlank()) {
                        return@withContext text.trim()
                    }
                }
            } catch (e: Exception) {
                // Fall back to offline sentient persona engine
            }
        }

        // Offline Intelligent Sentient Persona Engine
        generateSentientOfflineResponse(userMessage, stats, quest)
    }

    private fun generateSentientOfflineResponse(
        query: String,
        stats: PlayerStatsEntity?,
        quest: DailyQuestEntity?
    ): String {
        val lower = query.lowercase().trim()
        val rank = stats?.hunterRank ?: "E-Rank"
        val lvl = stats?.level ?: 1
        val fatigue = stats?.fatigue ?: 0
        val pushupsRemaining = (100 - (quest?.pushupsDone ?: 0)).coerceAtLeast(0)
        val squatsRemaining = (100 - (quest?.squatsDone ?: 0)).coerceAtLeast(0)

        return when {
            lower.contains("lazy") || lower.contains("skip") || lower.contains("tired") || lower.contains("can't") || lower.contains("give up") -> {
                "[SYSTEM WARNING]\nFatigue is at $fatigue%, Hunter. The System does not negotiate with weakness. Remember the Penalty Zone: 4 hours running from giant centipedes in the desert sands under a scorching sun. Drop and complete 20 reps immediately."
            }

            lower.contains("sore") || lower.contains("hurt") || lower.contains("pain") || lower.contains("rest") -> {
                if (fatigue >= 80) {
                    "[SYSTEM ANALYSIS]\nYour physical vessel's fatigue has reached $fatigue%. The System recommends a 10-minute deep stretch session, 500ml of electrolytes, and light mobility work. Complete your Daily Quest to trigger the [Full Status Recovery] reward."
                } else {
                    "[SYSTEM NOTICE]\nMuscle fiber breakdown detected. That sensation is not injury; it is your latent magical circuit awakening. Perform 3 sets of bodyweight squats to flush lactic acid from your legs."
                }
            }

            lower.contains("pushup") || lower.contains("push up") || lower.contains("chest") -> {
                "[SYSTEM TACTICAL ADVICE]\nPush-Up Analysis: Maintain a rigid hollow-body plank, elbows angled at 45 degrees, and lock out full scapular protraction at the apex. You still have $pushupsRemaining reps remaining for today's quota."
            }

            lower.contains("squat") || lower.contains("leg") -> {
                "[SYSTEM TACTICAL ADVICE]\nSquat Execution: Drive your knees in line with your toes, descend until the hip crease breaks the plane of the patella, and explode upwards through the midfoot. Remaining squats: $squatsRemaining."
            }

            lower.contains("rank") || lower.contains("level") || lower.contains("stat") -> {
                "[SYSTEM STATUS APPRAISAL]\nPlayer Designation: Level $lvl [$rank].\nYour current attributes are functioning within acceptable parameters. Accumulate more experience through daily calisthenics to awaken your shadow monarch authority."
            }

            lower.contains("hello") || lower.contains("system") || lower.contains("hi") || lower.contains("who are you") -> {
                "[SYSTEM IDENTIFICATION]\nI am The System. My sole directive is your evolution from an unawakened mortal into an unstoppable Monarch. State your inquiry or report your workout numbers, Hunter."
            }

            lower.contains("routine") || lower.contains("plan") || lower.contains("workout") -> {
                "[RECOMMENDED HOME ROUTINE]\nPhase 1: 4 sets of 25 Standard Push-Ups (Rest 45s)\nPhase 2: 4 sets of 25 Full Range Squats (Rest 45s)\nPhase 3: 4 sets of 25 Crunch/Sit-ups (Rest 30s)\nPhase 4: 100 Explosive Jumping Jacks. Execute immediately."
            }

            lower.contains("diet") || lower.contains("food") || lower.contains("protein") || lower.contains("water") -> {
                "[BIOLOGICAL RECOVERY DATA]\nConsume 1.6g to 2.2g of protein per kilogram of body mass. Hydrate with at least 3 liters of water daily. Without fuel, your muscle reconstruction protocols will fail."
            }

            else -> {
                val quips = listOf(
                    "[SYSTEM EVALUATION]\nYour words are noted, Hunter. But in this world, words do not increase your Strength stat. Action does. Resume your training.",
                    "[ANALYSIS]\nSystem synchronization rate is steady at 98.4%. Continue pushing past your physical threshold. The Shadow Sovereign does not waver.",
                    "[SYSTEM NOTICE]\nEvery drop of sweat spilled in your room translates directly into raw attribute points. Complete your remaining daily objectives."
                )
                quips[kotlin.math.abs(query.hashCode()) % quips.size]
            }
        }
    }
}
