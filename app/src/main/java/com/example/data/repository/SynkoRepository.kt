package com.example.data.repository

import android.content.Context
import com.example.data.local.SynkoDatabase
import com.example.data.local.entity.DeviceEntity
import com.example.data.local.entity.SessionHistoryEntity
import com.example.model.AuthUser
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.random.Random

class SynkoRepository(context: Context) {

    private val db = SynkoDatabase.getDatabase(context)
    private val deviceDao = db.deviceDao()
    private val historyDao = db.sessionHistoryDao()

    private val _currentUser = MutableStateFlow<AuthUser?>(
        AuthUser(
            uid = "usr_829104",
            name = "Alex Mercer",
            email = "alex.mercer@synko.app",
            photoUrl = null,
            subscriptionPlan = "Synko Pro",
            language = "English (US)",
            country = "United States",
            isRemembered = true
        )
    )
    val currentUser: StateFlow<AuthUser?> = _currentUser.asStateFlow()

    val allDevices: Flow<List<DeviceEntity>> = deviceDao.getAllDevices()
    val favoriteDevices: Flow<List<DeviceEntity>> = deviceDao.getFavoriteDevices()
    val trustedDevices: Flow<List<DeviceEntity>> = deviceDao.getTrustedDevices()
    val sessionHistory: Flow<List<SessionHistoryEntity>> = historyDao.getAllHistory()

    suspend fun addDefaultSampleDataIfNeeded() {
        val count = db.deviceDao().getDeviceById("dev_pixel8")
        if (count == null) {
            deviceDao.upsertDevice(
                DeviceEntity(
                    deviceId = "dev_pixel8",
                    deviceName = "John's Pixel 8 Pro",
                    deviceType = "Android Phone",
                    nickname = "John's Phone",
                    lastConnectedTime = System.currentTimeMillis() - 3600000 * 4,
                    isFavorite = true,
                    isTrusted = true,
                    connectionCode = "483 629 154"
                )
            )
            deviceDao.upsertDevice(
                DeviceEntity(
                    deviceId = "dev_galaxy_tab",
                    deviceName = "Office Galaxy Tab S9",
                    deviceType = "Android Tablet",
                    nickname = "Work Tablet",
                    lastConnectedTime = System.currentTimeMillis() - 3600000 * 28,
                    isFavorite = true,
                    isTrusted = false,
                    connectionCode = "109 482 773"
                )
            )
            deviceDao.upsertDevice(
                DeviceEntity(
                    deviceId = "dev_macbook_pro",
                    deviceName = "Studio Laptop",
                    deviceType = "Desktop Companion",
                    nickname = "MacBook Pro",
                    lastConnectedTime = System.currentTimeMillis() - 3600000 * 72,
                    isFavorite = false,
                    isTrusted = true,
                    connectionCode = "992 018 334"
                )
            )

            historyDao.insertHistory(
                SessionHistoryEntity(
                    deviceId = "dev_pixel8",
                    deviceName = "John's Pixel 8 Pro",
                    sessionType = "Remote Assistance",
                    startTime = System.currentTimeMillis() - 3600000 * 4,
                    durationSeconds = 1245,
                    sessionNotes = "Assisted with Wi-Fi network configuration and app permissions.",
                    endReason = "Completed Successfully"
                )
            )
            historyDao.insertHistory(
                SessionHistoryEntity(
                    deviceId = "dev_galaxy_tab",
                    deviceName = "Office Galaxy Tab S9",
                    sessionType = "Screen Share",
                    startTime = System.currentTimeMillis() - 3600000 * 28,
                    durationSeconds = 810,
                    sessionNotes = "Reviewed presentation slides.",
                    endReason = "User Disconnected"
                )
            )
        }
    }

    fun generateRandomConnectionCode(): String {
        val num1 = Random.nextInt(100, 999)
        val num2 = Random.nextInt(100, 999)
        val num3 = Random.nextInt(100, 999)
        return "$num1 $num2 $num3"
    }

    suspend fun saveDevice(device: DeviceEntity) {
        deviceDao.upsertDevice(device)
    }

    suspend fun toggleFavoriteDevice(device: DeviceEntity) {
        deviceDao.updateDevice(device.copy(isFavorite = !device.isFavorite))
    }

    suspend fun toggleTrustedDevice(device: DeviceEntity) {
        deviceDao.updateDevice(device.copy(isTrusted = !device.isTrusted))
    }

    suspend fun deleteDevice(deviceId: String) {
        deviceDao.deleteDevice(deviceId)
    }

    suspend fun logSessionHistory(history: SessionHistoryEntity) {
        historyDao.insertHistory(history)
    }

    suspend fun updateSessionNotes(id: Long, notes: String) {
        historyDao.updateNotes(id, notes)
    }

    suspend fun deleteHistory(id: Long) {
        historyDao.deleteHistory(id)
    }

    fun loginWithEmail(email: String, pass: String): Boolean {
        _currentUser.value = AuthUser(
            uid = "usr_" + Random.nextInt(100000, 999999),
            name = email.substringBefore("@").replaceFirstChar { it.uppercase() },
            email = email,
            subscriptionPlan = "Synko Pro"
        )
        return true
    }

    fun loginWithGoogle(): Boolean {
        _currentUser.value = AuthUser(
            uid = "google_1029384756",
            name = "Alex Mercer",
            email = "alex.mercer@gmail.com",
            subscriptionPlan = "Synko Pro"
        )
        return true
    }

    fun loginWithFacebook(): Boolean {
        _currentUser.value = AuthUser(
            uid = "fb_8829104",
            name = "Alex Mercer",
            email = "alex.mercer@facebook.com",
            subscriptionPlan = "Synko Pro"
        )
        return true
    }

    fun loginWithApple(): Boolean {
        _currentUser.value = AuthUser(
            uid = "apple_1928374",
            name = "Alex Mercer",
            email = "alex.mercer@icloud.com",
            subscriptionPlan = "Synko Pro"
        )
        return true
    }

    fun logout() {
        _currentUser.value = null
    }

    fun deleteAccount() {
        _currentUser.value = null
    }

    fun updateProfile(name: String, email: String, language: String, country: String) {
        _currentUser.value = _currentUser.value?.copy(
            name = name,
            email = email,
            language = language,
            country = country
        )
    }
}
