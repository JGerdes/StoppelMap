package com.jonasgerdes.stoppelmap.view.widget

import android.annotation.SuppressLint
import android.content.Context
import android.support.v7.widget.AppCompatEditText
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo.IME_ACTION_UNSPECIFIED
import android.widget.TextView
import com.jakewharton.rxbinding2.internal.Preconditions.checkMainThread
import com.jakewharton.rxbinding2.widget.TextViewEditorActionEvent
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.MainThreadDisposable

class ExtendedEditText @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : AppCompatEditText(context, attrs, defStyleAttr) {

    var preImeActionsListener: OnEditorActionListener? = null

    fun backPresses() = preImeEditorActions().filter {
        it.keyEvent()?.action == KeyEvent.ACTION_UP
                && it.keyEvent()?.keyCode == KeyEvent.KEYCODE_BACK
    }

    fun preImeEditorActions() = object : Observable<TextViewEditorActionEvent>() {
        @SuppressLint("RestrictedApi")
        override fun subscribeActual(observer: Observer<in TextViewEditorActionEvent>) {
            if (!checkMainThread(observer)) {
                return
            }
            val listener = Listener(observer)
            observer.onSubscribe(listener)
            preImeActionsListener = listener
        }

        inner class Listener(val observer: Observer<in TextViewEditorActionEvent>) : MainThreadDisposable(), OnEditorActionListener {
            override fun onEditorAction(view: TextView?, actionId: Int, event: KeyEvent?): Boolean {
                try {
                    if (!isDisposed && event != null && view != null) {
                        observer.onNext(TextViewEditorActionEvent.create(view, actionId, event))
                    }
                } catch (e: Exception) {
                    observer.onError(e)
                    dispose()
                }
                return false
            }

            override fun onDispose() {
                preImeActionsListener = null
            }

        }

    }

    override fun onKeyPreIme(keyCode: Int, event: KeyEvent?): Boolean {
        preImeActionsListener?.apply {
            onEditorAction(this@ExtendedEditText, IME_ACTION_UNSPECIFIED, event)
        }
        return super.onKeyPreIme(keyCode, event)
    }
}