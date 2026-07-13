package com.example.acapp.ui.theme

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.drawscope.rotate

private data class LeafSpec(
    val xFrac: Float,
    val yFrac: Float,
    val angle: Float,
    val sizeFrac: Float,
)

private val Leaves = listOf(
    LeafSpec(0.08f, 0.12f, -25f, 0.22f),
    LeafSpec(0.78f, 0.06f, 40f, 0.18f),
    LeafSpec(0.55f, 0.22f, -60f, 0.14f),
    LeafSpec(0.18f, 0.40f, 15f, 0.20f),
    LeafSpec(0.88f, 0.36f, -30f, 0.16f),
    LeafSpec(0.42f, 0.52f, 55f, 0.12f),
    LeafSpec(0.05f, 0.68f, -10f, 0.24f),
    LeafSpec(0.72f, 0.62f, 25f, 0.20f),
    LeafSpec(0.30f, 0.80f, -45f, 0.18f),
    LeafSpec(0.62f, 0.88f, 70f, 0.14f),
    LeafSpec(0.92f, 0.78f, -20f, 0.22f),
    LeafSpec(0.15f, 0.94f, 35f, 0.16f),
)

@Composable
fun LeafBackground(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(AcColors.Sand, AcColors.Paper),
                ),
            ),
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height
            val baseUnit = minOf(w, h)
            Leaves.forEach { leaf ->
                val cx = w * leaf.xFrac
                val cy = h * leaf.yFrac
                val leafW = baseUnit * leaf.sizeFrac
                val leafH = leafW * 0.45f
                rotate(degrees = leaf.angle, pivot = Offset(cx, cy)) {
                    drawOval(
                        color = AcColors.Mint.copy(alpha = 0.08f),
                        topLeft = Offset(cx - leafW / 2f, cy - leafH / 2f),
                        size = Size(leafW, leafH),
                    )
                }
            }
        }
        content()
    }
}
