package com.jonasgerdes.stoppelmap.widget.heart;

/**
 * Created by jonas on 23.02.2017.
 */
public interface ColorableWidgetPreview extends WidgetPreview {
    void setColorsBy(int color);

    void setColors(int[] colors);
}
