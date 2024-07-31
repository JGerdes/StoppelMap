package com.jonasgerdes.stoppelmap.home.usecase

import co.touchlab.kermit.Logger
import com.jonasgerdes.stoppelmap.base.model.AppInfo
import com.jonasgerdes.stoppelmap.dto.Platform
import com.jonasgerdes.stoppelmap.shared.dataupdate.repository.AppConfigRepository
import kotlinx.coroutines.flow.map

class GetRemoteMessagesUseCase(
    private val appConfigRepository: AppConfigRepository,
    private val appInfo: AppInfo,
) {

    operator fun invoke() = appConfigRepository.messages
        .map { messages ->
            messages
                .filter { message ->
                    (message.version == null || message.version == appInfo.versionCode)
                        .also {
                            if (!it) Logger.v {
                                "💬 ${message.version} didn't match app version ${appInfo.versionCode} [${message.message.title.entries.first().value}]"
                            }
                        }
                }
                .filter { message ->
                    (message.platform.isNullOrEmpty()
                            || message.platform?.any(Platform::isCurrentPlatform) == true)
                        .also {
                            if (!it) Logger.v {
                                "💬 ${message.platform?.joinToString()} didn't match app platform [${message.message.title.entries.first().value}]"
                            }
                        }
                }
                .map { it.message }
        }
}

expect fun Platform.isCurrentPlatform(): Boolean
