package com.jonasgerdes.stoppelmap.core.di

import android.annotation.SuppressLint
import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders

interface Injector {
    companion object {
        const val SERVICE_NAME = "com.jonasgerdes.stoppelmap.core.di.Injector"
    }

    val viewModelFactory: ViewModelProvider.Factory
}

@SuppressLint("WrongConstant")
fun Context.getInjector(): Injector =
    applicationContext.getSystemService(Injector.SERVICE_NAME) as Injector

inline fun <reified T : ViewModel> Fragment.viewModelFactory(): Lazy<T> = lazy {
    ViewModelProviders.of(this, requireContext().getInjector().viewModelFactory)
        .get(T::class.java)
}

inline fun <reified T : ViewModel> FragmentActivity.viewModelFactory(): Lazy<T> = lazy {
    ViewModelProviders.of(this, getInjector().viewModelFactory)
        .get(T::class.java)
}