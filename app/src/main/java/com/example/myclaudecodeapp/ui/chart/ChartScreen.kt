package com.example.myclaudecodeapp.ui.chart

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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myclaudecodeapp.data.model.ElectricCarSale
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.HorizontalAxis
import com.patrykandpatrick.vico.compose.cartesian.axis.VerticalAxis
import com.patrykandpatrick.vico.compose.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.compose.cartesian.data.CartesianValueFormatter
import com.patrykandpatrick.vico.compose.cartesian.data.lineSeries
import com.patrykandpatrick.vico.compose.cartesian.layer.LineCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLineCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.data.CartesianLayerRangeProvider
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.compose.common.Fill

/**
 * チャート画面のComposable
 */
@Composable
fun ChartScreen(
    viewModel: ChartViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    when (val state = uiState) {
        is ChartUiState.Loading -> SkeletonChartContent()

        is ChartUiState.Success -> {
            ElectricCarSalesChart(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.5f)
                    .padding(16.dp),
                data = state.response.electricCarSales
            )
        }

        is ChartUiState.Error -> {
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
                    Button(onClick = viewModel::fetchChart) {
                        Text(text = "再試行")
                    }
                }
            }
        }
    }
}

/** ローディング中のSkeleton Screen */
@Composable
private fun SkeletonChartContent() {
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

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.5f)
            .padding(16.dp)
    ) {
        Row(modifier = Modifier.fillMaxSize()) {
            // Y軸ラベル×6
            Column(
                modifier = Modifier
                    .width(40.dp)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                repeat(6) {
                    SkeletonBox(
                        modifier = Modifier
                            .width(32.dp)
                            .height(12.dp),
                        shimmerColor = shimmerColor
                    )
                }
            }

            Spacer(modifier = Modifier.width(4.dp))

            // グラフ本体 + X軸ラベル
            Column(modifier = Modifier.fillMaxSize()) {
                // グラフ本体
                SkeletonBox(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    shimmerColor = shimmerColor
                )

                Spacer(modifier = Modifier.height(4.dp))

                // X軸ラベル×5
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    repeat(5) {
                        SkeletonBox(
                            modifier = Modifier
                                .width(30.dp)
                                .height(12.dp),
                            shimmerColor = shimmerColor
                        )
                    }
                }
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

/** EV販売シェアの折れ線グラフ（vicoライブラリ使用） */
@Composable
private fun ElectricCarSalesChart(
    data: List<ElectricCarSale>,
    modifier: Modifier = Modifier
) {
    val lineColor = Color(0xFF8C7ECA)
    val areaGradient = remember(lineColor) {
        Brush.verticalGradient(
            colors = listOf(lineColor.copy(alpha = 0.4f), Color.Transparent)
        )
    }

    val modelProducer = remember { CartesianChartModelProducer() }

    LaunchedEffect(data) {
        modelProducer.runTransaction {
            lineSeries {
                series(
                    x = data.map { it.year },
                    y = data.map { it.share }
                )
            }
        }
    }

    val line = remember(lineColor, areaGradient) {
        LineCartesianLayer.Line(
            fill = LineCartesianLayer.LineFill.single(Fill(lineColor)),
            areaFill = LineCartesianLayer.AreaFill.single(Fill(areaGradient)),
        )
    }

    CartesianChartHost(
        modifier = modifier,
        chart = rememberCartesianChart(
            rememberLineCartesianLayer(
                lineProvider = LineCartesianLayer.LineProvider.series(line),
                rangeProvider = CartesianLayerRangeProvider.fixed(minY = 0.0, maxY = 100.0),
            ),
            startAxis = VerticalAxis.rememberStart(
                valueFormatter = CartesianValueFormatter { _, value, _ ->
                    "${value.toInt()}%"
                },
                itemPlacer = remember {
                    VerticalAxis.ItemPlacer.count(count = { 6 })
                },
            ),
            bottomAxis = HorizontalAxis.rememberBottom(
                valueFormatter = CartesianValueFormatter { _, value, _ ->
                    value.toInt().toString()
                },
            ),
        ),
        modelProducer = modelProducer,
    )
}
