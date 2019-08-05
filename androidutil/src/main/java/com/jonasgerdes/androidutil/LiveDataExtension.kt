package com.jonasgerdes.androidutil

import androidx.lifecycle.MutableLiveData

fun <Item> MutableLiveData<Item>.withDefault(defaultValue: Item) = apply { value = defaultValue }