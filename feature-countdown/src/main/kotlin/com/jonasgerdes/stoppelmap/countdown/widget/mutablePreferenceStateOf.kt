package com.jonasgerdes.stoppelmap.countdown.widget

import android.content.SharedPreferences
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

fun <T> mutablePreferenceStateOf(
    sharedPreferences: SharedPreferences,
    loadFromPreferences: (SharedPreferences) -> T,
    saveToPreferences: (SharedPreferences, T) -> Unit
): MutableState<T> = MutablePreferenceState(
    sharedPreferences,
    loadFromPreferences,
    saveToPreferences
)


class MutablePreferenceState<T>(
    private val sharedPreferences: SharedPreferences,
    loadFromPreferences: (SharedPreferences) -> T,
    private val saveToPreferences: (SharedPreferences, T) -> Unit
) : MutableState<T> {

    private var mutableState = mutableStateOf(loadFromPreferences(sharedPreferences))
    override var value: T
        get() = mutableState.value
        set(value) {
            saveToPreferences(sharedPreferences, value)
            mutableState.value = value
        }

    override fun component1() = value

    override fun component2(): (T) -> Unit = { value = it }
}
