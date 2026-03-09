package com.example.myclaudecodeapp.ui.login

/**
 * ログイン画面のUI状態を表すデータクラス
 */
data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val emailError: String? = null,
    val passwordError: String? = null,
    val isPasswordVisible: Boolean = false,
    val isLoginSuccess: Boolean = false
)
