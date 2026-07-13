package com.example.acapp.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.Text
import coil3.compose.AsyncImage
import com.example.acapp.data.dto.Villager
import com.example.acapp.ui.theme.AcColors
import kotlin.math.abs

private val SpeciesEmoji = mapOf(
    "cat" to "🐱",
    "dog" to "🐶",
    "rabbit" to "🐰",
    "bear" to "🐻",
    "cub" to "🐻",
    "duck" to "🦆",
    "frog" to "🐸",
    "wolf" to "🐺",
    "pig" to "🐷",
    "sheep" to "🐑",
    "cow" to "🐮",
    "chicken" to "🐔",
    "bird" to "🐦",
    "elephant" to "🐘",
    "hamster" to "🐹",
    "mouse" to "🐭",
    "koala" to "🐨",
    "monkey" to "🐵",
    "ostrich" to "🦩",
    "kangaroo" to "🦘",
    "penguin" to "🐧",
    "squirrel" to "🐿️",
    "tiger" to "🐯",
    "deer" to "🦌",
    "horse" to "🐴",
    "gorilla" to "🦍",
    "hippo" to "🦛",
    "lion" to "🦁",
    "octopus" to "🐙",
    "rhino" to "🦏",
    "eagle" to "🦅",
    "alligator" to "🐊",
    "anteater" to "🐜",
    "bull" to "🐂",
    "goat" to "🐐",
)

fun speciesEmoji(species: String): String =
    SpeciesEmoji[species.trim().lowercase()] ?: "🌱"

private val PastelPalette = listOf(
    Color(0xFFFFD6A5), // peach
    Color(0xFFFDFFB6), // butter
    Color(0xFFCAFFBF), // lime
    Color(0xFF9BF6FF), // aqua
    Color(0xFFA0C4FF), // periwinkle
    Color(0xFFBDB2FF), // lavender
    Color(0xFFFFC6FF), // pink
    Color(0xFFFFADAD), // coral soft
)

fun personalityHue(personality: String): Color {
    val key = personality.trim().lowercase().ifEmpty { "unknown" }
    val idx = abs(key.hashCode()) % PastelPalette.size
    return PastelPalette[idx]
}

@Composable
fun VillagerAvatar(
    villager: Villager,
    modifier: Modifier = Modifier,
    size: Dp = 56.dp,
) {
    val bg = personalityHue(villager.personality)
    val borderMod = Modifier
        .size(size)
        .clip(CircleShape)
        .background(bg)
        .border(2.dp, AcColors.Leaf.copy(alpha = 0.15f), CircleShape)

    if (villager.imageUrl != null) {
        AsyncImage(
            model = villager.imageUrl,
            contentDescription = villager.name,
            contentScale = ContentScale.Fit,
            modifier = modifier.then(borderMod),
        )
    } else {
        Box(
            modifier = modifier.then(borderMod),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = speciesEmoji(villager.species),
                style = TextStyle(fontSize = (size.value * 0.55f).sp),
            )
        }
    }
}
