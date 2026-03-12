package com.example.myclaudecodeapp.data.model

import com.google.gson.annotations.SerializedName

/** commitオブジェクトのデータクラス */
data class CommitInfo(
    @SerializedName("commit1") val commit1: String,
    @SerializedName("commit2") val commit2: String
)

/** APIレスポンスのデータクラス */
data class HelloResponse(
    @SerializedName("message") val message: String,
    @SerializedName("commit") val commit: CommitInfo,
    @SerializedName("data") val data: List<String>
)
