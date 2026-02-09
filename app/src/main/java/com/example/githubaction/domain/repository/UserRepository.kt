package com.example.githubaction.domain.repository

import com.example.githubaction.domain.model.User

interface UserRepository {
    suspend fun searchUsers(query: String): List<User>
}