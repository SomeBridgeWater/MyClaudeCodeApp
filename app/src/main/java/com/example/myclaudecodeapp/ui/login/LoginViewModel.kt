package com.example.myclaudecodeapp.ui.login

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * ログイン画面のビジネスロジックを管理するViewModel
 */
class LoginViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    /** メールアドレス入力更新 */
    fun onEmailChange(email: String) {
        _uiState.update { it.copy(email = email, emailError = null) }
    }

    /** パスワード入力更新 */
    fun onPasswordChange(password: String) {
        _uiState.update { it.copy(password = password, passwordError = null) }
    }

    /** パスワード表示/非表示の切り替え */
    fun onTogglePasswordVisibility() {
        _uiState.update { it.copy(isPasswordVisible = !it.isPasswordVisible) }
    }

    /**
     * ログインボタン押下時のバリデーションと処理
     * バリデーション成功時は isLoginSuccess を true に更新する
     */
    fun onLoginClick() {
        val current = _uiState.value
        val emailError = when {
            current.email.isBlank() -> "メールアドレスを入力してください"
            else -> null
        }
        val passwordError = when {
            current.password.isBlank() -> "パスワードを入力してください"
            else -> null
        }

        if (emailError != null || passwordError != null) {
            _uiState.update {
                it.copy(emailError = emailError, passwordError = passwordError)
            }
            return
        }

        // バリデーション成功 → ログイン成功状態に遷移
        _uiState.update { it.copy(isLoginSuccess = true) }
    }

    /** ナビゲーション完了後に状態をリセット */
    fun onLoginNavigated() {
        _uiState.update { it.copy(isLoginSuccess = false) }
    }
}
