package com.example.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        PlayerStatsEntity::class,
        DailyQuestEntity::class,
        WorkoutLogEntity::class,
        ChatMessageEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun playerStatsDao(): PlayerStatsDao
    abstract fun dailyQuestDao(): DailyQuestDao
    abstract fun workoutDao(): WorkoutDao
    abstract fun chatMessageDao(): ChatMessageDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "solo_leveling_gym.db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
