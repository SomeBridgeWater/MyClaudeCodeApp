package com.example.myclaudecodeapp.ui.chat

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import org.json.JSONObject

/** チャットメッセージのデータクラス */
data class ChatMessage(
    val id: Long,
    val type: String,
    val username: String,
    val text: String,
    val timestamp: String
)

/** WebSocket接続状態 */
enum class ConnectionStatus {
    DISCONNECTED, CONNECTING, CONNECTED, ERROR
}

/**
 * チャットルーム画面のViewModel
 * OkHttp3 WebSocketでサーバーと通信する
 */
class ChatViewModel : ViewModel() {

    private val _messages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val messages: StateFlow<List<ChatMessage>> = _messages.asStateFlow()

    private val _connectionStatus = MutableStateFlow(ConnectionStatus.DISCONNECTED)
    val connectionStatus: StateFlow<ConnectionStatus> = _connectionStatus.asStateFlow()

    private val _currentUsername = MutableStateFlow("")
    val currentUsername: StateFlow<String> = _currentUsername.asStateFlow()

    private val client = OkHttpClient()
    private var webSocket: WebSocket? = null

    /** チャットサーバーに接続し、joinメッセージを送信する */
    fun connect(username: String) {
        if (_connectionStatus.value == ConnectionStatus.CONNECTED ||
            _connectionStatus.value == ConnectionStatus.CONNECTING
        ) return

        _currentUsername.value = username
        _connectionStatus.value = ConnectionStatus.CONNECTING

        val request = Request.Builder()
            .url("ws://192.168.10.101:8080")
            .build()

        webSocket = client.newWebSocket(request, object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                val joinJson = JSONObject().apply {
                    put("type", "join")
                    put("username", username)
                }.toString()
                webSocket.send(joinJson)
                _connectionStatus.value = ConnectionStatus.CONNECTED
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                val json = JSONObject(text)
                val type = json.optString("type")
                val msgUsername = json.optString("username")
                val timestamp = json.optString("timestamp")

                val message = when (type) {
                    "join" -> ChatMessage(
                        id = System.nanoTime(),
                        type = type,
                        username = msgUsername,
                        text = "[入室] $msgUsername ($timestamp)",
                        timestamp = timestamp
                    )
                    "message" -> ChatMessage(
                        id = System.nanoTime(),
                        type = type,
                        username = msgUsername,
                        text = json.optString("text"),
                        timestamp = timestamp
                    )
                    "leave" -> ChatMessage(
                        id = System.nanoTime(),
                        type = type,
                        username = msgUsername,
                        text = "[退室] $msgUsername ($timestamp)",
                        timestamp = timestamp
                    )
                    else -> return
                }

                _messages.value = _messages.value + message
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                _connectionStatus.value = ConnectionStatus.ERROR
            }

            override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                _connectionStatus.value = ConnectionStatus.DISCONNECTED
            }
        })
    }

    /** メッセージを送信する */
    fun sendMessage(text: String) {
        if (text.isBlank()) return
        val json = JSONObject().apply {
            put("type", "message")
            put("text", text)
        }.toString()
        webSocket?.send(json)
    }

    /** WebSocket接続を切断する（leaveメッセージ送信後にクローズ） */
    fun disconnect() {
        if (_connectionStatus.value == ConnectionStatus.CONNECTED) {
            val leaveJson = JSONObject().apply {
                put("type", "leave")
                put("username", _currentUsername.value)
            }.toString()
            webSocket?.send(leaveJson)
        }
        webSocket?.close(1000, "ユーザーが切断")
        webSocket = null
        _currentUsername.value = ""
        _connectionStatus.value = ConnectionStatus.DISCONNECTED
    }

    override fun onCleared() {
        super.onCleared()
        disconnect()
    }
}
