package com.jonasgerdes.stoppelmap.theme.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun EventRow(
    name: String,
    description: String?,
    locationName: String?,
    selected: Boolean,
    onSelected: () -> Unit,
    isBookmarked: Boolean,
    onNotificationToggle: (active: Boolean) -> Unit,
    modifier: Modifier = Modifier,
    showNotificationToggle: Boolean = true,
    unSelectedBackgroundColor: Color? = null,
    padding: Dp = 16.dp,
) {
    val radius by animateDpAsState(
        targetValue = if (selected) 8.dp else 0.dp
    )

    val backgroundColor by animateColorAsState(
        targetValue =
            if (selected) MaterialTheme.colorScheme.surfaceColorAtElevation(8.dp)
            else unSelectedBackgroundColor ?: MaterialTheme.colorScheme.surfaceColorAtElevation(0.dp)
    )

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
                .padding(start = padding)
                .padding(vertical = padding),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            locationName?.let {
                Text(text = it, style = MaterialTheme.typography.labelMedium)
            }
            Text(text = name, style = MaterialTheme.typography.bodyLarge)
            description?.let {
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
            BookmarkIconButton(
                show = selected || isBookmarked,
                isBookmarked = isBookmarked,
                onBookmarkToggled = onNotificationToggle,
                modifier = Modifier.align(
                    if (description != null) Alignment.Top else Alignment.CenterVertically
                )
            )
        } else {
            Spacer(modifier = Modifier.padding(8.dp))
        }
    }
}

