package com.example.acapp.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.acapp.data.VillagerRepository
import com.example.acapp.data.dto.Villager
import com.example.acapp.ui.components.AcChip
import com.example.acapp.ui.theme.AcColors
import com.example.acapp.ui.theme.LeafBackground
import com.example.acapp.util.UiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VillagersListScreen(
    repo: VillagerRepository,
    onVillagerClick: (String) -> Unit,
) {
    val vm: VillagersListViewModel = viewModel { VillagersListViewModel(repo) }
    val state by vm.state.collectAsStateWithLifecycle()

    LeafBackground {
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            text = "🌿  Villagers",
                            style = MaterialTheme.typography.headlineSmall,
                            color = AcColors.Leaf,
                        )
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
                    is UiState.Loading -> LoadingView()
                    is UiState.Error -> ErrorView(s.message, onRetry = vm::load)
                    is UiState.Success -> VillagerList(s.value, onVillagerClick)
                }
            }
        }
    }
}

@Composable
private fun VillagerList(villagers: List<Villager>, onClick: (String) -> Unit) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        items(items = villagers, key = { it.id }) { villager ->
            VillagerRow(villager, onClick = { onClick(villager.id) })
        }
    }
}

@Composable
private fun VillagerRow(villager: Villager, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(containerColor = AcColors.Paper),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            VillagerAvatar(villager)
            Spacer(Modifier.size(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = villager.name,
                    style = MaterialTheme.typography.titleMedium,
                    color = AcColors.Leaf,
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    text = villager.species.replaceFirstChar(Char::uppercase),
                    style = MaterialTheme.typography.bodyMedium,
                    color = AcColors.LeafSoft,
                )
            }
            AcChip(text = villager.personality)
        }
    }
}

@Composable
private fun LoadingView() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        CircularProgressIndicator(color = AcColors.Mint)
        Spacer(Modifier.height(16.dp))
        Text(
            text = "Buscando aldeanos…",
            style = MaterialTheme.typography.bodyMedium,
            color = AcColors.LeafSoft,
        )
    }
}

@Composable
internal fun ErrorView(message: String, onRetry: () -> Unit) {
    Card(
        modifier = Modifier.padding(24.dp),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(containerColor = AcColors.Paper),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "🥲  Algo salió mal",
                style = MaterialTheme.typography.titleMedium,
                color = AcColors.Leaf,
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                color = AcColors.LeafSoft,
            )
            Spacer(Modifier.height(16.dp))
            Button(
                onClick = onRetry,
                colors = ButtonDefaults.buttonColors(
                    containerColor = AcColors.Mint,
                    contentColor = Color.White,
                ),
                shape = MaterialTheme.shapes.medium,
            ) {
                Text("Reintentar")
            }
        }
    }
}
