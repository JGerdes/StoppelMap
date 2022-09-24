package com.jonasgerdes.stoppelmap.update.usecase

import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import timber.log.Timber

@OptIn(ExperimentalCoroutinesApi::class)
class GetAppUpdateDownloadStateUseCase(
    private val appUpdateManager: AppUpdateManager,
) {

    operator fun invoke(): Flow<Result> = callbackFlow {

        val appUpdateInfoTask = appUpdateManager.appUpdateInfo
        appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
            val resultFromInstallStatus = mapInstallStatus(
                appUpdateInfo.installStatus(),
                bytesDownloaded = appUpdateInfo.bytesDownloaded(),
                totalBytesToDownload = appUpdateInfo.totalBytesToDownload()
            )
            val result = when {
                appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                        && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE) ->
                    Result.UpdateAvailable

                resultFromInstallStatus != null -> resultFromInstallStatus
                else -> Result.LatestVersionInstalled
            }
            trySend(result)
        }
        appUpdateInfoTask.addOnCanceledListener {
            trySend(Result.LatestVersionInstalled)
        }
        appUpdateInfoTask.addOnFailureListener {
            Timber.w(it, "Error while getting updateInfo")
            trySend(Result.Unknown)
        }

        val listener = InstallStateUpdatedListener { installState ->
            val result = mapInstallStatus(
                installState.installStatus(),
                bytesDownloaded = installState.bytesDownloaded(),
                totalBytesToDownload = installState.totalBytesToDownload()
            )
            if (result != null) {
                trySend(result)
            }
        }
        appUpdateManager.registerListener(listener)

        invokeOnClose {
            appUpdateManager.unregisterListener(listener)
        }
    }

    private fun mapInstallStatus(
        installStatus: Int,
        bytesDownloaded: Long,
        totalBytesToDownload: Long
    ) = when (installStatus) {
        InstallStatus.PENDING -> Result.DownloadPending
        InstallStatus.DOWNLOADING -> Result.DownloadInProgress(
            bytesDownloaded = bytesDownloaded,
            totalBytesToDownload = totalBytesToDownload
        )

        InstallStatus.DOWNLOADED -> Result.DownloadCompleted
        InstallStatus.FAILED -> Result.DownloadFailed
        InstallStatus.CANCELED -> Result.DownloadCanceled
        else -> null
    }

    sealed interface Result {
        object LatestVersionInstalled : Result
        object UpdateAvailable : Result
        object DownloadPending : Result
        data class DownloadInProgress(
            val bytesDownloaded: Long,
            val totalBytesToDownload: Long,
        ) : Result

        object DownloadCompleted : Result
        object DownloadFailed : Result
        object DownloadCanceled : Result
        object Unknown : Result
    }
}
