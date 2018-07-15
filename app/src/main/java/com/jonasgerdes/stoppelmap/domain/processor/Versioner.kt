package com.jonasgerdes.stoppelmap.domain.processor

import com.jonasgerdes.mvi.BaseAction
import com.jonasgerdes.mvi.BaseOperation
import com.jonasgerdes.mvi.BaseResult
import com.jonasgerdes.stoppelmap.inject
import com.jonasgerdes.stoppelmap.model.versioning.Message
import com.jonasgerdes.stoppelmap.util.versioning.VersionProvider
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers


class Versioner
    : BaseOperation<Versioner.Action>(Action::class.java) {

    private val versionProvider: VersionProvider by inject()

    override fun execute(action: Observable<Action>):
            Observable<BaseResult> = action.flatMap {
        when (it) {
            is Action.MarkMessageRead -> markMessageAsRead(it)
            is Action.CheckForUpdates -> checkForUpdates(it)
        }
    }

    private fun checkForUpdates(action: Action.CheckForUpdates) =
            versionProvider.requestVersionInfo()
                    .subscribeOn(Schedulers.io())
                    .map {
                        Result.ShowMessages(
                                messages = it.messages?.filter {
                                    versionProvider.getHasMessageBeShown(it)
                                } ?: emptyList(),
                                newVersionAvailable = it.latest?.let
                                {
                                    it.code > versionProvider.getCurrentVersionCode()
                                } ?: false
                        )
                    }

    private fun markMessageAsRead(action: Action.MarkMessageRead): Observable<Result.ShowMessages> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    sealed class Action : BaseAction {
        object CheckForUpdates : Action()
        data class MarkMessageRead(val messageSlug: String) : Action()
    }

    sealed class Result : BaseResult {
        data class ShowMessages(val newVersionAvailable: Boolean,
                                val messages: List<Message>) : Result()
    }
}