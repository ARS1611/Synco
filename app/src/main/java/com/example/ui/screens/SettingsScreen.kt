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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Devices
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Policy
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ui.components.SynkoGlassCard
import com.example.ui.theme.SynkoAccentLight
import com.example.ui.theme.SynkoError
import com.example.ui.theme.SynkoPrimary
import com.example.ui.theme.SynkoSurface
import com.example.ui.theme.SynkoTextDark
import com.example.ui.theme.SynkoTextPrimary
import com.example.ui.theme.SynkoTextSecondary
import com.example.ui.theme.SynkoWhite
import com.example.ui.viewmodel.AuthViewModel

@Composable
fun SettingsScreen(
    authViewModel: AuthViewModel,
    onNavigateToProfile: () -> Unit,
    onNavigateToDevices: () -> Unit,
    onNavigateToPrivacyPolicy: () -> Unit,
    onNavigateToTermsOfService: () -> Unit,
    onNavigateToLicenses: () -> Unit,
    onNavigateToConsent: () -> Unit
) {
    val context = LocalContext.current
    val currentUser by authViewModel.currentUser.collectAsState()

    var notificationsEnabled by remember { mutableStateOf(true) }
    var showAboutDialog by remember { mutableStateOf(false) }
    var showDeleteAccountDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SynkoWhite)
            .verticalScroll(rememberScrollState())
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
                    imageVector = Icons.Default.Settings,
                    contentDescription = null,
                    tint = SynkoPrimary,
                    modifier = Modifier.size(26.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = "Settings",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = SynkoTextPrimary
                )
                Text(
                    text = "Profile, accounts, and privacy preferences",
                    fontSize = 13.sp,
                    color = SynkoTextSecondary
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Section 1: Account & Profile
        SettingsGroupHeader("ACCOUNT")
        SynkoGlassCard(cornerRadius = 20.dp) {
            Column {
                SettingsClickableItem(
                    title = "Profile",
                    subtitle = currentUser?.name ?: "Alex Mercer",
                    icon = Icons.Default.Person,
                    onClick = onNavigateToProfile
                )

                Spacer(modifier = Modifier.height(12.dp))

                SettingsClickableItem(
                    title = "Google Account",
                    subtitle = "Connected (alex.mercer@gmail.com)",
                    icon = Icons.Default.AccountCircle,
                    onClick = { Toast.makeText(context, "Google Account is connected.", Toast.LENGTH_SHORT).show() }
                )

                Spacer(modifier = Modifier.height(12.dp))

                SettingsClickableItem(
                    title = "Facebook Account",
                    subtitle = "Connected (alex.mercer@facebook.com)",
                    icon = Icons.Default.AccountCircle,
                    onClick = { Toast.makeText(context, "Facebook Account linked.", Toast.LENGTH_SHORT).show() }
                )

                Spacer(modifier = Modifier.height(12.dp))

                SettingsClickableItem(
                    title = "Apple Account",
                    subtitle = "Only supported on iOS devices",
                    icon = Icons.Default.AccountCircle,
                    onClick = { Toast.makeText(context, "Apple Sign-In is only available on iOS/supported platforms.", Toast.LENGTH_SHORT).show() }
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Section 2: App Preferences
        SettingsGroupHeader("PREFERENCES & DEVICES")
        SynkoGlassCard(cornerRadius = 20.dp) {
            Column {
                SettingsClickableItem(
                    title = "Language",
                    subtitle = currentUser?.language ?: "English (US)",
                    icon = Icons.Default.Language,
                    onClick = { Toast.makeText(context, "Language: English (US)", Toast.LENGTH_SHORT).show() }
                )

                Spacer(modifier = Modifier.height(12.dp))

                SettingsToggleItem(
                    title = "Notifications",
                    subtitle = "Receive connection alerts & updates",
                    icon = Icons.Default.Notifications,
                    checked = notificationsEnabled,
                    onCheckedChange = { notificationsEnabled = it }
                )

                Spacer(modifier = Modifier.height(12.dp))

                SettingsClickableItem(
                    title = "Connected Devices",
                    subtitle = "Manage paired phones & tablets",
                    icon = Icons.Default.Devices,
                    onClick = onNavigateToDevices
                )

                Spacer(modifier = Modifier.height(12.dp))

                SettingsClickableItem(
                    title = "Security & Consent",
                    subtitle = "Review remote access agreement",
                    icon = Icons.Default.Security,
                    onClick = onNavigateToConsent
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Section 3: Legal & About
        SettingsGroupHeader("LEGAL & ABOUT")
        SynkoGlassCard(cornerRadius = 20.dp) {
            Column {
                SettingsClickableItem(
                    title = "Privacy Policy",
                    subtitle = "Data handling & end-to-end encryption",
                    icon = Icons.Default.Policy,
                    onClick = onNavigateToPrivacyPolicy
                )

                Spacer(modifier = Modifier.height(12.dp))

                SettingsClickableItem(
                    title = "Terms of Service",
                    subtitle = "Usage agreement & rules",
                    icon = Icons.Default.Info,
                    onClick = onNavigateToTermsOfService
                )

                Spacer(modifier = Modifier.height(12.dp))

                SettingsClickableItem(
                    title = "Open Source Licenses",
                    subtitle = "Third-party libraries & tools",
                    icon = Icons.Default.Code,
                    onClick = onNavigateToLicenses
                )

                Spacer(modifier = Modifier.height(12.dp))

                SettingsClickableItem(
                    title = "About Synko",
                    subtitle = "Version 1.0.0 (Build 100)",
                    icon = Icons.Default.Info,
                    onClick = { showAboutDialog = true }
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Section 4: Account Actions
        SettingsGroupHeader("ACCOUNT ACTIONS")
        SynkoGlassCard(cornerRadius = 20.dp) {
            Column {
                SettingsClickableItem(
                    title = "Log Out",
                    subtitle = "Sign out of your account",
                    icon = Icons.Default.ExitToApp,
                    onClick = { authViewModel.logout() }
                )

                Spacer(modifier = Modifier.height(12.dp))

                SettingsClickableItem(
                    title = "Delete Account",
                    subtitle = "Permanently remove account & session data",
                    icon = Icons.Default.Delete,
                    onClick = { showDeleteAccountDialog = true }
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))
    }

    if (showAboutDialog) {
        Dialog(onDismissRequest = { showAboutDialog = false }) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(16.dp, RoundedCornerShape(24.dp)),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = SynkoWhite)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "Synko Remote Assistance", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = SynkoTextDark)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "Version 1.0.0 (Build 100)\n256-Bit E2E Encrypted Remote Viewing", fontSize = 13.sp, color = SynkoTextSecondary, textAlign = TextAlign.Center)
                    Spacer(modifier = Modifier.height(20.dp))
                    Button(
                        onClick = { showAboutDialog = false },
                        shape = CircleShape,
                        colors = ButtonDefaults.buttonColors(containerColor = SynkoPrimary)
                    ) {
                        Text("Close", color = Color.White)
                    }
                }
            }
        }
    }

    if (showDeleteAccountDialog) {
        Dialog(onDismissRequest = { showDeleteAccountDialog = false }) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(16.dp, RoundedCornerShape(24.dp)),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = SynkoWhite)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "Delete Account?", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = SynkoError)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Are you sure? This will permanently delete your profile, paired devices, and session history.",
                        fontSize = 13.sp,
                        color = SynkoTextSecondary,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Row(modifier = Modifier.fillMaxWidth()) {
                        OutlinedButton(
                            onClick = { showDeleteAccountDialog = false },
                            modifier = Modifier.weight(1f),
                            shape = CircleShape
                        ) {
                            Text("Cancel")
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Button(
                            onClick = {
                                showDeleteAccountDialog = false
                                authViewModel.deleteAccount()
                            },
                            modifier = Modifier.weight(1f),
                            shape = CircleShape,
                            colors = ButtonDefaults.buttonColors(containerColor = SynkoError)
                        ) {
                            Text("Delete", color = Color.White)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SettingsGroupHeader(title: String) {
    Text(
        text = title,
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold,
        color = SynkoTextSecondary,
        letterSpacing = 1.2.sp,
        modifier = Modifier.padding(bottom = 8.dp, start = 4.dp)
    )
}

@Composable
fun SettingsToggleItem(
    title: String,
    subtitle: String,
    icon: ImageVector,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
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
                    .size(38.dp)
                    .background(SynkoSurface, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = icon, contentDescription = null, tint = SynkoPrimary, modifier = Modifier.size(20.dp))
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(text = title, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = SynkoTextPrimary)
                Text(text = subtitle, fontSize = 12.sp, color = SynkoTextSecondary)
            }
        }

        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = SynkoPrimary)
        )
    }
}

@Composable
fun SettingsClickableItem(
    title: String,
    subtitle: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .background(SynkoSurface, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = icon, contentDescription = null, tint = SynkoPrimary, modifier = Modifier.size(20.dp))
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(text = title, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = SynkoTextPrimary)
                Text(text = subtitle, fontSize = 12.sp, color = SynkoTextSecondary)
            }
        }

        Icon(Icons.Default.ChevronRight, contentDescription = null, tint = SynkoTextSecondary)
    }
}
