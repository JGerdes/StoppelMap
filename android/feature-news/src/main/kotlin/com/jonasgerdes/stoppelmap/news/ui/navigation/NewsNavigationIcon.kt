package com.jonasgerdes.stoppelmap.news.ui.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Newspaper
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel


@Composable
fun NewsNavigationIcon(modifier: Modifier = Modifier) {
    val viewModel: NewsNavigationIconViewModel = koinViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()
    BadgedBox(badge = {
        AnimatedVisibility(
            visible = state.unreadArticles > 0,
            enter = fadeIn() + scaleIn(),
            exit = fadeOut() + scaleOut(),
        ) {
            Badge {
                Text(text = state.unreadArticles.toString())
            }
        }
    }) {
        Icon(imageVector = Icons.Rounded.Newspaper, contentDescription = null)
    }
}