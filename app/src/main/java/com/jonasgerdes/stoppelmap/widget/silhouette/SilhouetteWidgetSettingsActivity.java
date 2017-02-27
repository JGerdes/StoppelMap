package com.jonasgerdes.stoppelmap.widget.silhouette;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v7.graphics.Palette;

import com.jonasgerdes.stoppelmap.util.BitmapUtil;
import com.jonasgerdes.stoppelmap.widget.AbstractWidgetSettingsActivity;
import com.jonasgerdes.stoppelmap.widget.options.ColorOptionPage;
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

        final ColorOptionPage colorOptionPage = new ColorOptionPage().setDefaultColor(DEFAULT_COLOR);
        Bitmap wallpaperBitmap = BitmapUtil.drawableToBitmap(getWallpaperDrawable());
        Palette.from(wallpaperBitmap).generate(new Palette.PaletteAsyncListener() {

            @Override
            public void onGenerated(Palette palette) {
                colorOptionPage.setPalette(palette);
            }
        });

        pages.add(colorOptionPage);
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
