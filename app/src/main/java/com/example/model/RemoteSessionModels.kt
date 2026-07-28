package com.example.model

enum class ConnectionQuality(val label: String, val pingMs: Int, val fps: Int) {
    EXCELLENT("Excellent", 18, 60),
    GOOD("Good", 38, 55),
    FAIR("Fair", 82, 30),
    POOR("Poor", 195, 15)
}

enum class SessionRole {
    HOST,   // Sharing device screen
    CLIENT  // Remotely viewing/controlling
}

data class ChatMessage(
    val id: String,
    val senderName: String,
    val text: String,
    val timestamp: Long = System.currentTimeMillis(),
    val isFromMe: Boolean
)

data class FileTransferItem(
    val id: String,
    val fileName: String,
    val fileSizeFormatted: String,
    val progress: Float, // 0.0 to 1.0
    val isCompleted: Boolean,
    val fileType: FileType // PHOTO, VIDEO, DOCUMENT, OTHER
)

enum class FileType {
    PHOTO, VIDEO, DOCUMENT, OTHER
}

data class RemoteControlCommand(
    val actionType: String, // TAP, SWIPE, BACK, HOME, RECENTS, KEY_TEXT
    val xRatio: Float = 0f,
    val yRatio: Float = 0f,
    val textInput: String = ""
)

data class LiveSessionState(
    val isActive: Boolean = false,
    val sessionCode: String = "",
    val connectedDeviceName: String = "",
    val connectedDeviceId: String = "",
    val role: SessionRole = SessionRole.HOST,
    val startTime: Long = 0L,
    val connectionQuality: ConnectionQuality = ConnectionQuality.EXCELLENT,
    val batteryLevel: Int = 88,
    val isWifi: Boolean = true,
    val isVoiceMuted: Boolean = true,
    val isVoiceActive: Boolean = false,
    val isPointerModeEnabled: Boolean = false,
    val isKeyboardVisible: Boolean = false,
    val accessibilityEnabled: Boolean = false,
    val mediaProjectionActive: Boolean = false,
    val clipboardContent: String = "",
    val unreadChatCount: Int = 0
)
