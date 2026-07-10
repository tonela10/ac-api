package com.example.acapp.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.acapp.data.VillagerRepository
import com.example.acapp.data.dto.Villager
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

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = (state as? UiState.Success<Villager>)?.value?.name ?: "Villager",
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                        )
                    }
                },
            )
        },
    ) { padding ->
        Box(
            modifier = Modifier.fillMaxSize().padding(padding),
            contentAlignment = Alignment.Center,
        ) {
            when (val s = state) {
                is UiState.Loading -> CircularProgressIndicator()
                is UiState.Error -> ErrorView(s.message, onRetry = vm::load)
                is UiState.Success -> VillagerDetail(s.value)
            }
        }
    }
}

@Composable
private fun VillagerDetail(v: Villager) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
    ) {
        Text(v.name, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(24.dp))

        Field("Species", v.species.replaceFirstChar(Char::uppercase))
        Field("Personality", v.personality.replaceFirstChar(Char::uppercase))
        Field("Gender", v.gender.replaceFirstChar(Char::uppercase))
        Field("Birthday", "${monthName(v.birthdayMonth)} ${v.birthdayDay}")
        v.zodiac?.let { Field("Zodiac", it.replaceFirstChar(Char::uppercase)) }
        v.catchphrase?.let { Field("Catchphrase", "\"$it\"") }
        v.hobby?.let { Field("Hobby", it.replaceFirstChar(Char::uppercase)) }
        if (v.games.isNotEmpty()) {
            Field("Appears in", v.games.joinToString(" · ") { it.uppercase() })
        }
    }
}

@Composable
private fun Field(label: String, value: String) {
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp)) {
        Text(
            label.uppercase(),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Text(value, style = MaterialTheme.typography.bodyLarge)
    }
}

private fun monthName(month: Int): String = when (month) {
    1 -> "January"; 2 -> "February"; 3 -> "March"; 4 -> "April"
    5 -> "May"; 6 -> "June"; 7 -> "July"; 8 -> "August"
    9 -> "September"; 10 -> "October"; 11 -> "November"; 12 -> "December"
    else -> "?"
}
