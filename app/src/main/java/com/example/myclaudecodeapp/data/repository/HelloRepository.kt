package com.example.myclaudecodeapp.data.repository

import com.example.myclaudecodeapp.data.model.HelloResponse
import com.example.myclaudecodeapp.data.remote.RetrofitClient

/** Hello APIのリポジトリ */
class HelloRepository {

    private val apiService = RetrofitClient.apiService

    /** レスポンス全体を取得する。成功時は Result.success、失敗時は Result.failure を返す */
    suspend fun getHelloResponse(): Result<HelloResponse> = runCatching {
        apiService.getHello()
    }
}
