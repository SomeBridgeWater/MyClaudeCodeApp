package com.example.myclaudecodeapp.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myclaudecodeapp.data.repository.HelloRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/** ホーム画面のViewModel */
class HomeViewModel(
    private val repository: HelloRepository = HelloRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        fetchMessage()
    }

    /** APIからレスポンス全体を取得する */
    fun fetchMessage() {
        _uiState.value = HomeUiState.Loading
        viewModelScope.launch {
            repository.getHelloResponse()
                .onSuccess { response ->
                    _uiState.value = HomeUiState.Success(response)
                }
                .onFailure { error ->
                    _uiState.value = HomeUiState.Error(
                        error.message ?: "不明なエラーが発生しました"
                    )
                }
        }
    }
}
