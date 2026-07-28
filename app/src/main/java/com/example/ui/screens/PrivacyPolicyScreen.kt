package com.example.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Policy
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.SynkoGlassCard
import com.example.ui.theme.SynkoAccentLight
import com.example.ui.theme.SynkoPrimary
import com.example.ui.theme.SynkoTextDark
import com.example.ui.theme.SynkoTextSecondary
import com.example.ui.theme.SynkoWhite

@Composable
fun PrivacyPolicyScreen(onBack: () -> Unit) {
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
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = SynkoTextDark
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Privacy Policy",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = SynkoTextDark
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Privacy Banner
        SynkoGlassCard(cornerRadius = 24.dp, containerColor = Color(0xFFF8FAFC)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(SynkoAccentLight, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Policy,
                        contentDescription = null,
                        tint = SynkoPrimary,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Spacer(modifier = Modifier.width(14.dp))
                Column {
                    Text(
                        text = "Synko Privacy Commitment",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = SynkoTextDark
                    )
                    Text(
                        text = "Last updated: July 2026",
                        fontSize = 12.sp,
                        color = SynkoTextSecondary
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        PrivacySection(
            title = "1. Information Collected",
            content = "We collect minimal information necessary to facilitate secure remote access:\n" +
                    "• Account Identifiers: Name and email address when creating an account.\n" +
                    "• Device Metadata: Device model name, operating system version, and unique session token for connection handshakes.\n" +
                    "• Connection Metrics: Anonymized connection quality and duration logs for performance tuning."
        )

        PrivacySection(
            title = "2. Information NOT Collected",
            content = "Synko operates under a zero-knowledge remote viewing architecture:\n" +
                    "• We NEVER record, stream to server disk, or store your screen content.\n" +
                    "• Passwords, bank credentials, and personal photos shown during sessions are never logged.\n" +
                    "• We do NOT track or sell user behavioral data to third-party advertisers."
        )

        PrivacySection(
            title = "3. How Data is Protected & End-to-End Encryption",
            content = "All video streams, input events, file transfers, and chat communications are secured using 256-bit AES End-to-End Encryption (E2EE) with TLS 1.3 transport security. Session connection codes automatically expire after 5 minutes."
        )

        PrivacySection(
            title = "4. User Rights & Account Deletion",
            content = "You retain full control over your data:\n" +
                    "• Access & Export: You can view your session history and saved devices at any time.\n" +
                    "• Account Deletion: You can permanently delete your account and associated records directly from Settings > Delete Account.\n" +
                    "• Revoke Access: Device owners can terminate any active remote session instantly with a single tap."
        )

        PrivacySection(
            title = "5. Third-Party Services",
            content = "Synko integrates strictly essential, industry-standard infrastructure services:\n" +
                    "• Firebase Authentication: Secure login session management.\n" +
                    "• Google Sign-In: OAuth 2.0 single sign-on.\n" +
                    "• Facebook Login: Official Facebook OAuth SDK.\n" +
                    "• Apple Sign-In: Supported cross-platform authentication."
        )

        PrivacySection(
            title = "6. Contact Us",
            content = "For privacy inquiries, data requests, or security concerns, contact our dedicated privacy officer:\n" +
                    "Email: privacy@synko.app"
        )

        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
private fun PrivacySection(title: String, content: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)
            .background(Color(0xFFF8FAFC), RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        Text(
            text = title,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = SynkoTextDark
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = content,
            fontSize = 13.sp,
            color = SynkoTextSecondary,
            lineHeight = 19.sp
        )
    }
}
