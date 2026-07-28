package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.SynkoAccentLight
import com.example.ui.theme.SynkoPrimary
import com.example.ui.theme.SynkoSurface
import com.example.ui.theme.SynkoTextDark
import com.example.ui.theme.SynkoTextMuted
import com.example.ui.theme.SynkoTextSecondary
import com.example.ui.theme.SynkoWhite

@Composable
fun ConsentScreen(
    onContinue: () -> Unit,
    onOpenPrivacyPolicy: () -> Unit,
    onOpenTermsOfService: () -> Unit
) {
    var checkOwnerApprove by remember { mutableStateOf(false) }
    var checkNoAccessWithoutPermission by remember { mutableStateOf(false) }
    var checkPrivacyPolicy by remember { mutableStateOf(false) }
    var checkTermsOfService by remember { mutableStateOf(false) }

    val allChecked = checkOwnerApprove && checkNoAccessWithoutPermission && checkPrivacyPolicy && checkTermsOfService

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SynkoWhite)
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Spacer(modifier = Modifier.height(16.dp))

            Box(
                modifier = Modifier
                    .size(64.dp)
                    .shadow(8.dp, CircleShape)
                    .background(SynkoAccentLight, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Shield,
                    contentDescription = null,
                    tint = SynkoPrimary,
                    modifier = Modifier.size(32.dp)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Remote Access Consent",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = SynkoTextDark
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Please review and accept Synko's security guarantees before starting a remote session.",
                fontSize = 14.sp,
                color = SynkoTextSecondary,
                modifier = Modifier.padding(horizontal = 8.dp)
            )

            Spacer(modifier = Modifier.height(28.dp))

            // Checkbox Items
            ConsentCheckboxItem(
                text = "I understand the device owner must approve every connection.",
                checked = checkOwnerApprove,
                onCheckedChange = { checkOwnerApprove = it }
            )

            Spacer(modifier = Modifier.height(12.dp))

            ConsentCheckboxItem(
                text = "I understand Synko cannot remotely access a device without explicit permission.",
                checked = checkNoAccessWithoutPermission,
                onCheckedChange = { checkNoAccessWithoutPermission = it }
            )

            Spacer(modifier = Modifier.height(12.dp))

            ConsentCheckboxItem(
                text = "I agree to the Privacy Policy.",
                checked = checkPrivacyPolicy,
                onCheckedChange = { checkPrivacyPolicy = it },
                onLinkClick = onOpenPrivacyPolicy
            )

            Spacer(modifier = Modifier.height(12.dp))

            ConsentCheckboxItem(
                text = "I agree to the Terms of Service.",
                checked = checkTermsOfService,
                onCheckedChange = { checkTermsOfService = it },
                onLinkClick = onOpenTermsOfService
            )
        }

        Column(modifier = Modifier.fillMaxWidth()) {
            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = onContinue,
                enabled = allChecked,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .shadow(if (allChecked) 4.dp else 0.dp, CircleShape),
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(
                    containerColor = SynkoPrimary,
                    disabledContainerColor = Color(0xFFE2E8F0),
                    disabledContentColor = SynkoTextMuted
                )
            ) {
                Text(
                    text = "Continue",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (allChecked) Color.White else SynkoTextMuted
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun ConsentCheckboxItem(
    text: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    onLinkClick: (() -> Unit)? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(SynkoSurface, RoundedCornerShape(16.dp))
            .border(1.dp, Color(0xFFF1F5F9), RoundedCornerShape(16.dp))
            .clickable { onCheckedChange(!checked) }
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = CheckboxDefaults.colors(checkedColor = SynkoPrimary)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = text,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                color = SynkoTextDark,
                lineHeight = 18.sp
            )
            if (onLinkClick != null) {
                TextButton(
                    onClick = onLinkClick,
                    modifier = Modifier.height(28.dp)
                ) {
                    Text(
                        text = "Read Details",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = SynkoPrimary
                    )
                }
            }
        }
    }
}
