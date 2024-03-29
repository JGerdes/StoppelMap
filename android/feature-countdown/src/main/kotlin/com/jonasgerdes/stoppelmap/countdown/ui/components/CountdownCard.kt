package com.jonasgerdes.stoppelmap.countdown.ui.components

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jonasgerdes.stoppelmap.countdown.R
import com.jonasgerdes.stoppelmap.theme.StoppelMapTheme

@Composable
fun CountdownCard(
    days: Int,
    hours: Int,
    minutes: Int,
    seconds: Int,
    seasonYear: Int,
    modifier: Modifier = Modifier
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
            Spacer(modifier = Modifier.size(16.dp))
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    CountdownUnit(
                        unitLabel = pluralStringResource(R.plurals.countdownCard_unit_day, days),
                        value = days.toString(),
                        modifier = Modifier.weight(1f)
                    )
                    CountdownUnit(
                        unitLabel = pluralStringResource(
                            R.plurals.countdownCard_unit_hour,
                            hours
                        ),
                        value = hours.toString(),
                        modifier = Modifier.weight(1f)
                    )
                }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    CountdownUnit(
                        unitLabel = pluralStringResource(
                            R.plurals.countdownCard_unit_minute,
                            minutes
                        ),
                        value = minutes.toString(),
                        modifier = Modifier.weight(1f)
                    )
                    CountdownUnit(
                        unitLabel = pluralStringResource(
                            R.plurals.countdownCard_unit_second,
                            seconds
                        ),
                        value = seconds.toString(),
                        modifier = Modifier.weight(1f)
                    )
                }
            }
            Spacer(modifier = Modifier.size(16.dp))
            Text(
                text = stringResource(id = R.string.countdownCard_suffix, seasonYear),
                modifier = Modifier.align(Alignment.End)
            )
        }
    }
}

@Composable
private fun CountdownUnit(
    unitLabel: String, value: String, modifier: Modifier = Modifier
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background
        ), modifier = modifier
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(horizontal = 2.dp, vertical = 8.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = value,
                style = MaterialTheme.typography.displayMedium,
                textAlign = TextAlign.Center,
            )
            Text(
                text = unitLabel,
                textAlign = TextAlign.Center,
            )
        }
    }
}


@Preview(uiMode = UI_MODE_NIGHT_NO)
@Preview(fontScale = 1.6f)
@Preview(uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun PreviewCountdownCard() {
    StoppelMapTheme {
        CountdownCard(days = 129, hours = 1, minutes = 20, seconds = 12, seasonYear = 2024)
    }
}
