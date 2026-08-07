package com.jonasgerdes.stoppelmap.home.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.jonasgerdes.stoppelmap.R
import com.jonasgerdes.stoppelmap.schedule.model.Event
import com.jonasgerdes.stoppelmap.schedule.ui.components.EventCard
import com.jonasgerdes.stoppelmap.theme.StoppelMapTheme
import com.jonasgerdes.stoppelmap.theme.settings.ThemeSetting


@Composable
fun NextOfficialEventCard(
    event: Event,
    onOpenUrl: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    when (event.slug) {
        "stoppelmarkt_feuerwerk" -> FireworkEventCard(event = event, onOpenUrl = onOpenUrl, modifier)
        else -> EventCard(event = event, onOpenUrl = onOpenUrl, modifier = modifier)
    }
}

@Composable
fun FireworkEventCard(
    event: Event,
    onOpenUrl: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    StoppelMapTheme(themeSetting = ThemeSetting.Dark) {
        EventCard(
            event = event,
            headerImage = R.drawable.fireworks,
            onOpenUrl = onOpenUrl,
            modifier = modifier
        )
    }
}
