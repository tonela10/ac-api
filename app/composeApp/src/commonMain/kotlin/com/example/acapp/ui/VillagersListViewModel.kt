package com.example.acapp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.acapp.data.VillagerRepository
import com.example.acapp.data.dto.Villager
import com.example.acapp.util.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class VillagersListViewModel(
    private val repo: VillagerRepository,
) : ViewModel() {

    private val _state = MutableStateFlow<UiState<List<Villager>>>(UiState.Loading)
    val state: StateFlow<UiState<List<Villager>>> = _state.asStateFlow()

    init {
        load()
    }

    fun load() {
        _state.value = UiState.Loading
        viewModelScope.launch {
            _state.value = try {
                UiState.Success(repo.listVillagers())
            } catch (t: Throwable) {
                UiState.Error(t.message ?: "Failed to load villagers")
            }
        }
    }
}
