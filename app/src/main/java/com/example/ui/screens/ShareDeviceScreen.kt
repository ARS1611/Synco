package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.R
import com.example.ui.components.QrCodeCanvas
import com.example.ui.components.StatusBadge
import com.example.ui.components.SynkoGlassCard
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
import com.example.ui.theme.SynkoWarning
import com.example.ui.theme.SynkoWarningLight
import com.example.ui.theme.SynkoWhite
import com.example.ui.viewmodel.MainViewModel

@Composable
fun ShareDeviceScreen(viewModel: MainViewModel) {
    val context = LocalContext.current
    val code by viewModel.connectionCode.collectAsState()
    val secondsLeft by viewModel.codeExpirySeconds.collectAsState()
    val liveState by viewModel.liveSessionState.collectAsState()

    var showPermissionGuide by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SynkoWhite)
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Share My Device",
            fontSize = 26.sp,
            fontWeight = FontWeight.ExtraBold,
            color = SynkoTextPrimary
        )

        Text(
            text = "Allow a trusted helper to connect using your unique code",
            fontSize = 13.sp,
            color = SynkoTextSecondary,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Hero Banner / Illustration Card
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp)
                .clip(RoundedCornerShape(22.dp))
                .background(SynkoAccentLight)
                .border(1.dp, SynkoBorder, RoundedCornerShape(22.dp)),
            contentAlignment = Alignment.Center
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    StatusBadge(
                        text = "Encrypted Connection",
                        color = SynkoSuccess,
                        backgroundColor = SynkoSuccessLight
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Voluntary & Controlled",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = SynkoTextPrimary
                    )
                    Text(
                        text = "You can stop remote access anytime with one tap.",
                        fontSize = 12.sp,
                        color = SynkoTextSecondary
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Connection Code Glass Card
        SynkoGlassCard(cornerRadius = 24.dp) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "CONNECTION CODE",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = SynkoTextSecondary,
                    letterSpacing = 1.5.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                // 9-digit Code Display e.g. "483 629 154"
                Text(
                    text = code,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Black,
                    color = SynkoPrimary,
                    letterSpacing = 3.sp
                )

                Spacer(modifier = Modifier.height(6.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Expires in ${secondsLeft / 60}:${"%02d".format(secondsLeft % 60)}",
                        fontSize = 12.sp,
                        color = SynkoWarning,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    IconButton(
                        onClick = { viewModel.refreshConnectionCode() },
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Refresh Code",
                            tint = SynkoPrimary,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Large QR Code Canvas
                QrCodeCanvas(code = code, sizeDp = 180.dp)

                Spacer(modifier = Modifier.height(20.dp))

                // Buttons: Copy Code, Share Link, Refresh Code
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    OutlinedButton(
                        onClick = {
                            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            clipboard.setPrimaryClip(ClipData.newPlainText("Synko Code", code))
                            Toast.makeText(context, "Code copied to clipboard!", Toast.LENGTH_SHORT).show()
                        },
                        shape = RoundedCornerShape(16.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, SynkoBorder)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ContentCopy,
                            contentDescription = null,
                            tint = SynkoPrimary,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Copy", fontSize = 13.sp, color = SynkoTextPrimary)
                    }

                    OutlinedButton(
                        onClick = {
                            val shareIntent = android.content.Intent().apply {
                                action = android.content.Intent.ACTION_SEND
                                putExtra(android.content.Intent.EXTRA_TEXT, "Connect to my device on Synko: synko://connect/${code.replace(" ", "")}")
                                type = "text/plain"
                            }
                            context.startActivity(android.content.Intent.createChooser(shareIntent, "Share Connection Link"))
                        },
                        shape = RoundedCornerShape(16.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, SynkoBorder)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = null,
                            tint = SynkoPrimary,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Share Link", fontSize = 13.sp, color = SynkoTextPrimary)
                    }

                    OutlinedButton(
                        onClick = { viewModel.refreshConnectionCode() },
                        shape = RoundedCornerShape(16.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, SynkoBorder)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = null,
                            tint = SynkoPrimary,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("New Code", fontSize = 13.sp, color = SynkoTextPrimary)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Start Sharing Action Card / Persistent Active Banner
        if (liveState.isActive && liveState.role == com.example.model.SessionRole.HOST) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(8.dp, RoundedCornerShape(20.dp)),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = SynkoSuccessLight),
                border = androidx.compose.foundation.BorderStroke(1.dp, SynkoSuccess)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(12.dp)
                                .clip(CircleShape)
                                .background(SynkoSuccess)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "ACTIVE SCREEN SHARING",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = SynkoSuccess
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Connected to ${liveState.connectedDeviceName}",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = SynkoTextPrimary
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = { viewModel.stopSession() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        shape = RoundedCornerShape(18.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = SynkoError)
                    ) {
                        Icon(Icons.Default.Stop, contentDescription = null, tint = Color.White)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Stop Sharing Instantly", fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }
            }
        } else {
            Button(
                onClick = {
                    showPermissionGuide = true
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(58.dp)
                    .shadow(8.dp, RoundedCornerShape(20.dp)),
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(containerColor = SynkoPrimary)
            ) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Start Sharing Screen",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))
    }

    if (showPermissionGuide) {
        Dialog(onDismissRequest = { showPermissionGuide = false }) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(16.dp, RoundedCornerShape(24.dp)),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = SynkoWhite)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Security,
                            contentDescription = null,
                            tint = SynkoPrimary,
                            modifier = Modifier.size(28.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Permission Disclosure",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = SynkoTextPrimary
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "1. Screen Capture (MediaProjection)\nRequired to capture and stream your screen in real-time to your helper.",
                        fontSize = 13.sp,
                        color = SynkoTextPrimary,
                        lineHeight = 18.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "2. Remote Assistance (Accessibility Service)\nOptionally allows your helper to tap or scroll on your device when permitted. You can revoke this anytime.",
                        fontSize = 13.sp,
                        color = SynkoTextSecondary,
                        lineHeight = 18.sp
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Row(modifier = Modifier.fillMaxWidth()) {
                        OutlinedButton(
                            onClick = { showPermissionGuide = false },
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Text("Cancel")
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Button(
                            onClick = {
                                showPermissionGuide = false
                                viewModel.startLiveSession(
                                    sessionCode = code,
                                    connectedDeviceName = "Helper Device",
                                    role = com.example.model.SessionRole.HOST
                                )
                            },
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = SynkoPrimary)
                        ) {
                            Text("Grant & Start", color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}
