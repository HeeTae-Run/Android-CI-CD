package com.example.githubaction.search

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.example.githubaction.domain.model.User
import com.example.githubaction.domain.repository.UserRepository
import com.example.githubaction.presentation.search.SearchScreen
import com.example.githubaction.presentation.search.common.SearchViewModel
import kotlinx.coroutines.delay
import org.junit.Rule
import org.junit.Test

class SearchScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun `검색_버튼을_누르면_로딩이_표시된다`() {
        // GIVEN: 테스트용 ViewModel (또는 실제 VM에 가짜 Repo 주입)
        val fakeRepo = object : UserRepository {
            override suspend fun searchUsers(query: String): List<User> {
                delay(1000) // 로딩을 보여주기 위해 1초 지연
                return emptyList()
            }
        }
        val viewModel = SearchViewModel(fakeRepo)

        // 화면 그리기
        composeTestRule.setContent {
            SearchScreen(viewModel = viewModel)
        }
        // WHEN: 텍스트 입력 후 버튼 클릭
        composeTestRule.onNodeWithTag("search_input").performTextInput("Kotlin")
        composeTestRule.onNodeWithTag("search_button").performClick()

        // Then: 로딩 인디케이터가 보이는지 확인
        composeTestRule.onNodeWithTag("loading_indicator").assertIsDisplayed()

    }
}