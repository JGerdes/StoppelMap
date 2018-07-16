package com.jonasgerdes.stoppelmap.domain

import android.arch.lifecycle.ViewModel
import android.util.Log
import com.jakewharton.rxrelay2.PublishRelay
import com.jonasgerdes.mvi.Flow
import com.jonasgerdes.mvi.interpret
import com.jonasgerdes.mvi.process
import com.jonasgerdes.stoppelmap.domain.MainEvent.*
import com.jonasgerdes.stoppelmap.domain.processor.*
import io.reactivex.Observable

/**
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 08.05.2018
 */
class MainViewModel : ViewModel() {

    val events = PublishRelay.create<MainEvent>()
    val state: Observable<MainState> by lazy {
        flow.start(events.startWith(MainEvent.InitialEvent())).replay(1).autoConnect()
    }

    private val flow = Flow<MainEvent, MainState>(interpret {
        when (it) {
            is InitialEvent -> Versioner.Action.CheckForUpdates and
                    FeedProvider.Action() and
                    TransportationProvider.Action()
            is MapEvent.MapMoved -> MapHighlighter.Action.SelectNothing
            is MapEvent.SearchFieldClickedEvent
            -> MapSearchToggle.Action(true) and MapSearch.Action.Refresh()
            is MapEvent.OnBackPressEvent
            -> MapSearchToggle.Action(false)
            MapEvent.MapClickedEvent -> MapHighlighter.Action.SelectNothing
            is MapEvent.MapItemClickedEvent -> MapHighlighter.Action.StallSelect(it.slug) and MapHighlighter.Action.HighlightCard(0)
            is MapEvent.QueryEntered -> MapSearch.Action.Search(it.query)
            is FeedEvent.ReloadTriggered -> FeedItemLoader.Action()
            is FeedEvent.ItemClicked -> FeedItemInteractor.Action.OnFeedItemSelect(it.url)
            is MapEvent.SearchResultClicked ->
                MapHighlighter.Action.ResultSelect(it.searchResultId) and
                        MapSearchToggle.Action(false) and
                        MapHighlighter.Action.HighlightCard(0)
            is MapEvent.StallCardSelected ->
                MapHighlighter.Action.HighlightCard(it.cardIndex)
            is MainEvent.MessageRead -> TODO()
        }
    }, process(
            Versioner(),
            MapSearchToggle(),
            MapHighlighter(),
            MapSearch(),
            FeedProvider(),
            FeedItemLoader(),
            FeedItemInteractor(),
            TransportationProvider()
    ),
            MainReducer(),
            { Log.d("Flow", it) }
    )
}