package com.example.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "player_stats")
data class PlayerStatsEntity(
    @PrimaryKey val id: Int = 1,
    val playerName: String = "Hunter",
    val title: String = "The Weakest Hunter",
    val hunterRank: String = "E-Rank", // E, D, C, B, A, S, Shadow Monarch
    val level: Int = 1,
    val currentExp: Int = 0,
    val maxExp: Int = 100,
    val hp: Int = 100,
    val maxHp: Int = 100,
    val mp: Int = 50,
    val maxMp: Int = 50,
    val fatigue: Int = 0, // 0 - 100%
    val str: Int = 10,
    val agi: Int = 10,
    val vit: Int = 10,
    val intel: Int = 10,
    val per: Int = 10,
    val statPoints: Int = 3,
    val streakDays: Int = 1,
    val lastCompletedDate: String = ""
)

@Entity(tableName = "daily_quest")
data class DailyQuestEntity(
    @PrimaryKey val date: String, // YYYY-MM-DD
    val pushupsDone: Int = 0,
    val pushupsTarget: Int = 100,
    val situpsDone: Int = 0,
    val situpsTarget: Int = 100,
    val squatsDone: Int = 0,
    val squatsTarget: Int = 100,
    val cardioDone: Int = 0, // 100 reps of Jumping Jacks / Burpees or equivalent home cardio
    val cardioTarget: Int = 100,
    val isCompleted: Boolean = false,
    val isRewardClaimed: Boolean = false,
    val penaltyTriggered: Boolean = false
)

@Entity(tableName = "workout_logs")
data class WorkoutLogEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val exerciseName: String,
    val count: Int,
    val category: String, // STRENGTH, AGILITY, VITALITY, CORE
    val expEarned: Int,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "chat_messages")
data class ChatMessageEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val sender: String, // "SYSTEM" or "HUNTER"
    val text: String,
    val timestamp: Long = System.currentTimeMillis(),
    val isSystemAlert: Boolean = false
)
