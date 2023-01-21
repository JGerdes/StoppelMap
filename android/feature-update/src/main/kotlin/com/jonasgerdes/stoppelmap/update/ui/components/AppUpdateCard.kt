package com.jonasgerdes.stoppelmap.update.ui.components

import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.jonasgerdes.stoppelmap.update.R
import com.jonasgerdes.stoppelmap.update.model.UpdateState

@Composable
fun AppUpdateCard(
    updateState: UpdateState,
    onDownloadTap: (appUpdateInfo: AppUpdateInfo) -> Unit,
    onInstallTap: () -> Unit,
    onOpenGooglePlayTap: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(modifier = modifier.animateContentSize()) {
        Crossfade(targetState = updateState::class, animationSpec = spring()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                when (updateState) {
                    UpdateState.Hidden -> Unit // Invalid state
                    is UpdateState.Available -> {
                        Text(
                            text = stringResource(R.string.appUpdateCard_updateAvailable_title),
                            style = MaterialTheme.typography.titleLarge
                        )
                        Spacer(modifier = Modifier.size(8.dp))
                        Text(
                            text = stringResource(R.string.appUpdateCard_updateAvailable_description),
                        )
                        Spacer(modifier = Modifier.size(16.dp))
                        Button(onClick = {
                            onDownloadTap(updateState.appUpdateInfo)
                        }, modifier = Modifier.align(Alignment.End)) {
                            Text(text = stringResource(R.string.appUpdateCard_updateAvailable_downloadButton))
                        }
                    }

                    is UpdateState.Downloading -> {
                        Text(
                            text = stringResource(R.string.appUpdateCard_downloading_title),
                            style = MaterialTheme.typography.titleLarge
                        )
                        Spacer(modifier = Modifier.size(8.dp))
                        Text(
                            text = stringResource(R.string.appUpdateCard_downloading_description),
                        )
                        Spacer(modifier = Modifier.size(8.dp))
                        when (updateState.progress) {
                            is UpdateState.Downloading.Progress.Determinate -> {
                                LinearProgressIndicator(
                                    progress = updateState.progress.progress,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }

                            UpdateState.Downloading.Progress.Indeterminate -> {
                                LinearProgressIndicator(
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }
                    }

                    UpdateState.ReadyToInstall -> {
                        Text(
                            text = stringResource(R.string.appUpdateCard_readyToInstall_title),
                            style = MaterialTheme.typography.titleLarge
                        )
                        Spacer(modifier = Modifier.size(8.dp))
                        Text(
                            text = stringResource(R.string.appUpdateCard_readyToInstall_description)
                        )
                        Spacer(modifier = Modifier.size(16.dp))
                        Button(onClick = onInstallTap, modifier = Modifier.align(Alignment.End)) {
                            Text(text = stringResource(R.string.appUpdateCard_readyToInstall_installButton))
                        }
                    }

                    UpdateState.Failed -> {
                        Text(
                            text = stringResource(R.string.appUpdateCard_failed_title),
                            style = MaterialTheme.typography.titleLarge
                        )
                        Spacer(modifier = Modifier.size(8.dp))
                        Text(
                            text = stringResource(R.string.appUpdateCard_failed_description)
                        )
                        Spacer(modifier = Modifier.size(16.dp))
                        Button(
                            onClick = onOpenGooglePlayTap,
                            modifier = Modifier.align(Alignment.End)
                        ) {
                            Text(text = stringResource(R.string.appUpdateCard_failed_openGooglePlayButton))
                        }
                    }
                }
            }
        }
    }
}
