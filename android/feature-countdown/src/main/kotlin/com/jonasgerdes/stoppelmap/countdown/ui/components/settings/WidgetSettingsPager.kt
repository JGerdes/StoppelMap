@file:OptIn(ExperimentalAnimationApi::class)

package com.jonasgerdes.stoppelmap.countdown.ui.components.settings

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
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
import com.jonasgerdes.stoppelmap.countdown.R
import kotlinx.coroutines.launch


@Composable
fun WidgetSettingsPager(
    onSave: () -> Unit, settingsCards: List<@Composable (Modifier) -> Unit>, modifier: Modifier = Modifier
) {
    val pagerState = rememberPagerState(pageCount = { settingsCards.size })
    val scope = rememberCoroutineScope()
    val isLastPage = pagerState.currentPage == pagerState.pageCount - 1
    Column(modifier = modifier) {
        Spacer(modifier = Modifier.size(16.dp))
        HorizontalPager(
            state = pagerState,
            contentPadding = PaddingValues(vertical = 16.dp, horizontal = 8.dp),
            verticalAlignment = Alignment.Bottom
        ) { page ->
            Box(Modifier.padding(horizontal = 8.dp)) {
                settingsCards[page](Modifier.padding(top = 28.dp))
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(end = 16.dp)
                ) {
                    WidgetSettingsPageFab(
                        isVisible = !pagerState.isScrollInProgress, isLastPage = isLastPage, onNext = {
                            scope.launch {
                                pagerState.animateScrollToPage(pagerState.currentPage + 1)
                            }
                        }, onSave = onSave
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
                    Icons.Rounded.Save, contentDescription = stringResource(
                        id = R.string.widget_configuration_button_save_contentDescription
                    )
                )
            } else {
                Icon(
                    Icons.Rounded.ArrowForward, contentDescription = stringResource(
                        id = R.string.widget_configuration_button_next_contentDescription
                    )
                )
            }
        }
    }
}
