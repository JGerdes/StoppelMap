@file:OptIn(
    ExperimentalLifecycleComposeApi::class, ExperimentalMaterial3Api::class,
    ExperimentalPagerApi::class
)

package com.jonasgerdes.stoppelmap.news.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import com.jonasgerdes.stoppelmap.news.R
import com.jonasgerdes.stoppelmap.theme.onScrim
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.datetime.toJavaLocalDate
import org.koin.androidx.compose.viewModel
import java.time.format.DateTimeFormatter

@Composable
fun NewsScreen(
    onUrlTap: (String) -> Unit,
    modifier: Modifier = Modifier,
    lazyViewModel: Lazy<NewsViewModel> = viewModel(),
) {
    val viewModel by lazyViewModel
    val state by viewModel.state.collectAsStateWithLifecycle()
    Column(
        modifier = modifier
    ) {
        CenterAlignedTopAppBar(
            title = { Text(text = stringResource(id = R.string.news_topbar_title)) }
        )
        val dateFormat = remember {
            DateTimeFormatter.ofPattern("d. MMMM yyyy")
        }
        val listState = rememberLazyListState()

        LazyColumn(
            state = listState,
            contentPadding = PaddingValues(
                start = 16.dp,
                end = 16.dp,
                bottom = 16.dp,
            ),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(
                items = state.articles,
                key = { it.url },
            ) { article ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column {
                        if (article.images.isNotEmpty()) {
                            val imagePagerState = rememberPagerState()
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .aspectRatio(16 / 9f)
                            ) {
                                HorizontalPager(
                                    count = article.images.size,
                                    state = imagePagerState,
                                    modifier = Modifier.fillMaxSize()
                                ) { page ->
                                    val image = article.images[page]
                                    AsyncImage(
                                        model = image.url,
                                        contentDescription = image.caption,
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier.fillMaxSize()
                                    )
                                }
                                Box(
                                    Modifier
                                        .fillMaxWidth()
                                        .height(48.dp)
                                        .align(Alignment.BottomCenter)
                                        .background(
                                            Brush.verticalGradient(
                                                colors = listOf(
                                                    MaterialTheme.colorScheme.scrim.copy(alpha = 0f),
                                                    MaterialTheme.colorScheme.scrim.copy(alpha = 0.4f),
                                                    MaterialTheme.colorScheme.scrim.copy(alpha = 0.6f),
                                                )
                                            )
                                        )
                                )
                                val currentImage by remember {
                                    derivedStateOf { article.images[imagePagerState.currentPage] }
                                }

                                currentImage.author?.let { author ->
                                    Text(
                                        text = stringResource(
                                            id = R.string.news_article_card_photo_copyright,
                                            author
                                        ),
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onScrim,
                                        modifier = Modifier
                                            .align(Alignment.BottomStart)
                                            .padding(vertical = 8.dp, horizontal = 16.dp)
                                    )
                                }
                                if (article.images.size > 1) {
                                    PageIndicator(
                                        pagerState = imagePagerState,
                                        modifier = Modifier
                                            .align(Alignment.BottomCenter)
                                            .padding(bottom = 4.dp)
                                    )
                                }
                            }
                        }
                        Column(
                            modifier = Modifier
                                .padding(horizontal = 16.dp)
                                .padding(top = 16.dp, bottom = 8.dp)
                        ) {
                            Text(
                                text = article.publishDate.toJavaLocalDate().format(dateFormat),
                                style = MaterialTheme.typography.labelSmall
                            )
                            Spacer(modifier = Modifier.size(8.dp))
                            Text(
                                text = article.title,
                                style = MaterialTheme.typography.titleLarge
                            )
                            article.teaser?.let { teaser ->
                                Spacer(modifier = Modifier.size(8.dp))
                                Text(
                                    text = teaser,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                            TextButton(
                                onClick = { onUrlTap(article.url) },
                                modifier = Modifier.align(Alignment.End)
                            ) {
                                Text(text = stringResource(id = R.string.news_article_card_more))
                            }
                        }
                    }
                }
            }
        }

        EndReachedHandler(listState) {
            viewModel.onListEndReached()
        }
    }
}

@Composable
fun PageIndicator(
    pagerState: PagerState,
    modifier: Modifier = Modifier
) {
    val lineHeight = remember { 2.dp }
    val lineWidth = remember { 32.dp }

    Row(modifier, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        (0 until pagerState.pageCount).forEach {
            Box(
                modifier = Modifier
                    .size(width = lineWidth, height = lineHeight)
                    .clip(RoundedCornerShape(100))
                    .background(
                        MaterialTheme.colorScheme.onScrim
                            .copy(alpha = if (it == pagerState.currentPage) 1f else 0.4f)
                    )
            )
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
