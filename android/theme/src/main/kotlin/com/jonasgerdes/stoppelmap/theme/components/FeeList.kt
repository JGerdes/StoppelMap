package com.jonasgerdes.stoppelmap.theme.components

import android.content.res.Resources
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.os.ConfigurationCompat
import java.text.NumberFormat
import java.util.Currency
import java.util.Locale


data class Fee(
    val name: String,
    val price: Int,
)

@Composable
fun FeeList(
    title: String,
    fees: List<Fee>,
    modifier: Modifier = Modifier,
    footer: (@Composable () -> Unit)? = null
) {
    Column(modifier.fillMaxWidth()) {
        Text(
            text = title,
            style = MaterialTheme.typography.labelMedium
        )
        Spacer(modifier = Modifier.size(8.dp))
        fees.forEach { fee ->
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = fee.name
                )
                Spacer(modifier = Modifier.size(8.dp))
                Text(
                    text = fee.formatAmount(LocalContext.current.resources)
                )
            }
        }
        footer?.invoke()
    }
}

private fun Fee.formatAmount(resources: Resources) =
    NumberFormat.getCurrencyInstance(
        ConfigurationCompat.getLocales(resources.configuration)[0] ?: Locale.getDefault()
    ).apply {
        maximumFractionDigits = 2
        currency = Currency.getInstance("EUR")
    }.format(price / 100f)