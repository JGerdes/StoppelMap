@file:OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalComposeUiApi::class,
    ExperimentalLifecycleComposeApi::class
)

package com.jonasgerdes.stoppelmap.about.ui

import android.annotation.SuppressLint
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Attribution
import androidx.compose.material.icons.rounded.Balance
import androidx.compose.material.icons.rounded.Code
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import com.jonasgerdes.stoppelmap.BuildConfig
import com.jonasgerdes.stoppelmap.R
import com.jonasgerdes.stoppelmap.about.data.Library
import com.jonasgerdes.stoppelmap.about.data.libraries
import com.jonasgerdes.stoppelmap.theme.components.ListLineHeader

@SuppressLint("NewApi")
@Composable
fun AboutScreen(
    onNavigateBack: () -> Unit,
    onUrlTap: (url: String) -> Unit,
    modifier: Modifier = Modifier
) {

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
            }
        )
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
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
                    library = it,
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
    library: Library,
    onTap: (url: String) -> Unit,
    modifier: Modifier = Modifier
) {
    val sourceUrl = library.githubUrl ?: library.gitlabUrl ?: library.sourceUrl
    val clickModifier = sourceUrl?.let { Modifier.clickable { onTap(it) } } ?: Modifier
    OutlinedCard(modifier.then(clickModifier)) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = library.name,
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.size(4.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Rounded.Attribution,
                    contentDescription = stringResource(R.string.about_libraries_item_attribution)
                )
                Spacer(modifier = Modifier.size(8.dp))
                Text(text = library.author)
            }
            Spacer(modifier = Modifier.size(4.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Rounded.Balance,
                    contentDescription = stringResource(R.string.about_libraries_item_license)
                )
                Spacer(modifier = Modifier.size(8.dp))
                Text(text = library.license.name)
            }
            if (sourceUrl != null) {
                Spacer(modifier = Modifier.size(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Rounded.Code,
                        contentDescription = stringResource(R.string.about_libraries_item_sourceUrl)
                    )
                    Spacer(modifier = Modifier.size(8.dp))
                    Text(
                        text = sourceUrl,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}
