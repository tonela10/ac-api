package com.example.acapp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.acapp.data.VillagerRepository
import com.example.acapp.data.dto.Villager
import com.example.acapp.util.UiState
import com.example.acapp.util.toUserFacingMessage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class VillagerDetailViewModel(
    private val repo: VillagerRepository,
    private val villagerId: String,
) : ViewModel() {

    private val _state = MutableStateFlow<UiState<Villager>>(UiState.Loading)
    val state: StateFlow<UiState<Villager>> = _state.asStateFlow()

    init {
        load()
    }

    fun load() {
        _state.value = UiState.Loading
        viewModelScope.launch {
            _state.value = try {
                UiState.Success(repo.getVillager(villagerId))
            } catch (t: Throwable) {
                val detail = t.toUserFacingMessage("Failed to load villager")
                println("[ac-api] getVillager() failed: $detail")
                t.printStackTrace()
                UiState.Error(detail)
            }
        }
    }
}
