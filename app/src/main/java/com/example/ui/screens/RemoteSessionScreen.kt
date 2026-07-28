package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.BatteryChargingFull
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CallEnd
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.ContentPaste
import androidx.compose.material.icons.filled.FolderOpen
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Keyboard
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MicOff
import androidx.compose.material.icons.filled.Mouse
import androidx.compose.material.icons.filled.PowerSettingsNew
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.SignalCellularAlt
import androidx.compose.material.icons.filled.Smartphone
import androidx.compose.material.icons.filled.Task
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.FileType
import com.example.ui.components.StatusBadge
import com.example.ui.theme.SynkoAccent
import com.example.ui.theme.SynkoAccentLight
import com.example.ui.theme.SynkoBorder
import com.example.ui.theme.SynkoError
import com.example.ui.theme.SynkoPrimary
import com.example.ui.theme.SynkoSuccess
import com.example.ui.theme.SynkoSuccessLight
import com.example.ui.theme.SynkoSurface
import com.example.ui.theme.SynkoTextPrimary
import com.example.ui.theme.SynkoTextSecondary
import com.example.ui.theme.SynkoWhite
import com.example.ui.viewmodel.MainViewModel

@Composable
fun RemoteSessionScreen(viewModel: MainViewModel) {
    val context = LocalContext.current
    val liveState by viewModel.liveSessionState.collectAsState()
    val chatMessages by viewModel.chatMessages.collectAsState()
    val fileTransfers by viewModel.fileTransfers.collectAsState()

    var showChatSheet by remember { mutableStateOf(false) }
    var showFilesSheet by remember { mutableStateOf(false) }
    var chatInputText by remember { mutableStateOf("") }

    val pointerTouches = remember { mutableStateListOf<Offset>() }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF020617))
    ) {
        // LIVE REMOTE STREAM CANVAS
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 70.dp, bottom = 90.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(Color(0xFF0F172A))
                .pointerInput(Unit) {
                    detectTapGestures { offset ->
                        pointerTouches.add(offset)
                        Toast
                            .makeText(context, "Pointer tap registered at (${offset.x.toInt()}, ${offset.y.toInt()})", Toast.LENGTH_SHORT)
                            .show()
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val w = size.width
                val h = size.height

                // Simulated Mobile / Tablet Screen UI Canvas
                drawRect(Color(0xFF090D16))

                // Status bar mockup
                drawRect(Color(0x22FFFFFF), topLeft = Offset(0f, 0f), size = androidx.compose.ui.geometry.Size(w, 40f))

                // App mock content
                drawRect(Color(0xFF2563EB).copy(alpha = 0.15f), topLeft = Offset(w * 0.1f, h * 0.15f), size = androidx.compose.ui.geometry.Size(w * 0.8f, h * 0.2f))
                drawRect(Color(0xFF3B82F6).copy(alpha = 0.25f), topLeft = Offset(w * 0.1f, h * 0.4f), size = androidx.compose.ui.geometry.Size(w * 0.8f, h * 0.35f))

                // Draw touch ripples
                for (pt in pointerTouches) {
                    drawCircle(
                        color = Color(0xFF3B82F6).copy(alpha = 0.6f),
                        radius = 28f,
                        center = pt
                    )
                    drawCircle(
                        color = Color.White,
                        radius = 12f,
                        center = pt
                    )
                }
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Smartphone,
                    contentDescription = null,
                    tint = Color.White.copy(alpha = 0.3f),
                    modifier = Modifier.size(64.dp)
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Live Stream Active (${liveState.connectionQuality.label})",
                    color = Color.White.copy(alpha = 0.8f),
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Tap anywhere on screen to send remote gestures",
                    color = Color.White.copy(alpha = 0.5f),
                    fontSize = 12.sp
                )
            }
        }

        // TOP FLOATING BAR
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, start = 16.dp, end = 16.dp)
                .shadow(12.dp, RoundedCornerShape(20.dp)),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = SynkoWhite)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    StatusBadge(
                        text = "Connected",
                        color = SynkoSuccess,
                        backgroundColor = SynkoSuccessLight
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = liveState.connectedDeviceName,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = SynkoTextPrimary
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "${liveState.connectionQuality.pingMs}ms | ${liveState.connectionQuality.fps}fps",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = SynkoPrimary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        imageVector = if (liveState.isWifi) Icons.Default.Wifi else Icons.Default.SignalCellularAlt,
                        contentDescription = null,
                        tint = SynkoTextSecondary,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Icon(
                        imageVector = Icons.Default.BatteryChargingFull,
                        contentDescription = null,
                        tint = SynkoSuccess,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }

        // BOTTOM FLOATING TOOLBAR
        Card(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(bottom = 16.dp, start = 12.dp, end = 12.dp)
                .shadow(16.dp, RoundedCornerShape(24.dp)),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = SynkoWhite)
        ) {
            Column(modifier = Modifier.padding(10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { Toast.makeText(context, "Nav: Back sent", Toast.LENGTH_SHORT).show() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = SynkoTextPrimary)
                    }

                    IconButton(onClick = { Toast.makeText(context, "Nav: Home sent", Toast.LENGTH_SHORT).show() }) {
                        Icon(Icons.Default.Home, contentDescription = "Home", tint = SynkoTextPrimary)
                    }

                    IconButton(onClick = { Toast.makeText(context, "Nav: Recents sent", Toast.LENGTH_SHORT).show() }) {
                        Icon(Icons.Default.Task, contentDescription = "Recents", tint = SynkoTextPrimary)
                    }

                    IconButton(onClick = { viewModel.toggleKeyboard() }) {
                        Icon(
                            Icons.Default.Keyboard,
                            contentDescription = "Keyboard",
                            tint = if (liveState.isKeyboardVisible) SynkoPrimary else SynkoTextSecondary
                        )
                    }

                    IconButton(onClick = { viewModel.togglePointerMode() }) {
                        Icon(
                            Icons.Default.Mouse,
                            contentDescription = "Pointer Mode",
                            tint = if (liveState.isPointerModeEnabled) SynkoPrimary else SynkoTextSecondary
                        )
                    }

                    IconButton(onClick = { showChatSheet = true }) {
                        Icon(Icons.Default.Chat, contentDescription = "Chat", tint = SynkoPrimary)
                    }

                    IconButton(onClick = { showFilesSheet = true }) {
                        Icon(Icons.Default.FolderOpen, contentDescription = "Files", tint = SynkoAccent)
                    }

                    IconButton(onClick = { viewModel.toggleVoiceMute() }) {
                        Icon(
                            imageVector = if (liveState.isVoiceMuted) Icons.Default.MicOff else Icons.Default.Mic,
                            contentDescription = "Voice Call",
                            tint = if (!liveState.isVoiceMuted) SynkoSuccess else SynkoTextSecondary
                        )
                    }

                    IconButton(
                        onClick = { viewModel.stopSession() },
                        modifier = Modifier
                            .size(40.dp)
                            .background(SynkoError, CircleShape)
                    ) {
                        Icon(Icons.Default.PowerSettingsNew, contentDescription = "Disconnect", tint = Color.White)
                    }
                }
            }
        }

        // CHAT BOTTOM SHEET / OVERLAY
        if (showChatSheet) {
            Card(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .height(380.dp)
                    .padding(12.dp)
                    .shadow(16.dp, RoundedCornerShape(24.dp)),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = SynkoWhite)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Live Session Chat",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = SynkoTextPrimary
                        )
                        IconButton(onClick = { showChatSheet = false }) {
                            Icon(Icons.Default.Close, contentDescription = "Close", tint = SynkoTextSecondary)
                        }
                    }

                    LazyColumn(
                        modifier = Modifier
                            .weight(1f)
                            .padding(vertical = 8.dp)
                    ) {
                        items(chatMessages) { msg ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                horizontalArrangement = if (msg.isFromMe) Arrangement.End else Arrangement.Start
                            ) {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(16.dp))
                                        .background(if (msg.isFromMe) SynkoPrimary else SynkoSurface)
                                        .padding(horizontal = 14.dp, vertical = 8.dp)
                                ) {
                                    Text(
                                        text = msg.text,
                                        color = if (msg.isFromMe) Color.White else SynkoTextPrimary,
                                        fontSize = 13.sp
                                    )
                                }
                            }
                        }
                    }

                    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                        OutlinedTextField(
                            value = chatInputText,
                            onValueChange = { chatInputText = it },
                            placeholder = { Text("Type message...") },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        IconButton(
                            onClick = {
                                viewModel.sendChatMessage(chatInputText)
                                chatInputText = ""
                            },
                            modifier = Modifier
                                .size(48.dp)
                                .background(SynkoPrimary, CircleShape)
                        ) {
                            Icon(Icons.Default.Send, contentDescription = "Send", tint = Color.White)
                        }
                    }
                }
            }
        }

        // FILE TRANSFER BOTTOM SHEET / OVERLAY
        if (showFilesSheet) {
            Card(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .height(360.dp)
                    .padding(12.dp)
                    .shadow(16.dp, RoundedCornerShape(24.dp)),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = SynkoWhite)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "File & Photo Transfer",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = SynkoTextPrimary
                        )
                        IconButton(onClick = { showFilesSheet = false }) {
                            Icon(Icons.Default.Close, contentDescription = "Close", tint = SynkoTextSecondary)
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Button(
                            onClick = { viewModel.addFileTransfer("photo_spec.jpg", "2.4 MB", FileType.PHOTO) },
                            shape = RoundedCornerShape(14.dp)
                        ) {
                            Text("Send Photo")
                        }
                        Button(
                            onClick = { viewModel.addFileTransfer("screen_record.mp4", "14.2 MB", FileType.VIDEO) },
                            shape = RoundedCornerShape(14.dp)
                        ) {
                            Text("Send Video")
                        }
                        Button(
                            onClick = { viewModel.addFileTransfer("manual.pdf", "850 KB", FileType.DOCUMENT) },
                            shape = RoundedCornerShape(14.dp)
                        ) {
                            Text("Send Doc")
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    LazyColumn(modifier = Modifier.weight(1f)) {
                        items(fileTransfers) { file ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                colors = CardDefaults.cardColors(containerColor = SynkoSurface)
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(file.fileName, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                        Text(file.fileSizeFormatted, fontSize = 12.sp, color = SynkoTextSecondary)
                                    }
                                    Spacer(modifier = Modifier.height(6.dp))
                                    LinearProgressIndicator(
                                        progress = { file.progress },
                                        modifier = Modifier.fillMaxWidth(),
                                        color = SynkoPrimary
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
