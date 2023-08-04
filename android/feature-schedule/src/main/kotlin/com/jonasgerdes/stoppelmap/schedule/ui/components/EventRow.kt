package com.jonasgerdes.stoppelmap.schedule.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.NotificationsActive
import androidx.compose.material.icons.rounded.NotificationsNone
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.jonasgerdes.stoppelmap.schedule.model.ScheduleEvent

@Composable
fun EventRow(
    scheduleEvent: ScheduleEvent,
    selected: Boolean,
    onSelected: () -> Unit,
    onNotificationToggle: (active: Boolean) -> Unit,
    modifier: Modifier = Modifier,
    showNotificationToggle: Boolean = false,
) {
    val radius by animateDpAsState(
        targetValue = if (selected) 8.dp else 0.dp
    )

    val backgroundColor by animateColorAsState(
        targetValue =
        if (selected) MaterialTheme.colorScheme.surfaceColorAtElevation(8.dp)
        else MaterialTheme.colorScheme.surfaceColorAtElevation(0.dp)
    )

    val event = scheduleEvent.event

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .clickable {
                onSelected()
            }
            .background(backgroundColor, RoundedCornerShape(radius))
    ) {
        Column(
            Modifier
                .weight(1f)
                .padding(start = 16.dp)
                .padding(vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            event.location?.let {
                Text(text = it, style = MaterialTheme.typography.labelMedium)
            }
            Text(text = event.name, style = MaterialTheme.typography.bodyLarge)
            event.description?.let {
                Text(
                    text = it,
                    maxLines = if (selected) Int.MAX_VALUE else 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.animateContentSize()
                )
            }
        }
        if (showNotificationToggle) {
            if (selected || scheduleEvent.notificationActive) {
                val iconTint by animateColorAsState(
                    targetValue =
                    if (scheduleEvent.notificationActive)
                        MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
                IconButton(
                    onClick = { onNotificationToggle(!scheduleEvent.notificationActive) },
                    modifier = Modifier
                        .padding(8.dp)
                        .align(
                            if (event.description != null) Alignment.Top else Alignment.CenterVertically
                        )
                ) {
                    Icon(
                        if (scheduleEvent.notificationActive) Icons.Rounded.NotificationsActive
                        else Icons.Rounded.NotificationsNone,
                        contentDescription = null,
                        tint = iconTint
                    )
                }
            } else {
                Box(Modifier.minimumInteractiveComponentSize())
            }
        } else {
            Spacer(modifier = Modifier.width(8.dp))
        }
    }
}
