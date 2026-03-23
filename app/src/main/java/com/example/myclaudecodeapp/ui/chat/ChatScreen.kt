package com.example.myclaudecodeapp.ui.chat

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel

/**
 * チャットルーム画面
 */
@Composable
fun ChatScreen(
    onBack: () -> Unit = {},
    viewModel: ChatViewModel = viewModel()
) {
    val messages by viewModel.messages.collectAsStateWithLifecycle()
    val connectionStatus by viewModel.connectionStatus.collectAsStateWithLifecycle()

    var usernameInput by remember { mutableStateOf("") }
    var messageInput by remember { mutableStateOf("") }

    val listState = rememberLazyListState()
    val focusManager = LocalFocusManager.current

    // 自分が一番下にいるかどうかを判定するプロパティ
    val isAtBottom = remember {
        derivedStateOf {
            val lastVisibleItem = listState.layoutInfo.visibleItemsInfo.lastOrNull()
            lastVisibleItem?.index == listState.layoutInfo.totalItemsCount - 1
        }
    }
    // 新着メッセージが届いたら最下部へスクロール
    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            if (isAtBottom.value) {
                listState.animateScrollToItem(messages.size - 1)
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
            // 背景タップでキーボードを非表示にする。
            .pointerInput(Unit) { detectTapGestures(onTap = { focusManager.clearFocus() }) }
    ) {

        // コンパクトヘッダー: 戻るボタン + 接続ステータス + 切断ボタン
        val statusText = when (connectionStatus) {
            ConnectionStatus.DISCONNECTED -> "未接続"
            ConnectionStatus.CONNECTING -> "接続中..."
            ConnectionStatus.CONNECTED -> "接続済み"
            ConnectionStatus.ERROR -> "エラー"
        }
        val statusColor = when (connectionStatus) {
            ConnectionStatus.CONNECTED -> MaterialTheme.colorScheme.primary
            ConnectionStatus.ERROR -> MaterialTheme.colorScheme.error
            else -> MaterialTheme.colorScheme.onSurfaceVariant
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = {
                viewModel.disconnect()
                onBack()
            }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "戻る"
                )
            }
            Text(
                text = statusText,
                style = MaterialTheme.typography.labelMedium,
                color = statusColor,
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(1f)
            )
            if (connectionStatus == ConnectionStatus.CONNECTED) {
                TextButton(onClick = { viewModel.disconnect() }) {
                    Text("切断")
                }
            } else {
                Spacer(modifier = Modifier.width(48.dp))
            }
        }

        HorizontalDivider()

        // 接続エリア（未接続時のみ表示）
        if (connectionStatus != ConnectionStatus.CONNECTED) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = usernameInput,
                    onValueChange = { usernameInput = it },
                    label = { Text("ユーザー名") },
                    singleLine = true,
                    modifier = Modifier.weight(1f)
                )
                Button(
                    onClick = { viewModel.connect(usernameInput.trim()) },
                    enabled = usernameInput.isNotBlank() &&
                            connectionStatus == ConnectionStatus.DISCONNECTED ||
                            connectionStatus == ConnectionStatus.ERROR
                ) {
                    Text("接続")
                }
            }
        }

        HorizontalDivider()

        // メッセージ一覧
        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            items(messages) { msg ->
                MessageRow(message = msg)
            }
        }

        HorizontalDivider()

        // メッセージ入力エリア
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = messageInput,
                onValueChange = { messageInput = it },
                label = { Text("メッセージ") },
                singleLine = true,
                enabled = connectionStatus == ConnectionStatus.CONNECTED,
                modifier = Modifier.weight(1f)
            )
            Button(
                onClick = {
                    viewModel.sendMessage(messageInput.trim())
                    messageInput = ""
                },
                enabled = connectionStatus == ConnectionStatus.CONNECTED && messageInput.isNotBlank()
            ) {
                Text("送信")
            }
        }
    }
}

/** メッセージ1件の表示 */
@Composable
private fun MessageRow(message: ChatMessage) {
    when (message.type) {
        "message" -> {
            Column(modifier = Modifier.padding(vertical = 2.dp)) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = message.username,
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = message.timestamp,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Text(
                    text = message.text,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
        else -> {
            // join / leave はシステムメッセージとして中央寄せ
            Text(
                text = message.text,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 2.dp)
            )
        }
    }
}
