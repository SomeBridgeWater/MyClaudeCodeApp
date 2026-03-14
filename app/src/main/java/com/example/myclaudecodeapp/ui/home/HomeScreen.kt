package com.example.myclaudecodeapp.ui.home

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel

/**
 * ホーム画面のComposable
 */
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    when (val state = uiState) {
        is HomeUiState.Loading -> SkeletonHomeContent()

        is HomeUiState.Success -> {
            val response = state.response
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    Text(
                        text = "ホーム画面",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }

                // message
                item {
                    SectionCard(title = "message") {
                        Text(
                            text = response.message,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }

                // commit
                item {
                    SectionCard(title = "commit") {
                        LabelValueRow(label = "commit1", value = response.commit.commit1)
                        HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))
                        LabelValueRow(label = "commit2", value = response.commit.commit2)
                    }
                }

                // dataセクションのヘッダー
                item {
                    Text(
                        text = "data",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }

                // data配列（可変サイズ対応）
                items(response.data) { item ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer
                        )
                    ) {
                        Text(
                            text = item,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                        )
                    }
                }
            }
        }

        is HomeUiState.Error -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = state.message,
                        color = MaterialTheme.colorScheme.error
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = viewModel::fetchMessage) {
                        Text(text = "再試行")
                    }
                }
            }
        }
    }
}

/** ローディング中のSkeleton Screen */
@Composable
private fun SkeletonHomeContent() {
    val infiniteTransition = rememberInfiniteTransition(label = "shimmer")
    val shimmerColor by infiniteTransition.animateColor(
        initialValue = MaterialTheme.colorScheme.surfaceVariant,
        targetValue = MaterialTheme.colorScheme.surface,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 900, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "shimmerColor"
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // タイトル
        item {
            SkeletonBox(
                modifier = Modifier
                    .fillMaxWidth(0.6f)
                    .height(28.dp),
                shimmerColor = shimmerColor
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        // messageセクション
        item {
            SkeletonSectionCard(
                titleWidth = 0.3f,
                shimmerColor = shimmerColor
            ) {
                SkeletonBox(
                    modifier = Modifier.fillMaxWidth().height(16.dp),
                    shimmerColor = shimmerColor
                )
                Spacer(modifier = Modifier.height(6.dp))
                SkeletonBox(
                    modifier = Modifier.fillMaxWidth(0.7f).height(16.dp),
                    shimmerColor = shimmerColor
                )
            }
        }

        // commitセクション
        item {
            SkeletonSectionCard(
                titleWidth = 0.25f,
                shimmerColor = shimmerColor
            ) {
                SkeletonBox(
                    modifier = Modifier.fillMaxWidth(0.4f).height(12.dp),
                    shimmerColor = shimmerColor
                )
                Spacer(modifier = Modifier.height(4.dp))
                SkeletonBox(
                    modifier = Modifier.fillMaxWidth(0.75f).height(16.dp),
                    shimmerColor = shimmerColor
                )
                HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))
                SkeletonBox(
                    modifier = Modifier.fillMaxWidth(0.4f).height(12.dp),
                    shimmerColor = shimmerColor
                )
                Spacer(modifier = Modifier.height(4.dp))
                SkeletonBox(
                    modifier = Modifier.fillMaxWidth(0.75f).height(16.dp),
                    shimmerColor = shimmerColor
                )
            }
        }

        // dataヘッダー
        item {
            SkeletonBox(
                modifier = Modifier.fillMaxWidth(0.2f).height(20.dp),
                shimmerColor = shimmerColor
            )
        }

        // dataカード×3枚
        items(3) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                )
            ) {
                SkeletonBox(
                    modifier = Modifier
                        .fillMaxWidth(0.85f)
                        .height(16.dp)
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    shimmerColor = shimmerColor
                )
            }
        }
    }
}

/** シマーアニメーション付きプレースホルダーBox */
@Composable
private fun SkeletonBox(modifier: Modifier = Modifier, shimmerColor: Color) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(4.dp))
            .background(shimmerColor)
    )
}

/** Skeleton用のセクションCard */
@Composable
private fun SkeletonSectionCard(
    titleWidth: Float,
    shimmerColor: Color,
    content: @Composable () -> Unit
) {
    Column {
        SkeletonBox(
            modifier = Modifier.fillMaxWidth(titleWidth).height(20.dp),
            shimmerColor = shimmerColor
        )
        Spacer(modifier = Modifier.height(4.dp))
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                content()
            }
        }
    }
}

/** セクションをCardで囲むComposable */
@Composable
private fun SectionCard(title: String, content: @Composable () -> Unit) {
    Column {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(4.dp))
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                content()
            }
        }
    }
}

/** ラベルと値を横並びで表示するComposable */
@Composable
private fun LabelValueRow(label: String, value: String) {
    Column {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.outline
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}
