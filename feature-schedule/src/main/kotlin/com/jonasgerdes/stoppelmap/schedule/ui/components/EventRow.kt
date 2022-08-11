package com.jonasgerdes.stoppelmap.schedule.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material.icons.rounded.Schedule
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.jonasgerdes.stoppelmap.data.Event
import com.jonasgerdes.stoppelmap.schedule.R
import kotlinx.datetime.toJavaLocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun EventRow(
    event: Event,
    timeFormatter: DateTimeFormatter,
    modifier: Modifier = Modifier,
    showDivider: Boolean = true,
) {
    Column(
        modifier = modifier
    ) {
        Text(
            text = event.name,
            style = MaterialTheme.typography.titleLarge
        )
        Spacer(modifier = Modifier.size(4.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                Icons.Rounded.Schedule,
                contentDescription = stringResource(R.string.schedule_startTime_contentDescription)
            )
            Spacer(modifier = Modifier.size(8.dp))
            Text(
                text = event.start.toJavaLocalDateTime()
                    .format(timeFormatter)
            )
        }
        if (event.location != null) {
            Spacer(modifier = Modifier.size(4.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Rounded.LocationOn,
                    contentDescription = stringResource(R.string.schedule_location_contentDescription)
                )
                Spacer(modifier = Modifier.size(8.dp))
                Text(text = event.location!!)
            }
        }
        if (event.description != null) {
            Spacer(modifier = Modifier.size(4.dp))
            Text(
                text = event.description!!,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(
                    start = 32.dp,
                )
            )
        }
        if (showDivider) {
            Spacer(modifier = Modifier.size(8.dp))
            Divider()
        }
    }
}
