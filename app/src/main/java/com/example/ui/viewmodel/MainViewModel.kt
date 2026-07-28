package com.example.ui.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.entity.DeviceEntity
import com.example.data.local.entity.SessionHistoryEntity
import com.example.data.repository.SynkoRepository
import com.example.model.ChatMessage
import com.example.model.ConnectionQuality
import com.example.model.FileType
import com.example.model.FileTransferItem
import com.example.model.LiveSessionState
import com.example.model.SessionRole
import com.example.service.ScreenShareService
import com.example.service.SynkoAccessibilityService
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    val repository = SynkoRepository(application)

    init {
        viewModelScope.launch {
            repository.addDefaultSampleDataIfNeeded()
        }
    }

    val allDevices: StateFlow<List<DeviceEntity>> = repository.allDevices
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val sessionHistory: StateFlow<List<SessionHistoryEntity>> = repository.sessionHistory
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Share Device Screen State
    private val _connectionCode = MutableStateFlow(repository.generateRandomConnectionCode())
    val connectionCode: StateFlow<String> = _connectionCode.asStateFlow()

    private val _codeExpirySeconds = MutableStateFlow(300) // 5 minutes code expiry
    val codeExpirySeconds: StateFlow<Int> = _codeExpirySeconds.asStateFlow()

    private var codeTimerJob: Job? = null

    init {
        startCodeTimer()
    }

    private fun startCodeTimer() {
        codeTimerJob?.cancel()
        _codeExpirySeconds.value = 300
        codeTimerJob = viewModelScope.launch {
            while (_codeExpirySeconds.value > 0) {
                delay(1000)
                _codeExpirySeconds.value -= 1
            }
            refreshConnectionCode()
        }
    }

    fun refreshConnectionCode() {
        _connectionCode.value = repository.generateRandomConnectionCode()
        startCodeTimer()
    }

    // Live Remote Session State
    private val _liveSessionState = MutableStateFlow(LiveSessionState())
    val liveSessionState: StateFlow<LiveSessionState> = _liveSessionState.asStateFlow()

    // Incoming Request Dialog State
    private val _incomingRequestDeviceName = MutableStateFlow<String?>(null)
    val incomingRequestDeviceName: StateFlow<String?> = _incomingRequestDeviceName.asStateFlow()

    // Deep Link Dialog State
    private val _deepLinkCode = MutableStateFlow<String?>(null)
    val deepLinkCode: StateFlow<String?> = _deepLinkCode.asStateFlow()

    // Remote Session Chat Messages
    private val _chatMessages = MutableStateFlow<List<ChatMessage>>(
        listOf(
            ChatMessage("1", "System", "Encrypted Session Established. Media & Audio Active.", System.currentTimeMillis() - 120000, false),
            ChatMessage("2", "Remote Assistant", "Hello! I am ready to assist you. Tap screen or use chat.", System.currentTimeMillis() - 60000, false)
        )
    )
    val chatMessages: StateFlow<List<ChatMessage>> = _chatMessages.asStateFlow()

    // File Transfers in Session
    private val _fileTransfers = MutableStateFlow<List<FileTransferItem>>(
        listOf(
            FileTransferItem("f1", "diagnostic_log.txt", "1.2 MB", 1.0f, true, FileType.DOCUMENT),
            FileTransferItem("f2", "screen_capture.png", "3.8 MB", 0.75f, false, FileType.PHOTO)
        )
    )
    val fileTransfers: StateFlow<List<FileTransferItem>> = _fileTransfers.asStateFlow()

    // Session Timer Job
    private var sessionTimerJob: Job? = null
    private var sessionSeconds = 0L

    fun checkAccessibilityEnabled(context: Context): Boolean {
        return SynkoAccessibilityService.instance != null
    }

    fun handleIncomingDeepLink(code: String) {
        _deepLinkCode.value = code
    }

    fun dismissDeepLink() {
        _deepLinkCode.value = null
    }

    fun triggerIncomingRequest(deviceName: String = "John's Phone") {
        _incomingRequestDeviceName.value = deviceName
    }

    fun acceptIncomingRequest() {
        val device = _incomingRequestDeviceName.value ?: "Remote User"
        _incomingRequestDeviceName.value = null
        startLiveSession(
            sessionCode = _connectionCode.value,
            connectedDeviceName = device,
            role = SessionRole.HOST
        )
    }

    fun declineIncomingRequest() {
        _incomingRequestDeviceName.value = null
    }

    fun connectWithCode(code: String, targetDeviceName: String = "Remote Device") {
        startLiveSession(
            sessionCode = code,
            connectedDeviceName = targetDeviceName,
            role = SessionRole.CLIENT
        )
    }

    fun startLiveSession(sessionCode: String, connectedDeviceName: String, role: SessionRole) {
        val context = getApplication<Application>().applicationContext
        ScreenShareService.start(context)

        sessionSeconds = 0L
        _liveSessionState.value = LiveSessionState(
            isActive = true,
            sessionCode = sessionCode,
            connectedDeviceName = connectedDeviceName,
            connectedDeviceId = "dev_" + System.currentTimeMillis(),
            role = role,
            startTime = System.currentTimeMillis(),
            connectionQuality = ConnectionQuality.EXCELLENT,
            batteryLevel = 92,
            isWifi = true,
            accessibilityEnabled = checkAccessibilityEnabled(context),
            mediaProjectionActive = true
        )

        sessionTimerJob?.cancel()
        sessionTimerJob = viewModelScope.launch {
            while (_liveSessionState.value.isActive) {
                delay(1000)
                sessionSeconds += 1
            }
        }
    }

    fun togglePointerMode() {
        _liveSessionState.value = _liveSessionState.value.copy(
            isPointerModeEnabled = !_liveSessionState.value.isPointerModeEnabled
        )
    }

    fun toggleKeyboard() {
        _liveSessionState.value = _liveSessionState.value.copy(
            isKeyboardVisible = !_liveSessionState.value.isKeyboardVisible
        )
    }

    fun toggleVoiceMute() {
        _liveSessionState.value = _liveSessionState.value.copy(
            isVoiceMuted = !_liveSessionState.value.isVoiceMuted
        )
    }

    fun sendChatMessage(text: String) {
        if (text.isBlank()) return
        val msg = ChatMessage(
            id = System.currentTimeMillis().toString(),
            senderName = "Me",
            text = text,
            isFromMe = true
        )
        _chatMessages.value = _chatMessages.value + msg

        // Simulate remote reply after 1.5s
        viewModelScope.launch {
            delay(1500)
            if (_liveSessionState.value.isActive) {
                val reply = ChatMessage(
                    id = (System.currentTimeMillis() + 1).toString(),
                    senderName = _liveSessionState.value.connectedDeviceName,
                    text = "Received: $text",
                    isFromMe = false
                )
                _chatMessages.value = _chatMessages.value + reply
            }
        }
    }

    fun syncClipboardText(text: String) {
        _liveSessionState.value = _liveSessionState.value.copy(clipboardContent = text)
    }

    fun addFileTransfer(fileName: String, sizeStr: String, type: FileType) {
        val id = System.currentTimeMillis().toString()
        val item = FileTransferItem(id, fileName, sizeStr, 0.1f, false, type)
        _fileTransfers.value = _fileTransfers.value + item

        // Simulate progress
        viewModelScope.launch {
            var progress = 0.1f
            while (progress < 1.0f) {
                delay(500)
                progress += 0.3f
                if (progress >= 1.0f) progress = 1.0f
                _fileTransfers.value = _fileTransfers.value.map {
                    if (it.id == id) it.copy(progress = progress, isCompleted = progress >= 1.0f) else it
                }
            }
        }
    }

    fun stopSession() {
        val currentState = _liveSessionState.value
        val context = getApplication<Application>().applicationContext
        ScreenShareService.stop(context)

        sessionTimerJob?.cancel()

        if (currentState.isActive) {
            viewModelScope.launch {
                // Log history
                repository.logSessionHistory(
                    SessionHistoryEntity(
                        deviceId = currentState.connectedDeviceId,
                        deviceName = currentState.connectedDeviceName,
                        sessionType = if (currentState.role == SessionRole.HOST) "Share My Device" else "Connect Remote",
                        startTime = currentState.startTime,
                        durationSeconds = sessionSeconds,
                        sessionNotes = "Session ended gracefully by user.",
                        endReason = "User Disconnected"
                    )
                )

                // Save device
                repository.saveDevice(
                    DeviceEntity(
                        deviceId = currentState.connectedDeviceId,
                        deviceName = currentState.connectedDeviceName,
                        deviceType = "Android Device",
                        nickname = currentState.connectedDeviceName,
                        lastConnectedTime = System.currentTimeMillis(),
                        isFavorite = false,
                        isTrusted = true,
                        connectionCode = currentState.sessionCode
                    )
                )
            }
        }

        _liveSessionState.value = LiveSessionState(isActive = false)
    }

    fun toggleFavoriteDevice(device: DeviceEntity) {
        viewModelScope.launch {
            repository.toggleFavoriteDevice(device)
        }
    }

    fun toggleTrustedDevice(device: DeviceEntity) {
        viewModelScope.launch {
            repository.toggleTrustedDevice(device)
        }
    }

    fun deleteDevice(deviceId: String) {
        viewModelScope.launch {
            repository.deleteDevice(deviceId)
        }
    }

    fun deleteHistoryItem(id: Long) {
        viewModelScope.launch {
            repository.deleteHistory(id)
        }
    }

    fun saveDevice(device: DeviceEntity) {
        viewModelScope.launch {
            repository.saveDevice(device)
        }
    }

    fun updateHistoryNotes(id: Long, notes: String) {
        viewModelScope.launch {
            repository.updateSessionNotes(id, notes)
        }
    }
}
