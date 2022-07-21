@file:OptIn(
    ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class,
    ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class
)

package com.jonasgerdes.stoppelmap.countdown.ui.components.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jonasgerdes.stoppelmap.countdown.R
import com.jonasgerdes.stoppelmap.countdown.ui.Font
import com.jonasgerdes.stoppelmap.theme.StoppelMapTheme
import androidx.compose.ui.text.font.Font as ComposeFont

@Composable
fun DisplaySettingsCard(
    showHours: Boolean,
    onShowHoursChanged: (Boolean) -> Unit,
    selectableFonts: List<Font>,
    font: Font,
    onFontChanged: (Font) -> Unit,
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
                style = MaterialTheme.typography.titleLarge
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
                }
            }
            Spacer(modifier = Modifier.size(16.dp))
            Text(
                text = stringResource(R.string.widget_configuration_font_title),
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.size(8.dp))
            selectableFonts.forEach { selectableFont ->
                Row {
                    val interactionSource = remember { MutableInteractionSource() }
                    RadioButton(
                        selected = selectableFont == font,
                        onClick = { onFontChanged(selectableFont) },
                        interactionSource = interactionSource
                    )
                    Column {
                        Spacer(modifier = Modifier.size(12.dp))
                        Text(
                            text = stringResource(selectableFont.fontName),
                            modifier = Modifier
                                .clickable(
                                    interactionSource,
                                    null
                                ) { onShowHoursChanged(!showHours) },
                            style = LocalTextStyle.current.copy(
                                fontFamily = FontFamily(
                                    ComposeFont(selectableFont.fontResource)
                                )
                            )
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun DisplaySettingsCardPreview() {
    StoppelMapTheme {
        DisplaySettingsCard(
            showHours = false,
            onShowHoursChanged = {},
            selectableFonts = Font.values().toList(),
            font = Font.Roboto,
            onFontChanged = { }
        )
    }
}
