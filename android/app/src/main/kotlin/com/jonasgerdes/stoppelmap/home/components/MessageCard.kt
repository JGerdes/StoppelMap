package com.jonasgerdes.stoppelmap.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jonasgerdes.stoppelmap.shared.dataupdate.model.Message
import com.jonasgerdes.stoppelmap.theme.i18n.localizedString

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MessageCard(
    message: Message,
    onUrlTap: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = if (message.type == Message.Type.Warning) {
            CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.errorContainer,
            )
        } else CardDefaults.cardColors()
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(top = 16.dp, bottom = 8.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                val circleColor =
                    if (message.type == Message.Type.Warning) MaterialTheme.colorScheme.surface
                    else MaterialTheme.colorScheme.primary
                if (message.type != null) {
                    Box(
                        modifier = Modifier
                            .background(
                                circleColor,
                                CircleShape
                            )
                            .padding(8.dp)
                    ) {
                        Icon(
                            when (message.type) {
                                Message.Type.Info -> Icons.Rounded.Info
                                Message.Type.Warning -> Icons.Rounded.Warning
                                else -> Icons.Rounded.Info
                            },
                            contentDescription = null,
                            tint = contentColorFor(circleColor),
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                }
                Text(
                    text = localizedString(message.title),
                    style = MaterialTheme.typography.titleLarge,
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = localizedString(message.content)
            )
            Spacer(modifier = Modifier.height(8.dp))
            if (message.buttons.isNotEmpty()) {
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    message.buttons.forEach { button ->
                        val url = localizedString(button.url)
                        Button(onClick = { onUrlTap(url) }) {
                            Text(localizedString(button.title))
                        }
                    }
                }
            }

        }
    }
}
