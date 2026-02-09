package com.example.githubaction.presentation.search.common

import com.example.githubaction.domain.model.User

sealed interface SearchUiState {
    object Idle : SearchUiState
    object Loading : SearchUiState
    data class Success(val users: List<User>) : SearchUiState
    data class Error(val message: String) : SearchUiState
}