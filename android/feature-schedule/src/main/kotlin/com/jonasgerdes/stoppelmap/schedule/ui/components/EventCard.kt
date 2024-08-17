package com.jonasgerdes.stoppelmap.schedule.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.jonasgerdes.stoppelmap.schedule.model.Event
import com.jonasgerdes.stoppelmap.theme.i18n.localizedString
import kotlinx.datetime.toJavaLocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun EventCard(
    event: Event,
    modifier: Modifier = Modifier,
    onBookmarkToggle: ((isBookmarked: Boolean) -> Unit)? = null,
    @DrawableRes headerImage: Int? = null,
) {
    Card(modifier = modifier.fillMaxWidth()) {
        if (headerImage != null) {
            Image(
                painter = painterResource(id = headerImage),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(2.4f),
            )
        }
        val timeFormatter = remember {
            DateTimeFormatter.ofPattern(
                "EEE, HH:mm",
                Locale.GERMAN
            )
        }
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.padding(start = 16.dp, bottom = 16.dp)
        ) {
            Row(horizontalArrangement = Arrangement.SpaceBetween) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier
                        .weight(1f)
                        .padding(top = 16.dp)
                ) {
                    event.locationName?.let {
                        Text(text = it, style = MaterialTheme.typography.labelMedium)
                    }
                    Text(
                        text = event.start.toJavaLocalDateTime().format(timeFormatter),
                        style = MaterialTheme.typography.labelLarge
                    )
                    Text(text = localizedString(event.name), style = MaterialTheme.typography.titleLarge)
                }
                BookmarkIconButton(
                    isBookmarked = event.isBookmarked,
                    onBookmarkToggled = onBookmarkToggle ?: {},
                    show = onBookmarkToggle != null,
                )
            }
            event.description?.let {
                Text(
                    text = localizedString(it),
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(end = 16.dp)
                )
            }
        }
    }
}