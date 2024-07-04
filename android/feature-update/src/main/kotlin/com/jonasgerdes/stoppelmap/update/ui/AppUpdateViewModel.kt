package com.jonasgerdes.stoppelmap.update.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jonasgerdes.stoppelmap.update.model.UpdateState
import com.jonasgerdes.stoppelmap.update.usecase.CompleteAppUpdateUseCase
import com.jonasgerdes.stoppelmap.update.usecase.GetAppUpdateStateUseCase
import com.jonasgerdes.stoppelmap.update.usecase.GetAppUpdateStateUseCase.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class AppUpdateViewModel(
    getAppUpdateState: GetAppUpdateStateUseCase,
    private val completeAppUpdate: CompleteAppUpdateUseCase,
) : ViewModel() {

    private val updateState: Flow<UpdateState> =
        getAppUpdateState().map { appUpdateState ->
            when (appUpdateState) {
                Result.LatestVersionInstalled -> UpdateState.Hidden
                Result.Unknown -> UpdateState.Hidden
                is Result.UpdateAvailable -> UpdateState.Available(appUpdateState.appUpdateInfo)
                Result.DownloadPending -> UpdateState.Downloading(progress = UpdateState.Downloading.Progress.Indeterminate)
                is Result.DownloadInProgress -> UpdateState.Downloading(
                    progress = appUpdateState.totalBytesToDownload.let {
                        if (it == 0L) UpdateState.Downloading.Progress.Indeterminate
                        else UpdateState.Downloading.Progress.Determinate(
                            appUpdateState.bytesDownloaded / appUpdateState.totalBytesToDownload.toFloat()
                        )
                    }
                )

                Result.DownloadCanceled -> UpdateState.Failed
                Result.DownloadCompleted -> UpdateState.ReadyToInstall
                Result.DownloadFailed -> UpdateState.Failed
            }
        }

    val state: StateFlow<ViewState> = updateState
        .map(::ViewState)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = ViewState()
        )

    fun onCompleteAppUpdateTapped() {
        completeAppUpdate()
    }

    data class ViewState(
        val updateState: UpdateState = UpdateState.Hidden
    )
}