package com.example.githubaction.paging

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.paging.LoadState
import androidx.paging.LoadStates
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.githubaction.domain.model.ShoesModel
import com.example.githubaction.presentation.paging.ShoesListContent
import kotlinx.coroutines.flow.flowOf
import org.junit.Rule
import org.junit.Test

class ShoesScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val fakeShoes = listOf(
        ShoesModel("1"),
        ShoesModel("2")
    )

    @Test
    fun showsData_whenLoadSuccess() {
        // 1. 로딩이 이미 "완료된(NotLoading)" 상태를 정의합니다.
        val successState = LoadStates(
            refresh = LoadState.NotLoading(endOfPaginationReached = false),
            prepend = LoadState.NotLoading(endOfPaginationReached = false),
            append = LoadState.NotLoading(endOfPaginationReached = false)
        )

        // 2. 데이터와 함께 상태를 주입하여 PagingData를 생성합니다.
        val flow = flowOf(
            PagingData.from(
                data = fakeShoes,
                sourceLoadStates = successState // ★ 핵심: 상태 강제 주입
            )
        )

        composeTestRule.setContent {
            val items = flow.collectAsLazyPagingItems()
            ShoesListContent(pagingItems = items)
        }

        // 3. UI가 안정화될 때까지 잠시 기다리거나, 상태 주입 덕분에 바로 통과됩니다.
        // 만약 그래도 안 된다면 waitForIdle()을 추가할 수 있습니다.
        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithTag("shoes_lazy_column").assertIsDisplayed()
        composeTestRule.onNodeWithText("1").assertIsDisplayed()
        composeTestRule.onNodeWithText("2").assertIsDisplayed()
    }

    @Test
    fun showsAppendLoading_whenScrollingToEnd() {
        // Given: 데이터가 있고, 하단 추가 로딩(Append) 중인 상태
        val appendLoadingFlow = flowOf(
            PagingData.from(
                data = fakeShoes,
                sourceLoadStates = LoadStates(
                    refresh = LoadState.NotLoading(false),
                    append = LoadState.Loading, // 핵심: Append가 Loading 상태
                    prepend = LoadState.NotLoading(false)
                )
            )
        )

        composeTestRule.setContent {
            val items = appendLoadingFlow.collectAsLazyPagingItems()
            ShoesListContent(pagingItems = items)
        }

        composeTestRule.onNodeWithTag("progress_bar_append").assertIsDisplayed()
    }
}