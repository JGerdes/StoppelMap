package com.jonasgerdes.stoppelmap.widget.heart;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.graphics.Palette;

import com.jonasgerdes.stoppelmap.R;
import com.jonasgerdes.stoppelmap.util.BitmapUtil;
import com.jonasgerdes.stoppelmap.widget.AbstractWidgetSettingsActivity;
import com.jonasgerdes.stoppelmap.widget.options.ColorOptionPage;
import com.jonasgerdes.stoppelmap.widget.options.HourOptionPage;
import com.jonasgerdes.stoppelmap.widget.options.OptionPage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jonas on 31.01.2017.
 */

public class GingerbreadHeartWidgetSettingsActivity extends AbstractWidgetSettingsActivity {

    public static final float MAX_LIGHTNESS = 0.8f;
    private static final int DEFAULT_COLOR = Color.parseColor("#7d56c2");
    public static final float MIN_LIGHTNESS = 0.2f;


    @Override
    protected String getWidgetName() {
        return "Countdown-Widget";
    }

    @Override
    protected List<OptionPage> getOptionPages() {
        List<OptionPage> pages = new ArrayList<>();

        final ColorOptionPage colorOptionPage = new ColorOptionPage()
                .setDefaultColor(DEFAULT_COLOR)
                .setSelectableColors(
                        0xff7d56c2,
                        ContextCompat.getColor(this, R.color.colorPrimary),
                        0xff2196f3,
                        0xff279056,
                        0xfff57f17,
                        ContextCompat.getColor(this, R.color.colorAccent)
                )
                .setLimits(MIN_LIGHTNESS, MAX_LIGHTNESS);
        Bitmap wallpaperBitmap = BitmapUtil.drawableToBitmap(getWallpaperDrawable());
        Palette.from(wallpaperBitmap).generate(new Palette.PaletteAsyncListener() {

            @Override
            public void onGenerated(Palette palette) {
                colorOptionPage.setPalette(palette);
            }
        });

        pages.add(colorOptionPage);
        pages.add(new HourOptionPage());
        return pages;
    }

    @Override
    protected GingerbreadHeartPreview createPreview() {
        GingerbreadHeartPreview preview = new GingerbreadHeartPreview(this);
        preview.setColorsBy(DEFAULT_COLOR);
        return preview;
    }

    @Override
    protected void initWithWidgetId(int appWidgetId) {

    }
}
