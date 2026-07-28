package com.example.ui.screens

import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentPaste
import androidx.compose.material.icons.filled.DesktopWindows
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.SynkoGlassCard
import com.example.ui.theme.SynkoAccentLight
import com.example.ui.theme.SynkoBorder
import com.example.ui.theme.SynkoPrimary
import com.example.ui.theme.SynkoSuccess
import com.example.ui.theme.SynkoSuccessLight
import com.example.ui.theme.SynkoSurface
import com.example.ui.theme.SynkoTextPrimary
import com.example.ui.theme.SynkoTextSecondary
import com.example.ui.theme.SynkoWhite
import com.example.ui.viewmodel.MainViewModel

@Composable
fun ConnectScreen(
    viewModel: MainViewModel,
    onNavigateToQrScanner: () -> Unit
) {
    val context = LocalContext.current
    var inputCode by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SynkoWhite)
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .size(64.dp)
                .background(SynkoAccentLight, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.DesktopWindows,
                contentDescription = null,
                tint = SynkoPrimary,
                modifier = Modifier.size(32.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Connect to Device",
            fontSize = 26.sp,
            fontWeight = FontWeight.ExtraBold,
            color = SynkoTextPrimary
        )

        Text(
            text = "Enter the 9-digit connection code provided by the device owner",
            fontSize = 13.sp,
            color = SynkoTextSecondary,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(32.dp))

        SynkoGlassCard(cornerRadius = 24.dp) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "ENTER CONNECTION CODE",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = SynkoTextSecondary,
                    letterSpacing = 1.2.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Large Input Box
                OutlinedTextField(
                    value = inputCode,
                    onValueChange = { text ->
                        if (text.length <= 11) {
                            inputCode = text
                        }
                    },
                    placeholder = {
                        Text(
                            text = "483 629 154",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = SynkoTextSecondary.copy(alpha = 0.5f),
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                    },
                    textStyle = TextStyle(
                        fontSize = 24.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = SynkoPrimary,
                        textAlign = TextAlign.Center,
                        letterSpacing = 2.sp
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(68.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = SynkoPrimary,
                        unfocusedBorderColor = SynkoBorder,
                        focusedContainerColor = SynkoWhite,
                        unfocusedContainerColor = SynkoSurface
                    ),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Action Button: Connect
                Button(
                    onClick = {
                        if (inputCode.replace(" ", "").length >= 6) {
                            viewModel.connectWithCode(inputCode)
                        } else {
                            Toast.makeText(context, "Please enter a valid connection code", Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .shadow(6.dp, RoundedCornerShape(18.dp)),
                    shape = RoundedCornerShape(18.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = SynkoPrimary)
                ) {
                    Text(
                        text = "Connect",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(modifier = Modifier.fillMaxWidth()) {
                    // Scan QR Button
                    OutlinedButton(
                        onClick = onNavigateToQrScanner,
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp),
                        shape = RoundedCornerShape(16.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, SynkoBorder)
                    ) {
                        Icon(
                            imageVector = Icons.Default.QrCodeScanner,
                            contentDescription = null,
                            tint = SynkoPrimary,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Scan QR", fontSize = 13.sp, color = SynkoTextPrimary, fontWeight = FontWeight.Bold)
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    // Paste Invite Button
                    OutlinedButton(
                        onClick = {
                            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            val clip = clipboard.primaryClip
                            if (clip != null && clip.itemCount > 0) {
                                val text = clip.getItemAt(0).text.toString()
                                val extracted = text.replace(Regex("[^0-9]"), "")
                                if (extracted.length >= 6) {
                                    inputCode = extracted.take(9)
                                    Toast.makeText(context, "Pasted code from clipboard", Toast.LENGTH_SHORT).show()
                                } else {
                                    Toast.makeText(context, "Clipboard does not contain a valid code", Toast.LENGTH_SHORT).show()
                                }
                            }
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp),
                        shape = RoundedCornerShape(16.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, SynkoBorder)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ContentPaste,
                            contentDescription = null,
                            tint = SynkoPrimary,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Paste Invite", fontSize = 13.sp, color = SynkoTextPrimary, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Security Assurance Box
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(SynkoSuccessLight, RoundedCornerShape(16.dp))
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Security,
                contentDescription = null,
                tint = SynkoSuccess,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = "Explicit Owner Approval Required",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = SynkoSuccess
                )
                Text(
                    text = "A prompt will appear on the remote device asking for explicit consent before streaming begins.",
                    fontSize = 12.sp,
                    color = SynkoTextSecondary
                )
            }
        }
    }
}
