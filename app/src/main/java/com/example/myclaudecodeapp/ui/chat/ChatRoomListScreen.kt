package com.example.myclaudecodeapp.ui.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/** チャットルームのダミーデータ */
data class ChatRoomItem(
    val id: Int,
    val name: String,
    val lastMessage: String,
    val timestamp: String,
    val avatarLabel: String
)

private val dummyChatRooms = listOf(
    ChatRoomItem(1, "田中 太郎", "了解しました！明日よろしくお願いします。", "12:30", "田"),
    ChatRoomItem(2, "鈴木 花子", "ありがとうございます😊", "11:45", "鈴"),
    ChatRoomItem(3, "佐藤グループ", "明日の予定について確認お願いします", "10:20", "佐"),
    ChatRoomItem(4, "山田 次郎", "写真送りましたー", "昨日", "山"),
    ChatRoomItem(5, "プロジェクトチーム", "会議の資料を共有しました", "昨日", "プ"),
    ChatRoomItem(6, "伊藤 さくら", "また遊びましょう！", "月曜日", "伊"),
    ChatRoomItem(7, "中村 健一", "お疲れ様でした", "日曜日", "中"),
    ChatRoomItem(8, "小林 美咲", "承知しました。確認します。", "土曜日", "小"),
    ChatRoomItem(9, "加藤ファミリー", "今週末の集まりは何時から？", "金曜日", "加"),
    ChatRoomItem(10, "渡辺 誠", "資料の件、後ほど連絡します", "金曜日", "渡"),
)

/**
 * チャットルーム一覧画面
 */
@Composable
fun ChatRoomListScreen(onRoomClick: (roomName: String) -> Unit = {}) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(dummyChatRooms) { room ->
            ChatRoomRow(room = room, onClick = { onRoomClick(room.name) })
            HorizontalDivider(modifier = Modifier.padding(start = 76.dp))
        }
    }
}

/** チャットルーム1件分の行 */
@Composable
private fun ChatRoomRow(room: ChatRoomItem, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 丸アバター
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primaryContainer),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = room.avatarLabel,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        // ルーム名 + 最新メッセージ
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = room.name,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = room.lastMessage,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        // 日時
        Text(
            text = room.timestamp,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
