package com.example.acapp.ui.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.acapp.ui.theme.AcColors

@Composable
fun AcChip(
    text: String,
    modifier: Modifier = Modifier,
    tint: Color = AcColors.Sky,
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(50),
        color = tint.copy(alpha = 0.7f),
        contentColor = AcColors.Leaf,
    ) {
        Text(
            text = text.uppercase(),
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier.padding(PaddingValues(horizontal = 12.dp, vertical = 6.dp)),
        )
    }
}
