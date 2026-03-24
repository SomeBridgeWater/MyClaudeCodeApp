package com.example.myclaudecodeapp.ui.chart

import com.example.myclaudecodeapp.data.model.ChartResponse

/** チャート画面のUI状態 */
sealed interface ChartUiState {
    /** 読み込み中 */
    data object Loading : ChartUiState

    /** 取得成功 */
    data class Success(val response: ChartResponse) : ChartUiState

    /** エラー */
    data class Error(val message: String) : ChartUiState
}
