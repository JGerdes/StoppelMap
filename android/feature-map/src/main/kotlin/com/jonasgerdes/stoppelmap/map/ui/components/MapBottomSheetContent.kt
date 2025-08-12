package com.jonasgerdes.stoppelmap.map.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.OpenInBrowser
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.carousel.HorizontalMultiBrowseCarousel
import androidx.compose.material3.carousel.rememberCarouselState
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.jonasgerdes.stoppelmap.map.R
import com.jonasgerdes.stoppelmap.map.model.FullMapEntity
import com.jonasgerdes.stoppelmap.map.ui.MapViewModel
import com.jonasgerdes.stoppelmap.resources.defaultFormat
import com.jonasgerdes.stoppelmap.resources.toFullResourceString
import com.jonasgerdes.stoppelmap.theme.components.EventRow
import com.jonasgerdes.stoppelmap.theme.components.Fee
import com.jonasgerdes.stoppelmap.theme.components.FeeList
import com.jonasgerdes.stoppelmap.theme.components.rememberBlurHashPainter
import com.jonasgerdes.stoppelmap.theme.material.appBarContainerColor
import com.jonasgerdes.stoppelmap.theme.util.MeasureUnconstrainedViewWidth
import com.jonasgerdes.stoppelmap.theme.util.localizedString
import kotlinx.datetime.LocalTime
import kotlinx.datetime.format
import timber.log.Timber

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun MapBottomSheetContent(
    bottomSheetState: MapViewModel.BottomSheetState,
    onPrimaryContentHeightChange: (Dp) -> Unit,
    onShareText: (String) -> Unit,
    onOpenUrl: (String) -> Unit,
    modifier: Modifier = Modifier,
    scrollState: ScrollState = rememberScrollState()
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
                    scrollState = scrollState,
                    modifier = modifier,
                )
            }

            is MapViewModel.BottomSheetState.SingleStall.Loading -> {
                SheetContent(
                    onPrimaryContentHeightChange = onPrimaryContentHeightChange,
                    primaryContent = {
                        Spacer(Modifier.size(32.dp))
                    },
                    scrollState = scrollState,
                )
            }

            is MapViewModel.BottomSheetState.SingleStall.Loaded -> {
                val mapEntity = state.fullMapEntity
                SheetContent(
                    onPrimaryContentHeightChange = onPrimaryContentHeightChange,
                    primaryContent = {
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
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
                                modifier = Modifier
                                    .size(IconButtonDefaults.smallContainerSize())
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
                    scrollState = scrollState,
                    modifier = modifier,
                )
            }
        }
    }

}

private fun FullMapEntity.hasSecondaryContent() =
    description != null || websites.isNotEmpty() || events.isNotEmpty() || images.isNotEmpty() || admissionFees.isNotEmpty()

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SingleMapEntityDetails(
    mapEntity: FullMapEntity,
    onOpenUrl: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier, verticalArrangement = spacedBy(32.dp)) {
        if (mapEntity.images.isNotEmpty()) {
            if (mapEntity.images.size < 3) {
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    mapEntity.images.forEach { image ->
                        val blurHashPainter =
                            rememberBlurHashPainter(blurHash = image.blurHash)
                        AsyncImage(
                            model = image.url.also { Timber.d("Image: $image") },
                            contentDescription = image.caption,
                            contentScale = ContentScale.Crop,
                            placeholder = blurHashPainter,
                            error = blurHashPainter,
                            modifier = Modifier
                                .weight(1f)
                                .height(205.dp)
                                .clip(MaterialTheme.shapes.extraLarge),
                        )
                    }
                }
            } else {
                HorizontalMultiBrowseCarousel(
                    state = rememberCarouselState { mapEntity.images.count() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    preferredItemWidth = 256.dp,
                    itemSpacing = 8.dp,
                ) { i ->
                    val image = mapEntity.images[i]
                    val blurHashPainter =
                        rememberBlurHashPainter(blurHash = image.blurHash)
                    AsyncImage(
                        model = image.url.also { Timber.d("Image: $image") },
                        contentDescription = image.caption,
                        contentScale = ContentScale.Crop,
                        placeholder = blurHashPainter,
                        error = blurHashPainter,
                        modifier = Modifier
                            .height(205.dp)
                            .maskClip(MaterialTheme.shapes.extraLarge),
                    )
                }
            }
        }
        mapEntity.description?.let {
            Text(it)
        }
        if (mapEntity.admissionFees.isNotEmpty()) {
            InfoCard {
                FeeList(
                    title = stringResource(R.string.map_sheet_prices_title),
                    fees = mapEntity.admissionFees.map { Fee(it.name, it.price) },
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
        if (mapEntity.events.isNotEmpty()) {
            val timeFormat = remember { LocalTime.defaultFormat() }
            InfoCard {
                Column(
                    Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp)
                        .padding(vertical = 16.dp)
                ) {
                    Text(
                        text = stringResource(R.string.map_sheet_events_title),
                        style = MaterialTheme.typography.labelMedium,
                    )
                    Spacer(Modifier.size(8.dp))
                    var selectedEvent by remember { mutableStateOf<String?>(null) }
                    mapEntity.events.forEachIndexed { index, eventDay ->
                        Text(
                            stringResource(eventDay.date.dayOfWeek.toFullResourceString().resourceId),
                            modifier = Modifier.padding(top = 8.dp)
                        )
                        eventDay.events.forEach { event ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = spacedBy(8.dp),
                            ) {
                                MeasureUnconstrainedViewWidth({
                                    Text(
                                        "00000",
                                        modifier = Modifier.padding(horizontal = 4.dp)
                                    )
                                }) { measuredWidth ->
                                    Text(
                                        event.start.time.format(timeFormat),
                                        modifier = Modifier.width(measuredWidth)
                                    )
                                }
                                EventRow(
                                    name = localizedString(event.name),
                                    description = event.description?.let { localizedString(it) },
                                    locationName = null,
                                    isBookmarked = event.isBookmarked,
                                    selected = selectedEvent == event.slug,
                                    onSelected = {
                                        selectedEvent = if (selectedEvent == event.slug) null else event.slug
                                    },
                                    onNotificationToggle = {},
                                    showNotificationToggle = selectedEvent == event.slug || event.isBookmarked,
                                    unSelectedBackgroundColor = Color.Transparent,
                                    padding = 4.dp,
                                )
                            }
                        }
                    }
                }
            }
        }

        if (mapEntity.websites.isNotEmpty()) {
            Column(
                verticalArrangement = spacedBy(4.dp), modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    text = stringResource(R.string.map_sheet_websites_title),
                    style = MaterialTheme.typography.labelMedium
                )
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
    }
}

@Composable
private fun SheetContent(
    onPrimaryContentHeightChange: (Dp) -> Unit,
    primaryContent: @Composable ColumnScope.() -> Unit,
    modifier: Modifier = Modifier,
    secondaryContent: (@Composable ColumnScope.() -> Unit)? = null,
    scrollState: ScrollState,
) {
    val containerColor by appBarContainerColor(
        scrollState = scrollState,
        initialColor = MaterialTheme.colorScheme.surfaceColorAtElevation(8.dp).copy(alpha = 0f),
        elevatedColor = MaterialTheme.colorScheme.surfaceColorAtElevation(8.dp)
    )
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        val density = LocalDensity.current
        Column(
            verticalArrangement = spacedBy(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .background(containerColor)
                .padding(horizontal = 16.dp)
                .padding(top = 4.dp, bottom = 8.dp)
                .onGloballyPositioned {
                    with(density) {
                        onPrimaryContentHeightChange(
                            it.size.height.toDp() + if (secondaryContent != null) 64.dp else 0.dp
                        )
                    }
                }
        ) {
            primaryContent()
        }
        if (secondaryContent != null) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(scrollState)
                    .padding(horizontal = 16.dp)
            ) {
                Spacer(Modifier.height(16.dp))
                secondaryContent()
                Spacer(Modifier.height(16.dp))
            }
        }
    }
}

@Composable
private fun InfoCard(modifier: Modifier = Modifier, content: @Composable ColumnScope.() -> Unit) {
    OutlinedCard(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.outlinedCardColors().copy(containerColor = Color.Transparent)
    ) {
        content()
    }
}