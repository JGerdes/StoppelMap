package com.jonasgerdes.stoppelmap.domain.processor

import com.jonasgerdes.mvi.BaseAction
import com.jonasgerdes.mvi.BaseOperation
import com.jonasgerdes.mvi.BaseResult
import com.jonasgerdes.stoppelmap.inject
import com.jonasgerdes.stoppelmap.model.news.DynamicDatabase
import com.jonasgerdes.stoppelmap.model.news.VersionMessage
import com.jonasgerdes.stoppelmap.model.versioning.Message
import com.jonasgerdes.stoppelmap.util.versioning.VersionProvider
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers


class Versioner
    : BaseOperation<Versioner.Action>(Action::class.java) {

    private val versionProvider: VersionProvider by inject()
    private val database: DynamicDatabase by inject()

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
                        database.versionMessages().removeUnseen()
                        it.messages?.forEach {
                            database.versionMessages().insertMessage(VersionMessage(
                                    slug = it.slug,
                                    title = it.title,
                                    body = it.message,
                                    versions = it.versions,
                                    showAlways = it.showAlways
                            ))
                        }
                        it.latest?.let {
                            if (it.code > versionProvider.getCurrentVersionCode()) {
                                database.versionMessages().insertMessage(VersionMessage(
                                        slug = "${VersionMessage.TYPE_UPDATE}#${it.code}",
                                        showAlways = true,
                                        importance = 10
                                ))
                            }
                        }


                        Result.ShowMessages(
                                messages = database.versionMessages().getUnseen()
                                        .sortedByDescending { it.importance },
                                newVersionAvailable = it.latest?.let
                                {
                                    it.code > versionProvider.getCurrentVersionCode()
                                } ?: false
                        )
                    }

    private fun markMessageAsRead(action: Action.MarkMessageRead) =
            Observable.just(action.messageSlug)
                    .doOnNext { database.versionMessages().markAsSeen(it) }
                    .map { Result.MarkAsReadResult }

    sealed class Action : BaseAction {
        object CheckForUpdates : Action()
        data class MarkMessageRead(val messageSlug: String) : Action()
    }

    sealed class Result : BaseResult {
        object MarkAsReadResult : Result()
        data class ShowMessages(val newVersionAvailable: Boolean,
                                val messages: List<VersionMessage>) : Result()
    }
}