package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.local.entity.DeviceEntity
import com.example.ui.components.StatusBadge
import com.example.ui.components.SynkoGlassCard
import com.example.ui.theme.SynkoAccentLight
import com.example.ui.theme.SynkoBorder
import com.example.ui.theme.SynkoError
import com.example.ui.theme.SynkoErrorLight
import com.example.ui.theme.SynkoPrimary
import com.example.ui.theme.SynkoSuccess
import com.example.ui.theme.SynkoSuccessLight
import com.example.ui.theme.SynkoSurface
import com.example.ui.theme.SynkoTextPrimary
import com.example.ui.theme.SynkoTextSecondary
import com.example.ui.theme.SynkoWhite
import com.example.ui.viewmodel.MainViewModel

@Composable
fun DevicesScreen(viewModel: MainViewModel) {
    val context = LocalContext.current
    val devices by viewModel.allDevices.collectAsState()

    var editingDevice by remember { mutableStateOf<DeviceEntity?>(null) }
    var editNicknameText by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SynkoWhite)
            .padding(horizontal = 20.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(SynkoAccentLight, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.PhoneAndroid,
                    contentDescription = null,
                    tint = SynkoPrimary,
                    modifier = Modifier.size(26.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = "Saved Devices",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = SynkoTextPrimary
                )
                Text(
                    text = "Trusted and favorite devices for fast reconnect",
                    fontSize = 13.sp,
                    color = SynkoTextSecondary
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        if (devices.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "No saved devices.", color = SynkoTextSecondary)
            }
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(devices) { device ->
                    SynkoGlassCard(
                        modifier = Modifier.padding(bottom = 14.dp),
                        cornerRadius = 20.dp
                    ) {
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(48.dp)
                                            .background(SynkoSurface, CircleShape),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.PhoneAndroid,
                                            contentDescription = null,
                                            tint = SynkoPrimary,
                                            modifier = Modifier.size(24.dp)
                                        )
                                    }

                                    Spacer(modifier = Modifier.width(12.dp))

                                    Column {
                                        Text(
                                            text = if (device.nickname.isNotBlank()) device.nickname else device.deviceName,
                                            fontSize = 17.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = SynkoTextPrimary
                                        )
                                        Text(
                                            text = "${device.deviceType} • ${device.connectionCode}",
                                            fontSize = 12.sp,
                                            color = SynkoTextSecondary
                                        )
                                    }
                                }

                                Row {
                                    IconButton(onClick = { viewModel.toggleFavoriteDevice(device) }) {
                                        Icon(
                                            imageVector = if (device.isFavorite) Icons.Default.Star else Icons.Default.StarBorder,
                                            contentDescription = "Favorite",
                                            tint = if (device.isFavorite) Color(0xFFF59E0B) else SynkoTextSecondary
                                        )
                                    }

                                    IconButton(onClick = {
                                        editingDevice = device
                                        editNicknameText = device.nickname
                                    }) {
                                        Icon(Icons.Default.Edit, contentDescription = "Rename", tint = SynkoPrimary)
                                    }

                                    IconButton(onClick = { viewModel.deleteDevice(device.deviceId) }) {
                                        Icon(Icons.Default.Delete, contentDescription = "Remove", tint = SynkoError)
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    StatusBadge(
                                        text = if (device.isTrusted) "Trusted Device" else "Standard Device",
                                        color = if (device.isTrusted) SynkoSuccess else SynkoTextSecondary,
                                        backgroundColor = if (device.isTrusted) SynkoSuccessLight else SynkoSurface,
                                        icon = if (device.isTrusted) Icons.Default.VerifiedUser else null
                                    )
                                }

                                Button(
                                    onClick = {
                                        viewModel.connectWithCode(device.connectionCode, device.deviceName)
                                    },
                                    shape = RoundedCornerShape(14.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = SynkoPrimary)
                                ) {
                                    Icon(Icons.Default.Refresh, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Connect", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                }
                            }
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(32.dp))
                }
            }
        }
    }

    if (editingDevice != null) {
        Dialog(onDismissRequest = { editingDevice = null }) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = SynkoWhite)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Rename Device Nickname", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = editNicknameText,
                        onValueChange = { editNicknameText = it },
                        label = { Text("Device Nickname") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Row(modifier = Modifier.fillMaxWidth()) {
                        TextButton(onClick = { editingDevice = null }, modifier = Modifier.weight(1f)) {
                            Text("Cancel")
                        }
                        Button(
                            onClick = {
                                viewModel.saveDevice(editingDevice!!.copy(nickname = editNicknameText))
                                editingDevice = null
                                Toast.makeText(context, "Nickname saved", Toast.LENGTH_SHORT).show()
                            },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = SynkoPrimary)
                        ) {
                            Text("Save", color = Color.White)
                        }
                    }
                }
            }
        }
    }
}
