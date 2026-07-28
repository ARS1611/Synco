package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import com.example.ui.components.StatusBadge
import com.example.ui.theme.SynkoAccentLight
import com.example.ui.theme.SynkoPrimary
import com.example.ui.theme.SynkoTextDark
import com.example.ui.theme.SynkoTextSecondary
import com.example.ui.theme.SynkoWhite

data class OpenSourceLibrary(
    val name: String,
    val author: String,
    val license: String,
    val version: String
)

@Composable
fun OpenSourceLicensesScreen(onBack: () -> Unit) {
    val libraries = listOf(
        OpenSourceLibrary("Jetpack Compose", "Google Android / Open Source", "Apache License 2.0", "1.6.0"),
        OpenSourceLibrary("Kotlin Coroutines & Flow", "JetBrains s.r.o.", "Apache License 2.0", "1.8.0"),
        OpenSourceLibrary("Room Database", "Google Android", "Apache License 2.0", "2.6.1"),
        OpenSourceLibrary("Firebase Authentication & Realtime", "Google Cloud", "Apache License 2.0", "32.8.0"),
        OpenSourceLibrary("Coil Image Loader", "Coil Contributors", "Apache License 2.0", "2.6.0"),
        OpenSourceLibrary("OkHttp & Retrofit", "Square Inc.", "Apache License 2.0", "2.9.0"),
        OpenSourceLibrary("Material Components for Android", "Google Inc.", "Apache License 2.0", "1.11.0"),
        OpenSourceLibrary("AndroidX Core & Navigation", "Google Android", "Apache License 2.0", "2.7.7")
    )

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
                text = "Open Source Licenses",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = SynkoTextDark
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Synko is powered by these high-quality open source libraries and tools:",
            fontSize = 13.sp,
            color = SynkoTextSecondary
        )

        Spacer(modifier = Modifier.height(20.dp))

        libraries.forEach { lib ->
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp)
                    .background(Color(0xFFF8FAFC), RoundedCornerShape(16.dp))
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = lib.name,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = SynkoTextDark
                    )
                    StatusBadge(
                        text = "v${lib.version}",
                        color = SynkoPrimary,
                        backgroundColor = SynkoAccentLight
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Author: ${lib.author}",
                    fontSize = 12.sp,
                    color = SynkoTextSecondary
                )
                Text(
                    text = "License: ${lib.license}",
                    fontSize = 11.sp,
                    color = SynkoTextSecondary.copy(alpha = 0.8f)
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}
