package com.jonasgerdes.stoppelmap.home.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jonasgerdes.stoppelmap.home.usescase.GetCountdownUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.threeten.bp.Duration
import org.threeten.bp.LocalDateTime
import org.threeten.bp.Month

@FlowPreview
class HomeViewModel(
    private val getCountdown: GetCountdownUseCase
) : ViewModel(), UpdatingViewModel by UpdatingViewModel.DefaultImplementation() {

    private val _cards = MutableLiveData<List<HomeCard>>()
    val cards: LiveData<List<HomeCard>> get() = _cards

    init {
        _cards.postValue(
            listOf(
                MoreCardsInfoCard,
                CountdownCard(Duration.between(LocalDateTime.now(), LocalDateTime.of(2019, Month.AUGUST, 15, 18, 0, 0)))
            )
        )

        onStartUpdates {
            launch(Dispatchers.IO) {
                getCountdown().collect { duration ->
                    updateCards(duration)
                }
            }
        }
    }

    fun updateCards(countdownDuration: Duration) {
        _cards.postValue(
            listOf(
                MoreCardsInfoCard,
                CountdownCard(countdownDuration)
            )
        )
    }


}