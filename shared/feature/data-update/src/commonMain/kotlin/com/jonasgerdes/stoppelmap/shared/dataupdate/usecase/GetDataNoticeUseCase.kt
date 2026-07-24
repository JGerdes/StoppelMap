package com.jonasgerdes.stoppelmap.shared.dataupdate.usecase

import co.touchlab.kermit.Logger
import com.jonasgerdes.stoppelmap.base.model.AppInfo
import com.jonasgerdes.stoppelmap.dto.Platform
import com.jonasgerdes.stoppelmap.shared.dataupdate.data.isCurrentPlatform
import com.jonasgerdes.stoppelmap.shared.dataupdate.repository.AppConfigRepository
import kotlinx.coroutines.flow.map

class GetDataNoticeUseCase(
    private val appConfigRepository: AppConfigRepository,
    private val appInfo: AppInfo,
) {

    operator fun invoke() = appConfigRepository.notices
        .map { noticeWrappers ->
            noticeWrappers
                .filter { noticeWrapper ->
                    (noticeWrapper.version == null || noticeWrapper.version == appInfo.versionCode)
                        .also {
                            if (!it) Logger.v {
                                "💬 ${noticeWrapper.version} didn't match app version ${appInfo.versionCode} [${noticeWrapper.notice.title.entries.first().value}]"
                            }
                        }
                }
                .filter { notice ->
                    (notice.platform.isNullOrEmpty()
                            || notice.platform?.any(Platform::isCurrentPlatform) == true)
                        .also {
                            if (!it) Logger.v {
                                "💬 ${notice.platform?.joinToString()} didn't match app platform [${notice.notice.title.entries.first().value}]"
                            }
                        }
                }
                .map { it.notice }
        }
}
