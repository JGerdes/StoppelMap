package com.jonasgerdes.stoppelmap.schedule.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Bookmark
import androidx.compose.material.icons.rounded.BookmarkBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.jonasgerdes.stoppelmap.schedule.R

@Composable
fun BookmarkIconButton(
    isBookmarked: Boolean,
    onBookmarkToggled: (active: Boolean) -> Unit,
    modifier: Modifier = Modifier,
    show: Boolean = true,
) {
    if (show) {
        val iconTint by animateColorAsState(
            targetValue =
            if (isBookmarked) MaterialTheme.colorScheme.primary
            else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )
        IconButton(
            onClick = { onBookmarkToggled(!isBookmarked) },
            modifier = modifier.padding(8.dp)
        ) {
            Icon(
                if (isBookmarked) Icons.Rounded.Bookmark
                else Icons.Rounded.BookmarkBorder,
                contentDescription = stringResource(
                    if (isBookmarked) R.string.schedule_bookmark_unsave_contentDescription
                    else R.string.schedule_bookmark_save_contentDescription
                ),
                tint = iconTint
            )
        }
    } else {
        Box(Modifier.minimumInteractiveComponentSize())
    }
}