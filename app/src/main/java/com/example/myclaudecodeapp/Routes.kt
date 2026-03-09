package com.example.myclaudecodeapp

import kotlinx.serialization.Serializable

// 画面遷移用の定義
sealed interface Routes {
    // ログイン画面
    @Serializable
    data object LoginRoute: Routes

    // ホーム画面
    @Serializable
    data class MainRoute(val id: String): Routes
}