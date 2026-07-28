package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.repository.SynkoRepository
import com.example.model.AuthUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = SynkoRepository(application)

    val currentUser: StateFlow<AuthUser?> = repository.currentUser

    private val _isLoginView = MutableStateFlow(true)
    val isLoginView: StateFlow<Boolean> = _isLoginView.asStateFlow()

    private val _emailInput = MutableStateFlow("")
    val emailInput: StateFlow<String> = _emailInput.asStateFlow()

    private val _passwordInput = MutableStateFlow("")
    val passwordInput: StateFlow<String> = _passwordInput.asStateFlow()

    private val _nameInput = MutableStateFlow("")
    val nameInput: StateFlow<String> = _nameInput.asStateFlow()

    private val _rememberMe = MutableStateFlow(true)
    val rememberMe: StateFlow<Boolean> = _rememberMe.asStateFlow()

    private val _showForgotPassword = MutableStateFlow(false)
    val showForgotPassword: StateFlow<Boolean> = _showForgotPassword.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    fun toggleAuthMode() {
        _isLoginView.value = !_isLoginView.value
        _errorMessage.value = null
    }

    fun onEmailChanged(value: String) { _emailInput.value = value }
    fun onPasswordChanged(value: String) { _passwordInput.value = value }
    fun onNameChanged(value: String) { _nameInput.value = value }
    fun toggleRememberMe() { _rememberMe.value = !_rememberMe.value }
    fun setShowForgotPassword(show: Boolean) { _showForgotPassword.value = show }
    fun clearError() { _errorMessage.value = null }

    fun login() {
        if (_emailInput.value.isBlank()) {
            _errorMessage.value = "Please enter your email address"
            return
        }
        if (_passwordInput.value.length < 6) {
            _errorMessage.value = "Password must be at least 6 characters"
            return
        }
        viewModelScope.launch {
            repository.loginWithEmail(_emailInput.value, _passwordInput.value)
        }
    }

    fun loginWithGoogle() {
        viewModelScope.launch {
            repository.loginWithGoogle()
        }
    }

    fun loginWithFacebook() {
        viewModelScope.launch {
            repository.loginWithFacebook()
        }
    }

    fun loginWithApple() {
        viewModelScope.launch {
            repository.loginWithApple()
        }
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }

    fun deleteAccount() {
        viewModelScope.launch {
            repository.deleteAccount()
        }
    }
}
