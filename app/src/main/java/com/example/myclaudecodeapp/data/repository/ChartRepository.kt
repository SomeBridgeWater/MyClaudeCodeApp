package com.example.myclaudecodeapp.data.repository

import com.example.myclaudecodeapp.data.model.ChartResponse
import com.example.myclaudecodeapp.data.remote.RetrofitClient

/** チャートAPIのリポジトリ */
class ChartRepository {

    private val apiService = RetrofitClient.apiService

    /** チャートデータを取得する。成功時は Result.success、失敗時は Result.failure を返す */
    suspend fun getChartData(): Result<ChartResponse> = runCatching {
        apiService.getChart()
    }
}
