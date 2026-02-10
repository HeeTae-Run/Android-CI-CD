package com.example.githubaction.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.githubaction.data.service.ShoesApiService
import com.example.githubaction.data.source.ShoesPagingSource
import com.example.githubaction.domain.model.ShoesModel
import com.example.githubaction.domain.repository.ShoesRepository
import kotlinx.coroutines.flow.Flow

class ShoesRepositoryImpl(
    private val apiService: ShoesApiService
) : ShoesRepository {
    override fun getShoesStream(): Flow<PagingData<ShoesModel>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false,
                initialLoadSize = 20
            ),
            pagingSourceFactory = { ShoesPagingSource(apiService) }
        ).flow
    }

}