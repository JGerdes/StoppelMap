package com.jonasgerdes.stoppelmap.home.view

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel

interface UpdatingViewModel {

    fun startUpdates()
    fun stopUpdates()

    fun onStartUpdates(block: CoroutineScope.() -> Unit)

    class DefaultImplementation : UpdatingViewModel {

        var updateScope: CoroutineScope? = null
        var onStartUpdates: (CoroutineScope.() -> Unit)? = null

        override fun startUpdates() {
            stopUpdates()
            updateScope = CoroutineScope(SupervisorJob() + Dispatchers.Main).apply {
                onStartUpdates?.let { block -> block() }
            }
        }

        override fun stopUpdates() {
            updateScope?.also { it.cancel() }
            updateScope = null
        }

        override fun onStartUpdates(block: CoroutineScope.() -> Unit) {
            onStartUpdates = block
        }


    }
}