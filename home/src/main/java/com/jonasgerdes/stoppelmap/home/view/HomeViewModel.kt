package com.jonasgerdes.stoppelmap.home.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jonasgerdes.stoppelmap.home.usescase.GetCountdownUseCase
import com.jonasgerdes.stoppelmap.home.usescase.GetCountdownUseCase.CountdownResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.threeten.bp.Duration
import javax.inject.Inject

@FlowPreview
class HomeViewModel @Inject constructor(
    private val getCountdown: GetCountdownUseCase
) : ViewModel(), UpdatingViewModel by UpdatingViewModel.DefaultImplementation() {

    private val updateInterval = Duration.ofMillis(1000)

    private val _cards = MutableLiveData<List<HomeCard>>()
    val cards: LiveData<List<HomeCard>> get() = _cards

    init {
        updateCards(getCountdown())

        onStartUpdates {
            launch(Dispatchers.IO) {
                getCountdown.asFlow(updateInterval).collect { result ->
                    updateCards(result)
                }
            }
        }
    }

    private fun updateCards(countdownResult: CountdownResult) {
        val cardList = mutableListOf<HomeCard>()

        cardList.add(RegulationsCard)

        //add countdown widget to top if there is any time left until start
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