package com.jonasgerdes.stoppelmap.news

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.jonasgerdes.stoppelmap.news.ui.NewsScreen
import kotlinx.serialization.Serializable


@Serializable
data object NewsDestination

fun NavGraphBuilder.newsDestinations(
    onOpenUrl: (String) -> Unit,
) {
    composable<NewsDestination> {
        NewsScreen(
            onUrlTap = onOpenUrl,
        )
    }
}