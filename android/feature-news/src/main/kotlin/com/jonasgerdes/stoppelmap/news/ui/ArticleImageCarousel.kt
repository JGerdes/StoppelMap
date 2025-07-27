package com.jonasgerdes.stoppelmap.news.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.jonasgerdes.stoppelmap.news.R
import com.jonasgerdes.stoppelmap.news.data.model.Image
import com.jonasgerdes.stoppelmap.theme.onScrim

@Composable
fun ArticleImageCarousel(
    images: List<Image>,
    modifier: Modifier = Modifier,
) {
    val imagePagerState =
        rememberPagerState(initialPage = 0, pageCount = {
            images.size
        })
    Box(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(4 / 3f)
    ) {
        var showScrim by remember { mutableStateOf(false) }
        HorizontalPager(
            state = imagePagerState,
            modifier = Modifier
                .fillMaxSize()
        ) { page ->
            val image = images[page]
            val blurHashPainter =
                rememberBlurHashPainter(blurHash = image.blurHash)
            AsyncImage(
                model = image.url,
                contentDescription = image.caption,
                contentScale = ContentScale.Crop,
                placeholder = blurHashPainter,
                error = blurHashPainter,
                onLoading = { showScrim = false },
                onError = { showScrim = false },
                onSuccess = { showScrim = true },
                modifier = Modifier.fillMaxSize()
            )
        }
        if (showScrim) {
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(64.dp)
                    .align(Alignment.BottomCenter)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.scrim.copy(
                                    alpha = 0f
                                ),
                                MaterialTheme.colorScheme.scrim.copy(
                                    alpha = 0.3f
                                ),
                                MaterialTheme.colorScheme.scrim.copy(
                                    alpha = 0.5f
                                ),
                            )
                        )
                    )
            )
        }
        val currentImage by remember {
            derivedStateOf { images[imagePagerState.currentPage] }
        }

        currentImage.copyright?.let { copyright ->
            Text(
                text = stringResource(
                    id = R.string.news_article_card_photo_copyright,
                    copyright
                ),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onScrim,
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(vertical = 8.dp, horizontal = 16.dp)
            )
        }
        if (images.size > 1) {
            PageIndicator(
                pagerState = imagePagerState,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 4.dp)
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
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