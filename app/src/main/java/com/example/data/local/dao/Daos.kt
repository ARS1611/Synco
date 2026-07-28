package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.local.entity.DeviceEntity
import com.example.data.local.entity.SessionHistoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DeviceDao {
    @Query("SELECT * FROM devices ORDER BY lastConnectedTime DESC")
    fun getAllDevices(): Flow<List<DeviceEntity>>

    @Query("SELECT * FROM devices WHERE isFavorite = 1 ORDER BY lastConnectedTime DESC")
    fun getFavoriteDevices(): Flow<List<DeviceEntity>>

    @Query("SELECT * FROM devices WHERE isTrusted = 1 ORDER BY lastConnectedTime DESC")
    fun getTrustedDevices(): Flow<List<DeviceEntity>>

    @Query("SELECT * FROM devices WHERE deviceId = :deviceId LIMIT 1")
    suspend fun getDeviceById(deviceId: String): DeviceEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertDevice(device: DeviceEntity)

    @Update
    suspend fun updateDevice(device: DeviceEntity)

    @Query("DELETE FROM devices WHERE deviceId = :deviceId")
    suspend fun deleteDevice(deviceId: String)
}

@Dao
interface SessionHistoryDao {
    @Query("SELECT * FROM session_history ORDER BY startTime DESC")
    fun getAllHistory(): Flow<List<SessionHistoryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHistory(history: SessionHistoryEntity)

    @Query("UPDATE session_history SET sessionNotes = :notes WHERE id = :id")
    suspend fun updateNotes(id: Long, notes: String)

    @Query("DELETE FROM session_history WHERE id = :id")
    suspend fun deleteHistory(id: Long)

    @Query("DELETE FROM session_history")
    suspend fun clearHistory()
}
