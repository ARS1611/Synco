package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.local.dao.DeviceDao
import com.example.data.local.dao.SessionHistoryDao
import com.example.data.local.entity.DeviceEntity
import com.example.data.local.entity.SessionHistoryEntity

@Database(
    entities = [DeviceEntity::class, SessionHistoryEntity::class],
    version = 1,
    exportSchema = false
)
abstract class SynkoDatabase : RoomDatabase() {
    abstract fun deviceDao(): DeviceDao
    abstract fun sessionHistoryDao(): SessionHistoryDao

    companion object {
        @Volatile
        private var INSTANCE: SynkoDatabase? = null

        fun getDatabase(context: Context): SynkoDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SynkoDatabase::class.java,
                    "synko_database"
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
