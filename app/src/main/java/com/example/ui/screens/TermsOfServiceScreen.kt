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
import androidx.compose.material.icons.filled.Info
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
fun TermsOfServiceScreen(onBack: () -> Unit) {
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
                text = "Terms of Service",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = SynkoTextDark
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Terms Header Card
        SynkoGlassCard(cornerRadius = 24.dp, containerColor = Color(0xFFF8FAFC)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(SynkoAccentLight, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = null,
                        tint = SynkoPrimary,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Spacer(modifier = Modifier.width(14.dp))
                Column {
                    Text(
                        text = "Synko Terms of Agreement",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = SynkoTextDark
                    )
                    Text(
                        text = "Effective Date: July 2026",
                        fontSize = 12.sp,
                        color = SynkoTextSecondary
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        TermsSection(
            title = "1. Acceptance of Terms",
            content = "By downloading, installing, or using Synko, you agree to be bound by these Terms of Service. If you do not agree to all terms, you must not access or use the application."
        )

        TermsSection(
            title = "2. User Responsibilities",
            content = "Users are responsible for maintaining the confidentiality of their credentials and connection codes. You agree to notify Synko immediately if you suspect unauthorized access to your account."
        )

        TermsSection(
            title = "3. Prohibited Activities",
            content = "You expressly agree NOT to:\n" +
                    "• Connect to any device without explicit owner authorization.\n" +
                    "• Use Synko for unauthorized surveillance, malware deployment, or scam operations.\n" +
                    "• Attempt to reverse engineer, decompile, or intercept encrypted session payloads."
        )

        TermsSection(
            title = "4. Remote Access Requires Owner Consent",
            content = "Synko strictly enforces explicit consent. Every remote session requires the device owner to approve the connection. Synko does NOT support silent background access."
        )

        TermsSection(
            title = "5. Privacy & Data Handling",
            content = "Your use of Synko is governed by our Privacy Policy, which outlines how session data and account metadata are protected with 256-bit encryption."
        )

        TermsSection(
            title = "6. Intellectual Property",
            content = "All right, title, and interest in Synko, including algorithms, UI elements, trademarks, and branding, remain the exclusive property of Synko Inc."
        )

        TermsSection(
            title = "7. Limitation of Liability",
            content = "Synko is provided 'as is' without warranties of any kind. Synko Inc. shall not be liable for indirect, incidental, or consequential damages resulting from connection outages or device misuse."
        )

        TermsSection(
            title = "8. Termination",
            content = "Synko reserves the right to suspend or terminate accounts violating these terms, engaging in fraudulent support practices, or operating unauthorized commercial remote access."
        )

        TermsSection(
            title = "9. Contact Information",
            content = "For legal questions or notices, reach out to:\nLegal Email: legal@synko.app"
        )

        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
private fun TermsSection(title: String, content: String) {
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
