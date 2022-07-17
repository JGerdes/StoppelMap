@file:OptIn(ExperimentalMaterial3Api::class)

package com.jonasgerdes.stoppelmap.countdown.ui.components.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jonasgerdes.stoppelmap.countdown.R
import com.jonasgerdes.stoppelmap.theme.StoppelMapTheme

@Composable
fun ShowHoursSettingsCard(
    showHours: Boolean,
    onShowHoursChanged: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(defaultElevation = 16.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.background
        )
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(
                text = stringResource(R.string.widget_configuration_display_title),
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.size(8.dp))
            Row {
                val interactionSource = remember { MutableInteractionSource() }
                Checkbox(
                    checked = showHours,
                    onCheckedChange = onShowHoursChanged,
                    interactionSource = interactionSource
                )
                Column {
                    Spacer(modifier = Modifier.size(12.dp))
                    Text(
                        text = stringResource(R.string.widget_configuration_display_show_hours_label),
                        modifier = Modifier
                            .clickable(interactionSource, null) { onShowHoursChanged(!showHours) }
                    )
                    Spacer(modifier = Modifier.size(8.dp))
                    Text(
                        text = stringResource(R.string.widget_configuration_display_show_hours_description),
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun ShowHoursSettingsCardPreview() {
    StoppelMapTheme {
        ShowHoursSettingsCard(showHours = false, onShowHoursChanged = {})
    }
}
