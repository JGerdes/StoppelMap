package com.jonasgerdes.stoppelmap.home.usecase

import co.touchlab.kermit.Logger
import com.jonasgerdes.stoppelmap.base.model.AppInfo
import com.jonasgerdes.stoppelmap.shared.dataupdate.usecase.GetCurrentDataVersionUseCase
import io.ktor.http.encodeURLParameter
import kotlinx.coroutines.flow.map

class GetFeedbackEmailUrlUseCase(
    private val appInfo: AppInfo,
    private val getCurrentDataVersion: GetCurrentDataVersionUseCase,
) {

    operator fun invoke() =
        getCurrentDataVersion().map { dataVersion ->
            "mailto:feedback@stoppelmap.de?subject=" +
                    "Feedback zur App".encodeURLParameter(spaceToPlus = false) +
                    "&body=" +
                    "Moin StoppelMap-Team,\n\n ich habe Feedback zur App (Version ${appInfo.versionName}[$dataVersion]) auf meinem ${appInfo.device} mit ${appInfo.os}:\n\n".encodeURLParameter(
                        spaceToPlus = false
                    )
                        .also {
                            Logger.d { "Feedback url: $it" }
                        }
        }

}