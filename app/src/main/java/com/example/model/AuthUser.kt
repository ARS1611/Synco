package com.example.model

data class AuthUser(
    val uid: String,
    val name: String,
    val email: String,
    val photoUrl: String? = null,
    val subscriptionPlan: String = "Synko Pro",
    val language: String = "English (US)",
    val country: String = "United States",
    val isRemembered: Boolean = true
)
