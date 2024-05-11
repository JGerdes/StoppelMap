package com.jonasgerdes.stoppelmap.home.usecase

import com.jonasgerdes.stoppelmap.base.model.AppInfo
import com.jonasgerdes.stoppelmap.shared.dataupdate.AppConfigRepository
import kotlinx.coroutines.flow.map

class GetRemoteMessagesUseCase(
    private val appConfigRepository: AppConfigRepository,
    private val appInfo: AppInfo,
) {

    operator fun invoke() = appConfigRepository.messages
        .map { messages ->
            messages
                .filter {
                    it.version == null || it.version == appInfo.versionCode
                }
                .map { it.message }
        }
}
