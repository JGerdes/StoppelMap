@file:OptIn(
    ExperimentalMaterial3Api::class,
)

package com.jonasgerdes.stoppelmap.settings.ui

import androidx.annotation.StringRes
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.ColorLens
import androidx.compose.material.icons.rounded.DarkMode
import androidx.compose.material.icons.rounded.EditCalendar
import androidx.compose.material.icons.rounded.EditLocationAlt
import androidx.compose.material.icons.rounded.Map
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jonasgerdes.stoppelmap.settings.R
import com.jonasgerdes.stoppelmap.settings.data.DateOverride
import com.jonasgerdes.stoppelmap.settings.data.LocationOverride
import com.jonasgerdes.stoppelmap.theme.modifier.elevationWhenScrolled
import com.jonasgerdes.stoppelmap.theme.settings.ColorSchemeSetting
import com.jonasgerdes.stoppelmap.theme.settings.MapColorSetting
import com.jonasgerdes.stoppelmap.theme.settings.ThemeSetting
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalLayoutApi::class, ExperimentalFoundationApi::class)
@Composable
fun SettingsScreen(
    onNavigateBack: () -> Unit,
    onUrlTap: (url: String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SettingsViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    val listState = rememberLazyListState()
    Column(
        modifier = modifier
    ) {
        TopAppBar(
            title = { Text(text = stringResource(id = R.string.settings_topbar_title)) },
            navigationIcon = {
                IconButton(
                    onClick = { onNavigateBack() }
                ) {
                    Icon(
                        Icons.Rounded.ArrowBack,
                        stringResource(id = R.string.settings_topbar_navigateBack_contentDescription)
                    )
                }
            },
            modifier = Modifier
                .elevationWhenScrolled(listState)
        )
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            state = listState,
            modifier = Modifier.fillMaxSize()
        ) {
            item {
                SettingsSectionLabel(R.string.settings_ui_title)
            }
            item {
                SelectionSettingsRow(
                    icon = Icons.Rounded.DarkMode,
                    labelRes = R.string.settings_theme_title,
                    items = state.themeSettings,
                    isItemSelected = { isSelected },
                    itemLabelRes = { themeSetting.titleStringRes },
                    onItemSelected = { viewModel.onThemeSettingSelected(it.themeSetting) },
                )
            }
            item {
                SelectionSettingsRow(
                    icon = Icons.Rounded.ColorLens,
                    labelRes = R.string.settings_colorScheme_title,
                    items = state.colorSchemeSettings,
                    isItemSelected = { isSelected },
                    itemLabelRes = { colorSchemeSetting.titleStringRes },
                    onItemSelected = { viewModel.onColorSchemeSettingSelected(it.colorSchemeSetting) },
                )
            }
            item {
                SelectionSettingsRow(
                    icon = Icons.Rounded.Map,
                    labelRes = R.string.settings_mapColorScheme_title,
                    items = state.mapColorSettings,
                    isItemSelected = { isSelected },
                    itemLabelRes = { value.titleStringRes },
                    onItemSelected = { viewModel.onMapColorSchemeSettingSelected(it.value) },
                )
            }
            val developerSettings = state.developerModeSettings
            if (developerSettings is SettingsViewModel.DeveloperModeSettings.Active) {
                item {
                    SettingsSectionLabel(R.string.settings_developerMode_title)
                }
                item {
                    SelectionSettingsRow(
                        icon = Icons.Rounded.EditCalendar,
                        labelRes = R.string.settings_dateOverride_title,
                        items = developerSettings.dateOverrideOptions,
                        isItemSelected = { isSelected },
                        itemLabelRes = { value.titleStringRes },
                        onItemSelected = { viewModel.onDateOverrideSelected(it.value) },
                    )
                }
                item {
                    SelectionSettingsRow(
                        icon = Icons.Rounded.EditLocationAlt,
                        labelRes = R.string.settings_locationOverride_title,
                        items = developerSettings.locationOverrideOptions,
                        isItemSelected = { isSelected },
                        itemLabelRes = { value.titleStringRes },
                        onItemSelected = { viewModel.onLocationOverrideSelected(it.value) },
                    )
                }
            }
            item {
                SettingsSectionLabel(R.string.settings_disclaimer_title)
            }
            item {
                Text(
                    text = stringResource(R.string.settings_disclaimer_text),
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
            item {
                SettingsSectionLabel(R.string.settings_info_title)
            }
            item {
                ListItem(
                    headlineContent = { Text(stringResource(R.string.settings_info_version)) },
                    trailingContent = {
                        Column(horizontalAlignment = Alignment.End) {
                            Text(state.appInfo.versionName)
                            Text(state.appInfo.versionCode.toString())
                        }
                    },
                    modifier = Modifier.clickable { viewModel.onVersionClick() }
                )
            }
            item {
                ListItem(
                    headlineContent = { Text(stringResource(R.string.settings_info_build_type)) },
                    trailingContent = { Text(state.appInfo.buildType) },
                )
            }
            item {
                val commitUrl =
                    stringResource(id = R.string.settings_repo_url_commit, state.appInfo.commitSha)
                ListItem(
                    headlineContent = { Text(stringResource(R.string.settings_info_commit)) },
                    trailingContent = { Text(state.appInfo.commitSha) },
                    modifier = Modifier.clickable {
                        onUrlTap(commitUrl)
                    }
                )
            }
            item {
                SettingsSectionLabel(R.string.settings_libraries_title)
            }
            items(
                state.libraries,
                key = { it.name },
                contentType = { ContentTypes.LIBRARY }
            ) {
                val url = it.githubUrl ?: it.gitlabUrl ?: it.sourceUrl
                ListItem(
                    headlineContent = { Text(it.name) },
                    supportingContent = { Text(it.author) },
                    trailingContent = {
                        TextButton(
                            onClick = { onUrlTap(it.license.link) },
                        ) {
                            Text(it.license.name)
                        }
                    },
                    modifier = url?.let { Modifier.clickable { onUrlTap(it) } } ?: Modifier
                )
            }
            item {
                SettingsSectionLabel(R.string.settings_images_title)
            }
            items(
                state.imageSources,
                key = { it.work },
                contentType = { ContentTypes.IMAGE_SOURCE }
            ) {
                ListItem(
                    headlineContent = { Text(it.work) },
                    supportingContent = { Text(it.author) },
                    leadingContent = it.resource?.let { resource ->
                        {
                            Image(
                                painter = painterResource(id = resource),
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .width(114.dp)
                                    .height(64.dp),
                            )
                        }
                    },
                    trailingContent = it.license?.let { license ->
                        {
                            TextButton(
                                onClick = { onUrlTap(license.link) },
                            ) {
                                Text(license.name)
                            }
                        }
                    },
                    modifier = Modifier.clickable { onUrlTap(it.sourceUrl) }
                )
            }
        }
    }
}

enum class ContentTypes {
    LIBRARY,
    IMAGE_SOURCE,
}


@OptIn(ExperimentalMaterial3Api::class)
@ExperimentalLayoutApi
@Composable
fun <T> SelectionSettingsRow(
    icon: ImageVector,
    @StringRes labelRes: Int,
    items: List<T>,
    isItemSelected: T.() -> Boolean,
    itemLabelRes: T.() -> Int,
    onItemSelected: (T) -> Unit,
) {
    ListItem(
        leadingContent = { Icon(icon, null) },
        headlineContent = { Text(stringResource(labelRes)) },
        supportingContent = {
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items.forEach {
                    FilterChip(
                        selected = it.isItemSelected(),
                        onClick = { onItemSelected(it) },
                        label = { Text(stringResource(it.itemLabelRes())) })
                }
            }
        }
    )
}

@Composable
fun SettingsSectionLabel(
    @StringRes title: Int,
    modifier: Modifier = Modifier
) {
    Text(
        stringResource(title),
        style = MaterialTheme.typography.labelLarge,
        color = MaterialTheme.colorScheme.secondary,
        modifier = modifier
            .padding(top = 16.dp)
            .padding(horizontal = 16.dp)
    )
}

private val ThemeSetting.titleStringRes: Int
    get() = when (this) {
        ThemeSetting.Light -> R.string.settings_theme_light
        ThemeSetting.Dark -> R.string.settings_theme_dark
        ThemeSetting.System -> R.string.settings_theme_system
    }

private val ColorSchemeSetting.titleStringRes: Int
    get() = when (this) {
        ColorSchemeSetting.Classic -> R.string.settings_colorScheme_classic
        ColorSchemeSetting.System -> R.string.settings_colorScheme_system
    }

private val MapColorSetting.titleStringRes: Int
    get() = when (this) {
        MapColorSetting.Classic -> R.string.settings_mapColorScheme_classic
        MapColorSetting.AppScheme -> R.string.settings_mapColorScheme_appScheme
    }

private val DateOverride.titleStringRes: Int
    get() = when (this) {
        DateOverride.None -> R.string.settings_dateOverride_none
        DateOverride.TodayInStoMaWeek -> R.string.settings_dateOverride_todayInStomaWeek
        DateOverride.Wednesday -> R.string.settings_dateOverride_wed
        DateOverride.Thursday -> R.string.settings_dateOverride_thu
        DateOverride.Friday -> R.string.settings_dateOverride_fri
        DateOverride.Saturday -> R.string.settings_dateOverride_sat
        DateOverride.Sunday -> R.string.settings_dateOverride_sun
        DateOverride.Monday -> R.string.settings_dateOverride_mon
        DateOverride.Tuesday -> R.string.settings_dateOverride_tue
    }

private val LocationOverride.titleStringRes: Int
    get() = when (this) {
        LocationOverride.None -> R.string.settings_locationOverride_none
        LocationOverride.Amtmannsbult -> R.string.settings_locationOverride_amtmannsbult
        LocationOverride.BusToEntrance -> R.string.settings_locationOverride_busToEntrance
    }
