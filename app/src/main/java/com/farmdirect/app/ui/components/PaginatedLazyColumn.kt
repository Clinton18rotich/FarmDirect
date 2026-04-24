package com.farmdirect.app.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.farmdirect.app.ui.theme.*

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun <T> PaginatedLazyColumn(
    items: List<T>,
    isLoading: Boolean,
    isRefreshing: Boolean,
    hasMorePages: Boolean,
    onRefresh: () -> Unit,
    onLoadMore: () -> Unit,
    itemContent: @Composable (T) -> Unit,
    emptyContent: @Composable () -> Unit = { EmptyStateView(emoji = "📭", title = "No items found") },
    modifier: Modifier = Modifier,
    listState: LazyListState = rememberLazyListState()
) {
    val pullRefreshState = rememberPullRefreshState(
        refreshing = isRefreshing,
        onRefresh = onRefresh
    )
    
    // Detect when scrolled to bottom
    val shouldLoadMore = remember {
        derivedStateOf {
            val lastVisibleItem = listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            val totalItems = listState.layoutInfo.totalItemsCount
            lastVisibleItem >= totalItems - 3 && !isLoading && hasMorePages && items.isNotEmpty()
        }
    }
    
    LaunchedEffect(shouldLoadMore.value) {
        if (shouldLoadMore.value) onLoadMore()
    }
    
    Box(modifier = modifier.pullRefresh(pullRefreshState)) {
        if (items.isEmpty() && !isLoading) {
            emptyContent()
        } else {
            LazyColumn(
                state = listState,
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                items(items, key = { (it as? Any)?.hashCode() ?: 0 }) { item ->
                    itemContent(item)
                }
                
                if (isLoading && items.isNotEmpty()) {
                    item {
                        Box(modifier = Modifier.fillMaxWidth().padding(16.dp), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator(color = PrimaryGreen, modifier = Modifier.size(32.dp))
                        }
                    }
                }
                
                if (!hasMorePages && items.isNotEmpty()) {
                    item {
                        Box(modifier = Modifier.fillMaxWidth().padding(16.dp), contentAlignment = Alignment.Center) {
                            Text("— You've reached the end —", color = TextSecondary, style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            }
        }
        
        PullRefreshIndicator(
            refreshing = isRefreshing,
            state = pullRefreshState,
            modifier = Modifier.align(Alignment.TopCenter),
            contentColor = PrimaryGreen
        )
    }
}
