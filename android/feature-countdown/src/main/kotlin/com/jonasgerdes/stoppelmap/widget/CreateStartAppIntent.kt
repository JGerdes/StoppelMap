package com.jonasgerdes.stoppelmap.widget

import android.content.Intent

fun interface CreateStartAppIntentUseCase {

    operator fun invoke(): Intent
}