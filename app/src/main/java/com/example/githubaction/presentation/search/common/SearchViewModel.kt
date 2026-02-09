package com.example.githubaction.presentation.search.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.githubaction.domain.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SearchViewModel(
    private val repository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<SearchUiState>(SearchUiState.Idle)
    val uiState = _uiState.asStateFlow()

    fun search(query: String) {
        if (query.isBlank()) return

        viewModelScope.launch {
            _uiState.value = SearchUiState.Loading

            try {
                val result = repository.searchUsers(query)
                _uiState.value = SearchUiState.Success(result)
            } catch (e: Exception) {
                _uiState.value = SearchUiState.Error(e.message ?: "Unknown Error")
            }
        }
    }
}