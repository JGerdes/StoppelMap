package com.jonasgerdes.stoppelmap.home.components

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Arrangement.Absolute.spacedBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.jonasgerdes.stoppelmap.R
import com.jonasgerdes.stoppelmap.dto.config.HomeCard
import com.jonasgerdes.stoppelmap.dto.config.HomeCard.Content.Button
import com.jonasgerdes.stoppelmap.theme.components.rememberBlurHashPainter
import com.jonasgerdes.stoppelmap.theme.i18n.localizedString


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ContentCard(
    content: HomeCard.Content,
    onUrlTap: (String) -> Unit,
    onCallPhoneNumber: (String) -> Unit,
    onSendFeedbackTap: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
    ) {
        Column(
            Modifier
                .fillMaxWidth()
        ) {
            content.headerImage?.let { headerImage ->
                val image = content.headerImageDark?.takeIf { isSystemInDarkTheme() } ?: headerImage
                val blurHashPainter = image.blurHash?.let {
                    rememberBlurHashPainter(blurHash = it)
                }
                AsyncImage(
                    model = image.url,
                    contentDescription = image.contentDescription?.let { localizedString(it) },
                    placeholder = blurHashPainter,
                    error = blurHashPainter,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(2f)
                        .clip(CardDefaults.shape)
                )
            }
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
                    .padding(top = 18.dp, bottom = 8.dp)
            ) {
                Text(
                    text = localizedString(content.text),
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                val buttons = content.buttons
                if (!buttons.isNullOrEmpty()) {
                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start,
                    ) {
                        buttons.forEach { button ->
                            val action: () -> Unit = when (val action = button.action) {
                                is Button.Action.CallPhoneNumber -> {
                                    { onCallPhoneNumber(action.phoneNumber) }
                                }

                                is Button.Action.OpenUrl -> {
                                    val url = localizedString(action.url);
                                    { onUrlTap(url) }
                                }

                                Button.Action.SendFeedback -> {
                                    onSendFeedbackTap
                                }
                            }
                            when (button.type) {
                                Button.Type.Primary -> Button(
                                    onClick = action,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    Row(verticalAlignment = CenterVertically, horizontalArrangement = spacedBy(8.dp)) {
                                        button.icon?.imageVector?.let { icon ->
                                            Icon(icon, null)
                                        }
                                        Text(localizedString(button.label))
                                    }
                                }

                                Button.Type.Secondary -> OutlinedButton(
                                    onClick = action,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    Row(verticalAlignment = CenterVertically, horizontalArrangement = spacedBy(8.dp)) {
                                        button.icon?.imageVector?.let { icon ->
                                            Icon(icon, null)
                                        }
                                        Text(localizedString(button.label))
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}


private val HomeCard.Icon.imageVector
    @Composable get(): ImageVector = when (this) {
        HomeCard.Icon.Phone -> Icons.Rounded.Phone
        HomeCard.Icon.Insta -> ImageVector.vectorResource(R.drawable.ic_social_inst)
        HomeCard.Icon.Bsky -> ImageVector.vectorResource(R.drawable.ic_social_bsky)
        HomeCard.Icon.Masto -> ImageVector.vectorResource(R.drawable.ic_social_mast)
        HomeCard.Icon.GHub -> ImageVector.vectorResource(R.drawable.ic_social_ghub)
    }
