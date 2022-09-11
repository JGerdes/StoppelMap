@file:OptIn(ExperimentalLifecycleComposeApi::class, ExperimentalMaterial3Api::class)

package com.jonasgerdes.stoppelmap.news.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
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
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(
                items = state.articles,
                key = { it.url },
            ) { article ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .padding(top = 16.dp, bottom = 8.dp)
                    ) {
                        Text(
                            text = article.title,
                            style = MaterialTheme.typography.titleLarge
                        )
                        article.teaser?.let { teaser ->
                            Spacer(modifier = Modifier.size(4.dp))
                            Text(
                                text = teaser,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                        TextButton(
                            onClick = { /*TODO*/ },
                            modifier = Modifier.align(Alignment.End)
                        ) {
                            Text(text = stringResource(id = R.string.news_article_card_more))
                        }
                    }
                }
            }
        }
    }
}
