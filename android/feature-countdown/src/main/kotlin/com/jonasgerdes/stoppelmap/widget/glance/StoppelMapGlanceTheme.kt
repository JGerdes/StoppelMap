package com.jonasgerdes.stoppelmap.widget.glance

import androidx.annotation.FontRes
import androidx.compose.runtime.Stable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.jonasgerdes.stoppelmap.countdown.R

@Stable
object StoppelMapGlanceTheme {

    val typography = Typography(
        title = TextStyle(
            size = 36.dp,
            font = R.font.roboto_slab_light,
        ),
        subTitle = TextStyle(
            size = 16.dp,
            font = R.font.roboto_slab_regular,
        ),
        body = TextStyle(
            size = 12.dp,
            font = R.font.roboto_slab_regular,
        )
    )

    @Stable
    data class Typography(
        val title: TextStyle,
        val subTitle: TextStyle,
        val body: TextStyle,
    )

    @Stable
    class TextStyle(
        val size: Dp,
        @FontRes val font: Int,
    )
}