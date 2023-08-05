package com.jonasgerdes.stoppelmap.home.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jonasgerdes.stoppelmap.base.extensions.combine
import com.jonasgerdes.stoppelmap.countdown.model.CountDown
import com.jonasgerdes.stoppelmap.countdown.usecase.GetOpeningCountDownFlowUseCase
import com.jonasgerdes.stoppelmap.countdown.usecase.ShouldShowCountdownWidgetSuggestionUseCase
import com.jonasgerdes.stoppelmap.dataupdate.model.Message
import com.jonasgerdes.stoppelmap.home.usecase.GetRemoteMessagesUseCase
import com.jonasgerdes.stoppelmap.schedule.GetNextOfficialEventUseCase
import com.jonasgerdes.stoppelmap.update.model.UpdateState
import com.jonasgerdes.stoppelmap.update.usecase.CompleteAppUpdateUseCase
import com.jonasgerdes.stoppelmap.update.usecase.GetAppUpdateStateUseCase
import com.jonasgerdes.stoppelmap.update.usecase.GetAppUpdateStateUseCase.Result
import com.jonasgerdes.stoppelmap.usecase.IsCurrentYearsSeasonJustOverUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn

class HomeViewModel(
    getAppUpdateState: GetAppUpdateStateUseCase,
    private val completeAppUpdate: CompleteAppUpdateUseCase,
    getOpeningCountDown: GetOpeningCountDownFlowUseCase,
    private val shouldShowCountdownWidgetSuggestion: ShouldShowCountdownWidgetSuggestionUseCase,
    getNextOfficialEvent: GetNextOfficialEventUseCase,
    isCurrentYearsSeasonJustOver: IsCurrentYearsSeasonJustOverUseCase,
    getRemoteMessages: GetRemoteMessagesUseCase,
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

    private val openingCountDownState: Flow<CountDownState> =
        getOpeningCountDown().map { countDownResult ->
            if (countDownResult is CountDown.InFuture) {
                CountDownState.CountingDown(
                    daysLeft = countDownResult.daysLeft,
                    hoursLeft = countDownResult.hoursLeft,
                    minutesLeft = countDownResult.minutesLeft,
                    year = countDownResult.year,
                    showCurrentSeasonIsOverHint = isCurrentYearsSeasonJustOver()
                )
            } else {
                CountDownState.Over
            }
        }

    private val countdownWidgetSuggestionState: Flow<CountDownWidgetSuggestionState> = flow {
        emit(
            when (shouldShowCountdownWidgetSuggestion()) {
                true -> CountDownWidgetSuggestionState.Visible
                false -> CountDownWidgetSuggestionState.Hidden
            }
        )
    }

    private val nextOfficialEventState = getNextOfficialEvent()

    private val instagramPromotionState = openingCountDownState.map {
        when (it) {
            is CountDownState.Loading -> InstagramPromotionState.Hidden
            else -> InstagramPromotionState.Visible
        }
    }


    fun onCompleteAppUpdateTapped() {
        completeAppUpdate()
    }

    val state: StateFlow<ViewState> =
        combine(
            updateState,
            getRemoteMessages().onStart { emit(emptyList()) },
            openingCountDownState,
            countdownWidgetSuggestionState,
            nextOfficialEventState,
            instagramPromotionState,
            ::ViewState
        ).stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = ViewState()
        )

    data class ViewState(
        val updateState: UpdateState = UpdateState.Hidden,
        val messages: List<Message> = emptyList(),
        val openingCountDownState: CountDownState = CountDownState.Loading,
        val countdownWidgetSuggestionState: CountDownWidgetSuggestionState = CountDownWidgetSuggestionState.Hidden,
        val nextOfficialEventState: GetNextOfficialEventUseCase.Result = GetNextOfficialEventUseCase.Result.None,
        val instagramPromotionState: InstagramPromotionState = InstagramPromotionState.Hidden,
    )

    sealed class CountDownWidgetSuggestionState {
        object Hidden : CountDownWidgetSuggestionState()
        object Visible : CountDownWidgetSuggestionState()
    }

    sealed class CountDownState {
        object Loading : CountDownState()
        data class CountingDown(
            val daysLeft: Int,
            val hoursLeft: Int,
            val minutesLeft: Int,
            val year: Int,
            val showCurrentSeasonIsOverHint: Boolean
        ) : CountDownState()

        object Over : CountDownState()
    }

    sealed interface InstagramPromotionState {
        object Hidden : InstagramPromotionState
        object Visible : InstagramPromotionState
    }
}
