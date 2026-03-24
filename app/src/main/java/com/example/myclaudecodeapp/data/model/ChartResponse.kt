package com.example.myclaudecodeapp.data.model

import com.google.gson.annotations.SerializedName

/** 電気自動車販売シェアの1データポイント */
data class ElectricCarSale(
    @SerializedName("year") val year: Int,
    @SerializedName("share") val share: Int
)

/** チャートAPIのレスポンスデータクラス */
data class ChartResponse(
    @SerializedName("electricCarSales") val electricCarSales: List<ElectricCarSale>
)
