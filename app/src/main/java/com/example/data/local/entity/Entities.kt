package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "devices")
data class DeviceEntity(
    @PrimaryKey val deviceId: String,
    val deviceName: String,
    val deviceType: String = "Android Phone", // Phone, Tablet, PC
    val nickname: String = "",
    val lastConnectedTime: Long = System.currentTimeMillis(),
    val isFavorite: Boolean = false,
    val isTrusted: Boolean = false,
    val connectionCode: String = ""
)

@Entity(tableName = "session_history")
data class SessionHistoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val deviceId: String,
    val deviceName: String,
    val sessionType: String, // "Host" (Share) or "Client" (Connect)
    val startTime: Long,
    val durationSeconds: Long,
    val sessionNotes: String = "",
    val endReason: String = "User Disconnected"
)
