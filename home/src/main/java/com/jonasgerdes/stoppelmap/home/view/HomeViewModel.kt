package com.jonasgerdes.stoppelmap.home.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jonasgerdes.stoppelmap.home.usescase.GetCountdownUseCase
import com.jonasgerdes.stoppelmap.home.usescase.GetCountdownUseCase.CountdownResult
import com.jonasgerdes.stoppelmap.home.usescase.GetFireworkCountdownUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.threeten.bp.Duration
import javax.inject.Inject

@FlowPreview
class HomeViewModel @Inject constructor(
    private val getCountdown: GetCountdownUseCase,
    private val getFireworkCountdown: GetFireworkCountdownUseCase
) : ViewModel(), UpdatingViewModel by UpdatingViewModel.DefaultImplementation() {

    private val updateInterval = Duration.ofMillis(1000)

    private val _cards = MutableLiveData<List<HomeCard>>()
    val cards: LiveData<List<HomeCard>> get() = _cards

    var countdownResult: GetCountdownUseCase.CountdownResult? = null
    var fireworkCountdownResult: GetFireworkCountdownUseCase.CountdownResult? = null

    init {
        updateCards()

        onStartUpdates {
            launch(Dispatchers.IO) {
                getCountdown.asFlow(updateInterval).collect { result ->
                    countdownResult = result
                    updateCards()
                }
            }
            launch(Dispatchers.IO) {
                getFireworkCountdown.asFlow(updateInterval).collect { result ->
                    fireworkCountdownResult = result
                    updateCards()
                }
            }
        }
    }

    private fun updateCards() {
        if (countdownResult == null || fireworkCountdownResult == null) {
            return
        }
        val cardList = mutableListOf<HomeCard>()

        when (val fireworkCountdownResult = fireworkCountdownResult) {
            is GetFireworkCountdownUseCase.CountdownResult.Countdown -> {
                cardList.add(FireworkCountdownCard(fireworkCountdownResult.timeLeft))
            }
            is GetFireworkCountdownUseCase.CountdownResult.Today -> {
                cardList.add(FireworkCountdownCard())
            }

        }

        cardList.add(RegulationsCard)

        //add countdown widget to top if there is any time left until start
        val countdownResult = this.countdownResult
        if (countdownResult is CountdownResult.Countdown) {
            cardList.add(CountdownCard(countdownResult.timeLeft))
        }

        //if there aren't any cards yet, show info that there will be soon
        if (cardList.isEmpty()) {
            cardList.add(MoreCardsInfoCard)
        }

        _cards.postValue(cardList)
    }


}