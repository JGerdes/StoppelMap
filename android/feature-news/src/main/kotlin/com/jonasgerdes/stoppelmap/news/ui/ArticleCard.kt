package com.jonasgerdes.stoppelmap.news.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.jonasgerdes.stoppelmap.news.R
import com.jonasgerdes.stoppelmap.news.data.model.Article
import kotlinx.datetime.toJavaLocalDate
import java.time.format.DateTimeFormatter

@Composable
fun ArticleCard(
    article: Article,
    dateFormat: DateTimeFormatter,
    onUrlTap: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(modifier = modifier) {
        Column {

            if (article.images.isNotEmpty()) {
                ArticleImageCarousel(images = article.images)
            }
            Column(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(top = 16.dp, bottom = 8.dp)
                    .fillMaxWidth()
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
                Spacer(modifier = Modifier.size(8.dp))
                Text(
                    text = article.teaser,
                    style = MaterialTheme.typography.bodyMedium
                )
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