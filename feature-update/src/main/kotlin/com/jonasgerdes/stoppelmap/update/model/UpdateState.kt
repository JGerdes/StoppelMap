package com.jonasgerdes.stoppelmap.update.model

import com.google.android.play.core.appupdate.AppUpdateInfo

sealed interface UpdateState {
    object Hidden : UpdateState
    data class Available(val appUpdateInfo: AppUpdateInfo) : UpdateState
    data class Downloading(
        val progress: Progress
    ) : UpdateState {
        sealed interface Progress {
            object Indeterminate : Progress
            data class Determinate(val progress: Float) : Progress
        }
    }

    object ReadyToInstall : UpdateState
    object Failed : UpdateState
}
