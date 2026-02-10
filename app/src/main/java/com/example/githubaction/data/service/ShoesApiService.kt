package com.example.githubaction.data.service

import com.example.githubaction.data.dto.ShoesDTO
import com.example.githubaction.data.dto.ShoesResponse

interface ShoesApiService {
    suspend fun getShoesList(
        page: Int,
        size: Int
    ): ShoesResponse
}