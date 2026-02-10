package com.example.githubaction.search

import androidx.paging.PagingSource
import com.example.githubaction.data.dto.ShoesDTO
import com.example.githubaction.data.dto.ShoesResponse
import com.example.githubaction.data.mapper.asDomain
import com.example.githubaction.data.service.ShoesApiService
import com.example.githubaction.data.source.ShoesPagingSource
import com.example.githubaction.domain.model.ShoesModel
import com.example.githubaction.domain.repository.ShoesRepository
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.runTest
import org.junit.Test

class ShoesRepositoryTest {
    private val mockApi = mockk<ShoesApiService>()
    private lateinit var repository: ShoesRepository


    @Test
    fun `Repository flow delivers success data from API`() = runTest {
        // GIVEN
        val fakeShoes = listOf<ShoesDTO>(ShoesDTO("1"))
        coEvery { mockApi.getShoesList(any(), any()) } returns ShoesResponse(
            items = fakeShoes,
            0,
            20,
            100,
            4
        )

        val pagingSource = ShoesPagingSource(mockApi)

        // When
        val result = pagingSource.load(
            PagingSource.LoadParams.Refresh(key = null, loadSize = 20, placeholdersEnabled = false)
        )
        val expectedModels = fakeShoes.map { it.asDomain() }

        // Then
        assertTrue(result is PagingSource.LoadResult.Page)
        assertEquals(expectedModels, (result as PagingSource.LoadResult.Page).data)
    }
}