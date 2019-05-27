package com.jonasgerdes.stoppelmap.news.view

import android.os.Bundle
import android.view.View
import androidx.core.view.updatePadding
import com.jonasgerdes.stoppelmap.core.util.dp
import com.jonasgerdes.stoppelmap.core.widget.BaseFragment
import com.jonasgerdes.stoppelmap.news.R
import kotlinx.android.synthetic.main.fragment_news.*

class NewsFragment : BaseFragment(R.layout.fragment_news) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbar.setOnApplyWindowInsetsListener { v, insets ->
            insets.apply { toolbar.updatePadding(top = insets.systemWindowInsetTop) }
        }
        toolbar.requestApplyInsets()
    }
}