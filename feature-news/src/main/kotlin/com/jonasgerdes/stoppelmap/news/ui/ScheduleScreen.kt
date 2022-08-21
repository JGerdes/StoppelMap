@file:OptIn(ExperimentalLifecycleComposeApi::class, ExperimentalMaterial3Api::class)

package com.jonasgerdes.stoppelmap.news.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jonasgerdes.stoppelmap.news.R
import org.koin.androidx.compose.viewModel

@Composable
fun NewsScreen(
    modifier: Modifier = Modifier,
    lazyViewModel: Lazy<NewsViewModel> = viewModel()
) {
    val viewModel by lazyViewModel
    val state by viewModel.state.collectAsStateWithLifecycle()
    Column(
        modifier = modifier
    ) {
        CenterAlignedTopAppBar(
            title = { Text(text = stringResource(id = R.string.news_topbar_title)) }
        )
    }
}
