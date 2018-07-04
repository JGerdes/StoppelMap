package com.jonasgerdes.stoppelmap.domain.processor

import android.util.Log
import com.jonasgerdes.mvi.BaseAction
import com.jonasgerdes.mvi.BaseOperation
import com.jonasgerdes.mvi.BaseResult
import com.jonasgerdes.stoppelmap.inject
import com.jonasgerdes.stoppelmap.model.map.StoppelMapDatabase
import com.jonasgerdes.stoppelmap.model.transportation.Route
import io.reactivex.Observable
import io.reactivex.rxkotlin.toObservable

class TransportationProvider
    : BaseOperation<TransportationProvider.Action>(Action::class.java) {

    private val database: StoppelMapDatabase by inject()

    override fun execute(action: Observable<Action>):
            Observable<BaseResult> = action.switchMap {
        database.routes()
                .getAll()
                .map { Result(it) }
                .toObservable()
    }

    class Action : BaseAction

    data class Result(val routes: List<Route>) : BaseResult
}