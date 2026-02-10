package com.example.githubaction.domain.repository

import androidx.paging.PagingData
import com.example.githubaction.domain.model.ShoesModel
import kotlinx.coroutines.flow.Flow

interface ShoesRepository {
    fun getShoesStream(): Flow<PagingData<ShoesModel>>
}