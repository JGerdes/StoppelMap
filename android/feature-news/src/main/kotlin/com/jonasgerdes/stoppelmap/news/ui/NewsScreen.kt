@file:OptIn(
    ExperimentalMaterial3Api::class,
)

package com.jonasgerdes.stoppelmap.news.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshState
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jonasgerdes.stoppelmap.news.R
import com.jonasgerdes.stoppelmap.theme.components.LoadingSpinner
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.distinctUntilChanged
import org.koin.androidx.compose.koinViewModel
import java.time.format.DateTimeFormatter
import kotlin.time.Duration.Companion.seconds

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun NewsScreen(
    onUrlTap: (String) -> Unit,
    viewModel: NewsViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = stringResource(id = R.string.news_topbar_title)) },
                scrollBehavior = scrollBehavior,
            )
        },
    ) { padding ->
        val pullToRefreshState: PullToRefreshState = rememberPullToRefreshState()
        PullToRefreshBox(
            isRefreshing = state.isRefreshing,
            onRefresh = viewModel::forceRefresh,
            state = pullToRefreshState,
            indicator = {
                PullToRefreshDefaults.LoadingIndicator(
                    state = pullToRefreshState,
                    isRefreshing = state.isRefreshing,
                    modifier = Modifier.align(Alignment.TopCenter),
                )
            },
            modifier = Modifier.padding(padding)
        ) {

            val dateFormat = remember {
                DateTimeFormatter.ofPattern("d. MMMM yyyy")
            }
            LaunchedEffect(key1 = state.articles.size) {
                // Keep delay here, since we only want mark first as shown after this is
                // in composition for some while
                delay(2.seconds)
                viewModel.onShowFirstArticle()
            }
            val listState = rememberLazyListState()
            LazyColumn(
                state = listState,
                contentPadding = PaddingValues(
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 16.dp,
                ),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier
                    .nestedScroll(scrollBehavior.nestedScrollConnection)
                    .graphicsLayer {
                        translationY =
                            pullToRefreshState.distanceFraction * 1.4f * PullToRefreshDefaults.PositionalThreshold.toPx()
                    }
                    .fillMaxSize()
            ) {
                items(
                    items = state.articles,
                    key = { it.sortKey.value },
                ) { article ->
                    ArticleCard(
                        article = article,
                        dateFormat = dateFormat,
                        onUrlTap = onUrlTap,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                if (state.isLoadingMore) {
                    item {
                        LoadingSpinner(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(128.dp)
                        )
                    }
                }
            }

            EndReachedHandler(listState) {
                viewModel.onListEndReached()
            }
        }
    }
}

@Composable
fun EndReachedHandler(
    listState: LazyListState,
    lastItemOffset: Int = 2,
    onEndReached: () -> Unit
) {
    val endReached = remember {
        derivedStateOf {
            with(listState.layoutInfo) {
                val lastVisibleItemIndex = visibleItemsInfo.lastOrNull()?.index ?: 0
                lastVisibleItemIndex + 1 > (totalItemsCount - lastItemOffset)
            }
        }
    }

    LaunchedEffect(endReached) {
        snapshotFlow { endReached.value }
            .distinctUntilChanged()
            .collect {
                if (it) onEndReached()
            }
    }
}
