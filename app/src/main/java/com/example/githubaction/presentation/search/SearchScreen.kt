package com.example.githubaction.presentation.search

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.example.githubaction.presentation.search.common.SearchUiState
import com.example.githubaction.presentation.search.common.SearchViewModel

@Composable
fun SearchScreen(
    viewModel: SearchViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    var query by remember { mutableStateOf("") }

    Column {
        TextField(
            value = query,
            onValueChange = { query = it },
            modifier = Modifier.testTag("search_input") // UI 테스트 식별자
        )
        Button(
            onClick = { viewModel.search(query) },
            modifier = Modifier.testTag("search_button")
        ) {
            Text("검색")
        }

        // 상태에 따른 UI 분기
        when (uiState) {
            is SearchUiState.Loading -> CircularProgressIndicator(Modifier.testTag("loading_indicator"))
            is SearchUiState.Success -> {
                LazyColumn(Modifier.testTag("result_list")) {
                    // 리스트 아이템 그리기...
                }
            }
            else -> {}
        }
    }
}