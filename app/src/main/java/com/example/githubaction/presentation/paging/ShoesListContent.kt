package com.example.githubaction.presentation.paging

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemKey
import com.example.githubaction.domain.model.ShoesModel

@Composable
fun ShoesListContent(
    pagingItems: LazyPagingItems<ShoesModel>,
    modifier: Modifier = Modifier
) {
    when (val refreshState = pagingItems.loadState.refresh) {
        is LoadState.Loading -> {
            // 초기 로딩 중: 전체 화면 스피너
            Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(modifier = Modifier.testTag("progress_bar_center"))
            }
        }
        is LoadState.Error -> {
            // 초기 로딩 실패: 전체 화면 에러 메시지 및 재시도 버튼
            ErrorScreen(
                message = refreshState.error.localizedMessage ?: "Unknown Error",
                onRetryClick = { pagingItems.retry() }
            )
        }
        is LoadState.NotLoading -> {
            // 데이터 로드 완료: 리스트 표시
            if (pagingItems.itemCount == 0) {
                // 데이터가 없는 경우 (Empty State)
                Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = "등록된 신발이 없습니다.", modifier = Modifier.testTag("empty_state_text"))
                }
            } else {
                ShoesLazyColumn(pagingItems = pagingItems, modifier = modifier)
            }
        }
    }
}

@Composable
private fun ShoesLazyColumn(
    pagingItems: LazyPagingItems<ShoesModel>,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.testTag("shoes_lazy_column"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // 실제 데이터 아이템들
        items(
            count = pagingItems.itemCount,
            key = pagingItems.itemKey { it.id } // key 설정은 성능과 애니메이션에 중요
        ) { index ->
            val item = pagingItems[index]
            if (item != null) {
                ShoesItem(shoes = item)
            } else {
                // Placeholders (옵션)
                // PlaceholderItem()
            }
        }

        // 하단 추가 로딩(Append) 상태 처리
        item {
            when (val appendState = pagingItems.loadState.append) {
                is LoadState.Loading -> {
                    // 리스트 끝에서 로딩 중
                    Box(modifier = Modifier.fillMaxWidth().padding(8.dp), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(modifier = Modifier.testTag("progress_bar_append"))
                    }
                }
                is LoadState.Error -> {
                    // 리스트 끝에서 에러 발생
                    ErrorItem(
                        message = "추가 로드 실패",
                        onRetryClick = { pagingItems.retry() }
                    )
                }
                else -> {}
            }
        }
    }
}

// 개별 아이템 UI
@Composable
fun ShoesItem(shoes: ShoesModel, modifier: Modifier = Modifier) {
    Card(modifier = modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = shoes.id, style = MaterialTheme.typography.titleMedium)
            Text(text = "${shoes.id}원", style = MaterialTheme.typography.bodyMedium)
        }
    }
}

// 전체 화면 에러 UI
@Composable
fun ErrorScreen(message: String, onRetryClick: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.testTag("error_screen"),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = message)
        Button(onClick = onRetryClick, modifier = Modifier.testTag("retry_button_center")) {
            Text("다시 시도")
        }
    }
}

// 리스트 하단 에러 아이템 UI
@Composable
fun ErrorItem(message: String, onRetryClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(8.dp).testTag("error_item_append"),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = message)
        Button(onClick = onRetryClick, modifier = Modifier.testTag("retry_button_append")) {
            Text("Retry")
        }
    }
}