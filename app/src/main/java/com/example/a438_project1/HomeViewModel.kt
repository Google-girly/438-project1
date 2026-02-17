package com.example.a438_project1

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class HomeUiState(
    val isLoading: Boolean = true,
    val artists: List<String> = emptyList(),
    val selectedArtist: String? = null,
    val error: String? = null
)

class HomeViewModel(
    private val artistRepo: ArtistRepository
) : ViewModel() {

    private val _state = MutableStateFlow(HomeUiState())
    val state: StateFlow<HomeUiState> = _state

    init { refreshArtists() }

    fun refreshArtists() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)
            try {
                val artists = artistRepo.loadArtistsFromApi()
                _state.value = HomeUiState(
                    isLoading = false,
                    artists = artists,
                    selectedArtist = artists.firstOrNull()
                )
            } catch (e: Exception) {
                _state.value = HomeUiState(
                    isLoading = false,
                    error = e.message ?: "Failed to load artists"
                )
            }
        }
    }

    fun selectArtist(name: String) {
        _state.value = _state.value.copy(selectedArtist = name)
    }

    fun randomArtist() {
        val list = _state.value.artists
        if (list.isNotEmpty()) _state.value = _state.value.copy(selectedArtist = list.random())
    }
}