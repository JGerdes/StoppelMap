package com.jonasgerdes.stoppelmap.widget.silhouette;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.graphics.Palette;

import com.jonasgerdes.stoppelmap.R;
import com.jonasgerdes.stoppelmap.util.BitmapUtil;
import com.jonasgerdes.stoppelmap.widget.AbstractWidgetSettingsActivity;
import com.jonasgerdes.stoppelmap.widget.options.ActionOptionPage;
import com.jonasgerdes.stoppelmap.widget.options.ColorOptionPage;
import com.jonasgerdes.stoppelmap.widget.options.FontColorOptionPage;
import com.jonasgerdes.stoppelmap.widget.options.OptionPage;
import com.jonasgerdes.stoppelmap.widget.options.TextOptionPage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jonas on 31.01.2017.
 */

public class SilhouetteWidgetSettingsActivity extends AbstractWidgetSettingsActivity {

    private static final int DEFAULT_COLOR = Color.BLACK;
    //Don't show pure black as suggestion from wallaper (since black is return if no color found)
    public static final float MIN_LIGHTNESS = 0.1f;
    public static final float MAX_LIGHTNESS = 1f;


    @Override
    protected String getWidgetName() {
        return "Countdown-Widget";
    }

    @Override
    protected List<OptionPage> getOptionPages() {
        List<OptionPage> pages = new ArrayList<>();

        final ColorOptionPage colorOptionPage =
                new ColorOptionPage()
                        .setDefaultColor(DEFAULT_COLOR)
                        .setSelectableColors(
                                0xff000000,
                                ContextCompat.getColor(this, R.color.colorPrimaryDarker),
                                0xff10525e,
                                0xff311005,
                                0xfff4f4f4
                        )
                        .setLimits(MIN_LIGHTNESS, MAX_LIGHTNESS);

        final FontColorOptionPage fontColorOptionPage =
                new FontColorOptionPage()
                        .setDefaultColor(SilhouetteWidgetProvider.DEFAULT_FONT_COLOR)
                        .setSelectableColors(
                                0xfff4f4f4,
                                0xff000000
                        )
                        .setLimits(MIN_LIGHTNESS, MAX_LIGHTNESS);

        Bitmap wallpaperBitmap = BitmapUtil.drawableToBitmap(getWallpaperDrawable());
        Palette.from(wallpaperBitmap).generate(new Palette.PaletteAsyncListener() {

            @Override
            public void onGenerated(Palette palette) {
                colorOptionPage.setPalette(palette);
                fontColorOptionPage.setPalette(palette);
            }
        });

        pages.add(colorOptionPage);
        pages.add(fontColorOptionPage);
        pages.add(new TextOptionPage());
        pages.add(new ActionOptionPage());
        return pages;
    }

    @Override
    protected SilhouettePreview createPreview() {
        SilhouettePreview preview = new SilhouettePreview(this);
        preview.setColorsBy(DEFAULT_COLOR);
        return preview;
    }
}
