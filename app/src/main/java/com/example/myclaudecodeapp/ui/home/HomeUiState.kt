package com.example.myclaudecodeapp.ui.home

import com.example.myclaudecodeapp.data.model.HelloResponse

/** ホーム画面のUI状態 */
sealed interface HomeUiState {
    /** 読み込み中 */
    data object Loading : HomeUiState

    /** 取得成功 */
    data class Success(val response: HelloResponse) : HomeUiState

    /** エラー */
    data class Error(val message: String) : HomeUiState
}
