package com.example.myclaudecodeapp.ui.chart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myclaudecodeapp.data.repository.ChartRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/** チャート画面のViewModel */
class ChartViewModel(
    private val repository: ChartRepository = ChartRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow<ChartUiState>(ChartUiState.Loading)
    val uiState: StateFlow<ChartUiState> = _uiState.asStateFlow()

    init {
        fetchChart()
    }

    /** APIからチャートデータを取得する */
    fun fetchChart() {
        _uiState.value = ChartUiState.Loading
        viewModelScope.launch {
            repository.getChartData()
                .onSuccess { response ->
                    _uiState.value = ChartUiState.Success(response)
                }
                .onFailure { error ->
                    _uiState.value = ChartUiState.Error(
                        error.message ?: "不明なエラーが発生しました"
                    )
                }
        }
    }
}
