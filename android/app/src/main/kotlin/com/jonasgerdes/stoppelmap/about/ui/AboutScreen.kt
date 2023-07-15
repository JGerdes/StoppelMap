@file:OptIn(
    ExperimentalMaterial3Api::class,
)

package com.jonasgerdes.stoppelmap.about.ui

import android.annotation.SuppressLint
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Attribution
import androidx.compose.material.icons.rounded.Balance
import androidx.compose.material.icons.rounded.Code
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.jonasgerdes.stoppelmap.BuildConfig
import com.jonasgerdes.stoppelmap.R
import com.jonasgerdes.stoppelmap.about.data.License
import com.jonasgerdes.stoppelmap.about.data.imageSources
import com.jonasgerdes.stoppelmap.about.data.libraries
import com.jonasgerdes.stoppelmap.theme.components.ListLineHeader
import com.jonasgerdes.stoppelmap.theme.modifier.elevationWhenScrolled

@SuppressLint("NewApi")
@Composable
fun AboutScreen(
    onNavigateBack: () -> Unit,
    onUrlTap: (url: String) -> Unit,
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState()
    Column(
        modifier = modifier
    ) {
        CenterAlignedTopAppBar(
            title = { Text(text = stringResource(id = R.string.about_topbar_title)) },
            navigationIcon = {
                IconButton(
                    onClick = { onNavigateBack() }
                ) {
                    Icon(
                        Icons.Rounded.ArrowBack,
                        stringResource(id = R.string.about_topbar_navigateBack_contentDescription)
                    )
                }
            },
            modifier = Modifier.elevationWhenScrolled(listState)
        )
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            state = listState,
            modifier = Modifier.fillMaxSize()
        ) {

            item {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Image(painterResource(R.drawable.logo_color_144dp), contentDescription = null)
                    Spacer(modifier = Modifier.size(8.dp))
                    Text(
                        text = stringResource(R.string.app_name),
                        style = MaterialTheme.typography.displaySmall,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.size(8.dp))
                    Text(
                        text = BuildConfig.VERSION_NAME,
                        textAlign = TextAlign.Center
                    )
                }
            }

            item {
                AboutHeader(R.string.about_disclaimer_title)
            }
            item {
                Text(stringResource(R.string.about_disclaimer_text))
            }
            item {
                AboutHeader(R.string.about_libraries_title)
            }
            items(
                libraries,
                key = { it.name },
                contentType = { ContentTypes.LIBRARY }
            ) {
                LibraryCard(
                    content = it.name,
                    author = it.author,
                    license = it.license,
                    url = it.githubUrl ?: it.gitlabUrl ?: it.sourceUrl,
                    onTap = onUrlTap,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            item {
                AboutHeader(R.string.about_images_title)
            }
            items(
                imageSources,
                key = { it.work },
                contentType = { ContentTypes.LIBRARY }
            ) {
                LibraryCard(
                    content = it.work,
                    author = it.author,
                    license = it.license,
                    url = it.website,
                    onTap = onUrlTap,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

enum class ContentTypes {
    LIBRARY
}


@Composable
fun AboutHeader(
    @StringRes title: Int,
    modifier: Modifier = Modifier
) {
    ListLineHeader(modifier = modifier.padding(top = 16.dp)) {
        Text(
            text = stringResource(id = title),
            style = MaterialTheme.typography.titleLarge
        )
    }
}

@Composable
fun LibraryCard(
    content: String,
    author: String,
    license: License? = null,
    url: String? = null,
    onTap: (url: String) -> Unit,
    modifier: Modifier = Modifier
) {
    val clickModifier = url?.let { Modifier.clickable { onTap(it) } } ?: Modifier
    OutlinedCard(modifier.then(clickModifier)) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = content,
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.size(4.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Rounded.Attribution,
                    contentDescription = stringResource(R.string.about_libraries_item_attribution)
                )
                Spacer(modifier = Modifier.size(8.dp))
                Text(text = author)
            }
            if (license != null) {
                Spacer(modifier = Modifier.size(4.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable {
                        onTap(license.link)
                    }
                ) {
                    Icon(
                        Icons.Rounded.Balance,
                        contentDescription = stringResource(R.string.about_libraries_item_license)
                    )
                    Spacer(modifier = Modifier.size(8.dp))
                    Text(text = license.name)
                }
            }
            if (url != null) {
                Spacer(modifier = Modifier.size(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Rounded.Code,
                        contentDescription = stringResource(R.string.about_libraries_item_sourceUrl)
                    )
                    Spacer(modifier = Modifier.size(8.dp))
                    Text(
                        text = url,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}
