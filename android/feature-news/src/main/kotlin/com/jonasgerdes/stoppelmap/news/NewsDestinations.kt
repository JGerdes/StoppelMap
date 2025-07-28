package com.jonasgerdes.stoppelmap.news

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.jonasgerdes.stoppelmap.news.ui.NewsScreen
import com.jonasgerdes.stoppelmap.news.ui.navigation.NewsNavigationIcon
import com.jonasgerdes.stoppelmap.theme.navigation.NavigationTab
import kotlinx.serialization.Serializable


@Serializable
data object NewsDestination

val newsNavigationTab = NavigationTab(
    iconComposable = { NewsNavigationIcon() },
    label = R.string.main_bottom_nav_item_news,
    startDestination = NewsDestination
)

fun NavGraphBuilder.newsDestinations(
    onOpenUrl: (String) -> Unit,
) {
    composable<NewsDestination> {
        NewsScreen(
            onUrlTap = onOpenUrl,
        )
    }
}