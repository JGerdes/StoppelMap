package com.jonasgerdes.stoppelmap

import com.jonasgerdes.stoppelmap.greeting.Greeting
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class KoinDependencies : KoinComponent {
    val greeting: Greeting by inject()
}