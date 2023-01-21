package com.jonasgerdes.stoppelmap.schedule.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.jonasgerdes.stoppelmap.R
import com.jonasgerdes.stoppelmap.data.Event
import com.jonasgerdes.stoppelmap.theme.StoppelMapTheme
import java.time.format.DateTimeFormatter
import java.util.*


@Composable
fun NextOfficialEventCard(
    event: Event,
    modifier: Modifier = Modifier,
) {
    when (event.slug) {
        "stoppelmarkt_feuerwerk" -> FireworkEventCard(event = event, modifier)
        else -> EventCard(event = event, modifier = modifier)
    }
}

@Composable
fun FireworkEventCard(
    event: Event,
    modifier: Modifier = Modifier
) {
    StoppelMapTheme(darkTheme = true) {
        EventCard(
            event = event,
            headerImage = R.drawable.fireworks,
            modifier = modifier
        )
    }
}

@Composable
private fun EventCard(
    event: Event,
    modifier: Modifier = Modifier,
    @DrawableRes headerImage: Int? = null,
) {
    Column(
        Modifier.fillMaxWidth()
    ) {
        Text(
            text = stringResource(id = R.string.home_officalEventCard_title),
        )
        Spacer(modifier = Modifier.size(8.dp))
        Card(modifier = Modifier.fillMaxWidth()) {
            if (headerImage != null) {
                Image(
                    painter = painterResource(id = R.drawable.fireworks),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(2.4f),
                )
            }
            Box(modifier = Modifier.padding(16.dp)) {
                val formatter =
                    remember {
                        DateTimeFormatter.ofPattern(
                            "EEE, HH:mm",
                            Locale.GERMAN
                        )
                    }
                EventRow(
                    event = event,
                    timeFormatter = formatter,
                    showDivider = false,
                    modifier = modifier
                        .background(
                            CardDefaults
                                .cardColors()
                                .containerColor(
                                    enabled = true
                                ).value
                        )
                        .fillMaxWidth()
                )
            }
        }
    }
}
