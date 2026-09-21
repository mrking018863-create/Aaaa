package com.example.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface PlayerStatsDao {
    @Query("SELECT * FROM player_stats WHERE id = 1")
    fun getPlayerStats(): Flow<PlayerStatsEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(stats: PlayerStatsEntity)

    @Update
    suspend fun update(stats: PlayerStatsEntity)
}

@Dao
interface DailyQuestDao {
    @Query("SELECT * FROM daily_quest WHERE date = :date")
    fun getDailyQuest(date: String): Flow<DailyQuestEntity?>

    @Query("SELECT * FROM daily_quest WHERE date = :date")
    suspend fun getDailyQuestDirect(date: String): DailyQuestEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(quest: DailyQuestEntity)
}

@Dao
interface WorkoutDao {
    @Query("SELECT * FROM workout_logs ORDER BY timestamp DESC LIMIT 50")
    fun getRecentWorkouts(): Flow<List<WorkoutLogEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun logWorkout(log: WorkoutLogEntity)
}

@Dao
interface ChatMessageDao {
    @Query("SELECT * FROM chat_messages ORDER BY timestamp ASC LIMIT 100")
    fun getAllMessages(): Flow<List<ChatMessageEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: ChatMessageEntity)

    @Query("DELETE FROM chat_messages")
    suspend fun clearMessages()
}
