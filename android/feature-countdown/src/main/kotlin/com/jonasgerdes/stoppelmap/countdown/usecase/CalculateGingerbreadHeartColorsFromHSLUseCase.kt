package com.jonasgerdes.stoppelmap.countdown.usecase

import androidx.core.graphics.ColorUtils

class CalculateGingerbreadHeartColorsFromHSLUseCase {

    operator fun invoke(
        hue: Float,
        saturation: Float,
        brightness: Float
    ) =
        Result(
            color1 = ColorUtils.HSLToColor(
                floatArrayOf(
                    hue,
                    (saturation - 0.4f).coerceAtLeast(0.1f),
                    0.75f
                )
            ),
            color2 = ColorUtils.HSLToColor(
                floatArrayOf(
                    hue,
                    saturation,
                    brightness
                )
            ),
            color3 = ColorUtils.HSLToColor(
                floatArrayOf(
                    hue,
                    (saturation + 0.3f).coerceAtMost(1f),
                    (brightness - 0.3f).coerceAtLeast(0.1f)
                )
            )
        )

    data class Result(
        val color1: Int,
        val color2: Int,
        val color3: Int,
    )
}
