package com.jonasgerdes.androidutil.widget

import android.content.Context
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo.IME_ACTION_UNSPECIFIED
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.widget.AppCompatEditText


typealias BackPressListener = () -> Unit
typealias FocusRegainedListener = () -> Unit

class ExtendedEditText @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatEditText(context, attrs, defStyleAttr) {

    var preImeActionsListener: OnEditorActionListener? = null
    private var onBackPressListener: BackPressListener? = null

    fun onBackPress(listener: BackPressListener) {
        onBackPressListener = listener
    }

    override fun onKeyPreIme(keyCode: Int, event: KeyEvent?): Boolean {
        preImeActionsListener?.apply {
            onEditorAction(this@ExtendedEditText, IME_ACTION_UNSPECIFIED, event)
        }
        onBackPressListener?.let { listener ->
            if (event?.action == KeyEvent.ACTION_UP && event.keyCode == KeyEvent.KEYCODE_BACK)
                listener()
        }
        return super.onKeyPreIme(keyCode, event)
    }

    fun showKeyboard() = post {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
        imm?.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
    }

}