package com.example.acapp.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import com.example.acapp.data.VillagerRepository
import com.example.acapp.data.dto.Villager
import com.example.acapp.ui.components.AcChip
import com.example.acapp.ui.theme.AcColors
import com.example.acapp.ui.theme.LeafBackground
import com.example.acapp.util.UiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VillagerDetailScreen(
    repo: VillagerRepository,
    villagerId: String,
    onBack: () -> Unit,
) {
    val vm: VillagerDetailViewModel = viewModel(key = villagerId) {
        VillagerDetailViewModel(repo, villagerId)
    }
    val state by vm.state.collectAsStateWithLifecycle()

    LeafBackground {
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                CenterAlignedTopAppBar(
                    title = {},
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = AcColors.Leaf,
                            )
                        }
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = Color.Transparent,
                    ),
                )
            },
        ) { padding ->
            Box(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentAlignment = Alignment.Center,
            ) {
                when (val s = state) {
                    is UiState.Loading -> CircularProgressIndicator(color = AcColors.Mint)
                    is UiState.Error -> ErrorView(s.message, onRetry = vm::load)
                    is UiState.Success -> VillagerDetail(s.value)
                }
            }
        }
    }
}

@Composable
private fun VillagerDetail(v: Villager) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        if (v.imageUrl != null) {
            AsyncImage(
                model = v.imageUrl,
                contentDescription = v.name,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .size(200.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(AcColors.Sand)
                    .border(2.dp, AcColors.Leaf.copy(alpha = 0.15f), RoundedCornerShape(16.dp)),
            )
        } else {
            VillagerAvatar(v, size = 120.dp)
        }
        Spacer(Modifier.height(16.dp))
        Text(
            text = v.name,
            style = MaterialTheme.typography.headlineLarge,
            color = AcColors.Leaf,
        )
        Spacer(Modifier.height(12.dp))
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            AcChip(text = v.species, tint = AcColors.Mint)
            AcChip(text = v.personality, tint = AcColors.Sky)
            AcChip(text = v.gender, tint = AcColors.Sun)
        }
        Spacer(Modifier.height(24.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.large,
            colors = CardDefaults.cardColors(containerColor = AcColors.Paper),
            elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        ) {
            Column(modifier = Modifier.padding(vertical = 4.dp)) {
                InfoRow("🎂", "Cumpleaños", "${monthName(v.birthdayMonth)} ${v.birthdayDay}")
                v.zodiac?.let {
                    HorizontalDivider(color = AcColors.Sand, thickness = 1.dp)
                    InfoRow("✨", "Zodiaco", it.replaceFirstChar(Char::uppercase))
                }
                v.catchphrase?.let {
                    HorizontalDivider(color = AcColors.Sand, thickness = 1.dp)
                    InfoRow("💬", "Frase", "\"$it\"")
                }
                v.hobby?.let {
                    HorizontalDivider(color = AcColors.Sand, thickness = 1.dp)
                    InfoRow("🎨", "Hobby", it.replaceFirstChar(Char::uppercase))
                }
                if (v.games.isNotEmpty()) {
                    HorizontalDivider(color = AcColors.Sand, thickness = 1.dp)
                    InfoRow("🎮", "Aparece en", v.games.joinToString(" · ") { it.uppercase() })
                }
            }
        }
        Spacer(Modifier.height(24.dp))
    }
}

@Composable
private fun InfoRow(icon: String, label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 18.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier.size(36.dp),
            contentAlignment = Alignment.Center,
        ) {
            Text(text = icon, style = TextStyle(fontSize = 22.sp))
        }
        Spacer(Modifier.size(12.dp))
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = label.uppercase(),
                style = MaterialTheme.typography.labelSmall,
                color = AcColors.LeafSoft,
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyLarge,
                color = AcColors.Leaf,
            )
        }
    }
}

private fun monthName(month: Int): String = when (month) {
    1 -> "Enero"; 2 -> "Febrero"; 3 -> "Marzo"; 4 -> "Abril"
    5 -> "Mayo"; 6 -> "Junio"; 7 -> "Julio"; 8 -> "Agosto"
    9 -> "Septiembre"; 10 -> "Octubre"; 11 -> "Noviembre"; 12 -> "Diciembre"
    else -> "?"
}
