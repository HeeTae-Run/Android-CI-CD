package com.example.githubaction.data.source

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.githubaction.data.mapper.asDomain
import com.example.githubaction.data.service.ShoesApiService
import com.example.githubaction.domain.model.ShoesModel

class ShoesPagingSource(
    private val apiService: ShoesApiService
) : PagingSource<Int, ShoesModel>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ShoesModel> {
        val page = params.key ?: 1
        return try {
            val response = apiService.getShoesList(page, params.loadSize)
            LoadResult.Page(
                data = response.items.map { it.asDomain() },
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (response.items.isEmpty()) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, ShoesModel>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}