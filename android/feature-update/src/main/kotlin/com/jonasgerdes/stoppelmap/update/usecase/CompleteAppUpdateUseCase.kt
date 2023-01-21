package com.jonasgerdes.stoppelmap.update.usecase

import com.google.android.play.core.appupdate.AppUpdateManager

class CompleteAppUpdateUseCase(
    private val appUpdateManager: AppUpdateManager
) {

    operator fun invoke() {
        appUpdateManager.completeUpdate()
    }
}
