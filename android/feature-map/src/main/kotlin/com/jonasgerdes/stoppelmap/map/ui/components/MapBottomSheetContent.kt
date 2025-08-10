package com.jonasgerdes.stoppelmap.map.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Arrangement.Absolute.spacedBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.OpenInBrowser
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.jonasgerdes.stoppelmap.map.R
import com.jonasgerdes.stoppelmap.map.model.FullMapEntity
import com.jonasgerdes.stoppelmap.map.ui.MapViewModel
import com.jonasgerdes.stoppelmap.theme.components.Fee
import com.jonasgerdes.stoppelmap.theme.components.FeeList

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun MapBottomSheetContent(
    bottomSheetState: MapViewModel.BottomSheetState,
    onPrimaryContentHeightChange: (Dp) -> Unit,
    onShareText: (String) -> Unit,
    onOpenUrl: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    AnimatedContent(
        bottomSheetState,
        transitionSpec = {
            fadeIn().togetherWith(fadeOut())
        }
    ) { state ->
        when (state) {
            MapViewModel.BottomSheetState.Hidden -> Unit
            is MapViewModel.BottomSheetState.Idle -> Unit
            is MapViewModel.BottomSheetState.Collection -> {
                SheetContent(
                    onPrimaryContentHeightChange = onPrimaryContentHeightChange,
                    primaryContent = {
                        Text(text = state.name, style = MaterialTheme.typography.headlineLarge)
                        Text(text = state.subline(), style = MaterialTheme.typography.labelLarge)
                    },
                    modifier = modifier,
                )
            }

            is MapViewModel.BottomSheetState.SingleStall -> {
                val mapEntity = state.fullMapEntity
                SheetContent(
                    onPrimaryContentHeightChange = onPrimaryContentHeightChange,
                    primaryContent = {
                        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                            Text(
                                text = mapEntity.name,
                                style = MaterialTheme.typography.headlineLarge,
                                modifier = Modifier.weight(1f)
                            )
                            val shareText = stringResource(R.string.map_sheet_share_text, mapEntity.slug)
                            FilledTonalIconButton(
                                onClick = {
                                    onShareText(shareText)
                                },
                                modifier = Modifier.size(IconButtonDefaults.smallContainerSize())
                            ) {
                                Icon(
                                    Icons.Rounded.Share,
                                    contentDescription = stringResource(R.string.map_sheet_share_button_contentDescription)
                                )
                            }
                        }
                        mapEntity.subline()?.let {
                            Text(text = it, style = MaterialTheme.typography.labelLarge)
                        }
                    },
                    secondaryContent =
                        if (mapEntity.hasSecondaryContent()) {
                            {
                                SingleMapEntityDetails(
                                    mapEntity = mapEntity,
                                    onOpenUrl = onOpenUrl,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        } else null,
                    modifier = modifier,
                )
            }
        }
    }

}

private fun FullMapEntity.hasSecondaryContent() =
    description != null || websites.isNotEmpty()

@Composable
fun SingleMapEntityDetails(
    mapEntity: FullMapEntity,
    onOpenUrl: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier, verticalArrangement = spacedBy(8.dp)) {
        mapEntity.description?.let {
            Text(it)
        }
        if (mapEntity.admissionFees.isNotEmpty()) {
            OutlinedCard(
                Modifier.fillMaxWidth(),
                colors = CardDefaults.outlinedCardColors().copy(containerColor = Color.Transparent)
            ) {
                FeeList(
                    title = stringResource(R.string.map_sheet_prices_title),
                    fees = mapEntity.admissionFees.map { Fee(it.name, it.price) },
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
        if (mapEntity.websites.isNotEmpty()) {
            Text(
                text = stringResource(R.string.map_sheet_websites_title),
                style = MaterialTheme.typography.labelMedium,
                modifier = Modifier.padding(top = 16.dp)
            )
        }
        mapEntity.websites.forEach { website ->
            TextButton(
                onClick = { onOpenUrl(website.url) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Icon(Icons.Rounded.OpenInBrowser, null, modifier = Modifier.size(16.dp))
                    Spacer(Modifier.width(16.dp))
                    Text(website.url, style = MaterialTheme.typography.bodySmall)
                }
            }
        }
    }
}

@Composable
private fun SheetContent(
    onPrimaryContentHeightChange: (Dp) -> Unit,
    primaryContent: @Composable ColumnScope.() -> Unit,
    modifier: Modifier = Modifier,
    secondaryContent: (@Composable ColumnScope.() -> Unit)? = null,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(bottom = 16.dp)
    ) {
        val density = LocalDensity.current
        Column(
            verticalArrangement = spacedBy(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .onGloballyPositioned {
                    with(density) {
                        onPrimaryContentHeightChange(
                            it.size.height.toDp() + if (secondaryContent != null) 64.dp else 0.dp
                        )
                    }
                }
        ) {
            primaryContent()
            Spacer(modifier = Modifier.height(16.dp))
        }
        secondaryContent?.invoke(this@Column)
    }
}