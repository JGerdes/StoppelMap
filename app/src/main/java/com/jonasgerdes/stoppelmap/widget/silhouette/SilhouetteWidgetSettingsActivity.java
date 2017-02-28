package com.jonasgerdes.stoppelmap.widget.silhouette;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.graphics.Palette;

import com.jonasgerdes.stoppelmap.R;
import com.jonasgerdes.stoppelmap.util.BitmapUtil;
import com.jonasgerdes.stoppelmap.widget.AbstractWidgetSettingsActivity;
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
                        );
        final FontColorOptionPage fontColorOptionPage =
                new FontColorOptionPage()
                        .setDefaultColor(SilhouetteWidgetProvider.DEFAULT_FONT_COLOR)
                        .setSelectableColors(
                                0xfff4f4f4,
                                0xff000000
                        );

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
        return pages;
    }

    @Override
    protected SilhouettePreview createPreview() {
        SilhouettePreview preview = new SilhouettePreview(this);
        preview.setColorsBy(DEFAULT_COLOR);
        return preview;
    }
}
