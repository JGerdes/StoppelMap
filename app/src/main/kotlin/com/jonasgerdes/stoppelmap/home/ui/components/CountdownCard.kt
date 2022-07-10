@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)

package com.jonasgerdes.stoppelmap.home.ui.components

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jonasgerdes.stoppelmap.R

@Composable
fun CountdownCard(
    days: Int, hours: Int, minutes: Int, modifier: Modifier = Modifier
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ), modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = stringResource(id = R.string.countdownCard_prefix),
                modifier = Modifier.align(Alignment.Start)
            )
            Spacer(modifier = Modifier.size(8.dp))
            // TODO: Support large font scales better when one unit's value > 99
            Row(horizontalArrangement = Arrangement.SpaceAround) {
                if (days > 0) {
                    CountdownUnit(
                        unitLabel = pluralStringResource(R.plurals.countdownCard_unit_day, days),
                        value = days.toString(),
                        modifier = Modifier.weight(1f)
                    )
                }
                if (hours > 0 || days > 0) {
                    CountdownUnit(
                        unitLabel = pluralStringResource(R.plurals.countdownCard_unit_hour, hours),
                        value = hours.toString(),
                        modifier = Modifier.weight(1f)
                    )
                }
                CountdownUnit(
                    unitLabel = pluralStringResource(R.plurals.countdownCard_unit_minute, minutes),
                    value = minutes.toString(),
                    modifier = Modifier.weight(1f)
                )
            }
            Spacer(modifier = Modifier.size(8.dp))
            Text(
                text = stringResource(id = R.string.countdownCard_suffix),
                modifier = Modifier.align(Alignment.End)
            )
        }
    }
}

@Composable
fun CountdownUnit(
    unitLabel: String, value: String, modifier: Modifier = Modifier
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background
        ), modifier = modifier.padding(8.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
        ) {
            Text(value, style = MaterialTheme.typography.displayMedium)
            Text(unitLabel)
        }
    }
}


@Preview(uiMode = UI_MODE_NIGHT_NO)
@Composable
fun PreviewCountdownCardDay() {
    CountdownCard(days = 129, hours = 1, minutes = 20)
}

@Preview(fontScale = 1.6f)
@Composable
fun PreviewCountdownCardLargeFont() {
    CountdownCard(days = 129, hours = 1, minutes = 20)
}

@Preview(uiMode = UI_MODE_NIGHT_YES)
@Composable
fun PreviewCountdownCardNight() {
    CountdownCard(days = 0, hours = 22, minutes = 0)
}
