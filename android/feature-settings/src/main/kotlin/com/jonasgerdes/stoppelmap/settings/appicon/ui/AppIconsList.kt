package com.jonasgerdes.stoppelmap.settings.appicon.ui

import android.graphics.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement.Absolute.spacedBy
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Apps
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.createBitmap
import com.jonasgerdes.stoppelmap.settings.R
import com.jonasgerdes.stoppelmap.settings.appicon.AppIcon

@Composable
fun AppIconsList(
    modifier: Modifier = Modifier,
    icons: List<AppIcon>,
    onIconSelect: (appIcon: AppIcon) -> Unit,
) {
    ListItem(
        modifier = modifier,
        leadingContent = { Icon(Icons.Rounded.Apps, null) },
        headlineContent = { Text(stringResource(R.string.settings_appIcon_title)) },
        supportingContent = {
            FlowRow(
                horizontalArrangement = spacedBy(8.dp),
                verticalArrangement = spacedBy(8.dp),
                modifier = Modifier.padding(top = 8.dp)
            ) {
                icons.forEach { icon ->
                    val theme = LocalContext.current.theme
                    val resources = LocalContext.current.resources
                    val bitmap = remember(icon.drawable, theme) {
                        ResourcesCompat.getDrawable(resources, icon.drawable, theme)?.let { drawable ->
                            createBitmap(
                                drawable.intrinsicWidth,
                                drawable.intrinsicHeight
                            ).also { bitmap ->
                                val canvas = Canvas(bitmap)
                                drawable.setBounds(0, 0, canvas.width, canvas.height)
                                drawable.draw(canvas)
                            }
                        }
                    }
                    if (bitmap != null) {
                        FilterChip(
                            selected = icon.selected,
                            onClick = { onIconSelect(icon) },
                            label = {
                                Image(bitmap.asImageBitmap(), null, modifier = Modifier.padding(8.dp))
                            }
                        )
                    }
                }
            }
        })
}