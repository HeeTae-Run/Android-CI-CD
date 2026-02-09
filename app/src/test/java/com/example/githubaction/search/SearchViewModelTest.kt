package com.example.githubaction.search

import com.example.githubaction.domain.model.User
import com.example.githubaction.domain.repository.UserRepository
import com.example.githubaction.presentation.search.common.SearchUiState
import com.example.githubaction.presentation.search.common.SearchViewModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SearchViewModelTest {
    private lateinit var viewModel: SearchViewModel
    private val repository: UserRepository = mockk()


    private val testDispatcher = UnconfinedTestDispatcher()


    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = SearchViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `search_호출시_데이터를_가져오면_Success_상태가_된다`() = runTest {
        // GIVEN (상황 설정)
        val query = "Android"
        val expectedUser = listOf<User>(User("1", "Android Dev"))

        // 가짜 레포지토리가 Android 검색 시 expectedUsers를 반환하도록 조작
        coEvery {
            repository.searchUsers(query)
        } returns expectedUser

        // when (실행)
        viewModel.search(query)

        // Then (검증)
        // 1. 상태가 Success인지 확인
        val currentState = viewModel.uiState.value
        assert(currentState is SearchUiState.Success)

        // 2. 데이터가 맞는지 확인
        assertEquals(expectedUser, (currentState as SearchUiState.Success).users)

        coVerify { repository.searchUsers(query) }
    }

    @Test
    fun `search_호출시_네트워크_에러가_발생하면_Error_상태가_된다`() = runTest {
        // Given: (준비: 레포지토리가 예외를 던지도록 조작)
        val query = "BuggyQuery"
        val errorMessage = "인터넷 연결을 확인해주세요."

        // coEvery가 특정 예외를 던지게 설정 (throws)
        coEvery { repository.searchUsers(query) } throws RuntimeException(errorMessage)

        // When (실행)
        viewModel.search(query)

        // Then (없음)
        val currentState = viewModel.uiState.value

        // 1. 상태가 Error타입인지 확인
        assert(currentState is SearchUiState.Error)

        // 2. 에러 메시지가 제대로 전달되었는지 확인 (Smart Cast 활용)
        // currentState가 Error 타입임을 확인되었으므로 .message에 접근
        assertEquals(errorMessage, (currentState as SearchUiState.Error).message)
    }
}