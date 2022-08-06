@file:OptIn(ExperimentalPagerApi::class, ExperimentalAnimationApi::class)

package com.jonasgerdes.stoppelmap.countdown.ui.components.settings

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowForward
import androidx.compose.material.icons.rounded.Save
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.jonasgerdes.stoppelmap.countdown.R
import kotlinx.coroutines.launch


@Composable
fun WidgetSettingsPager(
    onSave: () -> Unit,
    settingsCards: List<@Composable (Modifier) -> Unit>,
    modifier: Modifier = Modifier
) {
    val pagerState = rememberPagerState()
    val scope = rememberCoroutineScope()
    val isLastPage = pagerState.currentPage == pagerState.pageCount - 1
    Column(modifier = modifier) {
        Spacer(modifier = Modifier.size(16.dp))
        HorizontalPager(
            count = settingsCards.size,
            state = pagerState,
            contentPadding = PaddingValues(16.dp),
            itemSpacing = 16.dp,
            verticalAlignment = Alignment.Bottom
        ) { page ->
            Box {
                settingsCards[page](Modifier.padding(top = 28.dp))
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(end = 16.dp)
                ) {
                    WidgetSettingsPageFab(
                        isVisible = !pagerState.isScrollInProgress,
                        isLastPage = isLastPage,
                        onNext = {
                            scope.launch {
                                pagerState.animateScrollToPage(pagerState.currentPage + 1)
                            }
                        },
                        onSave = onSave
                    )
                }
            }
        }
    }
}


@Composable
private fun WidgetSettingsPageFab(
    isVisible: Boolean,
    isLastPage: Boolean,
    onNext: () -> Unit,
    onSave: () -> Unit,

    ) {
    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn() + scaleIn(),
        exit = scaleOut() + fadeOut(),
    ) {
        FloatingActionButton(
            onClick = {
                if (isLastPage) onSave() else onNext()
            },
            shape = CircleShape,
        ) {
            if (isLastPage) {
                Icon(
                    Icons.Rounded.Save,
                    contentDescription = stringResource(
                        id = R.string.widget_configuration_button_save_contentDescription
                    )
                )
            } else {
                Icon(
                    Icons.Rounded.ArrowForward,
                    contentDescription = stringResource(
                        id = R.string.widget_configuration_button_next_contentDescription
                    )
                )
            }
        }
    }
}
