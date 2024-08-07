package com.jonasgerdes.stoppelmap.home.usecase

import co.touchlab.kermit.Logger
import com.jonasgerdes.stoppelmap.base.model.AppInfo
import io.ktor.http.encodeURLParameter

class GetFeedbackEmailUrlUseCase(
    private val appInfo: AppInfo
) {

    operator fun invoke() =
        "mailto:feedback@stoppelmap.de?subject=" +
                "Feedback zur App".encodeURLParameter(spaceToPlus = false) +
                "&body=" +
                "Moin StoppelMap-Team,\n\n ich habe Feedback zur App (Version ${appInfo.versionName}) auf meinem ${appInfo.device} mit ${appInfo.os}:\n\n".encodeURLParameter(
                    spaceToPlus = false
                )
                    .also {
                        Logger.d { "Feedback url: $it" }
                    }

}