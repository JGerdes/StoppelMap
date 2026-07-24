package com.jonasgerdes.stoppelmap.home.usecase

import com.jonasgerdes.stoppelmap.base.model.AppInfo
import com.jonasgerdes.stoppelmap.dto.config.HomeCard
import com.jonasgerdes.stoppelmap.shared.dataupdate.data.isCurrentPlatform
import com.jonasgerdes.stoppelmap.shared.dataupdate.repository.AppConfigRepository
import kotlinx.coroutines.flow.map

class GetHomeCardsUseCase(
    private val appConfigRepository: AppConfigRepository,
    private val appInfo: AppInfo,
) {

    operator fun invoke() = appConfigRepository.homeCards
        .map {
            it.filter { it.condition.isMet() }
        }


    fun HomeCard.Condition?.isMet(): Boolean =
        when {
            this == null -> true
            platform.let { platform ->
                platform != null && platform.isNotEmpty() && platform.none { it.isCurrentPlatform() }
            } -> false

            version.let {
                it != null && it != appInfo.versionCode
            } -> false

            minVersion.let {
                it != null && it > appInfo.versionCode
            } -> false

            maxVersion.let {
                it != null && it < appInfo.versionCode
            } -> false

            else -> true
        }
}

