package com.jonasgerdes.stoppelmap.widget.silhouette;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.graphics.Palette;

import com.jonasgerdes.stoppelmap.R;
import com.jonasgerdes.stoppelmap.widget.AbstractWidgetSettingsActivity;
import com.jonasgerdes.stoppelmap.widget.Font;
import com.jonasgerdes.stoppelmap.widget.WidgetSettingsHelper;
import com.jonasgerdes.stoppelmap.widget.options.ActionOptionPage;
import com.jonasgerdes.stoppelmap.widget.options.ColorOptionPage;
import com.jonasgerdes.stoppelmap.widget.options.FontColorOptionPage;
import com.jonasgerdes.stoppelmap.widget.options.OptionPage;
import com.jonasgerdes.stoppelmap.widget.options.TextOptionPage;
import com.jonasgerdes.stoppelmap.widget.util.BitmapUtil;
import com.jonasgerdes.stoppelmap.widget.util.ContextUtil;

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
    private int mDefaultColor;
    private int mDefaultFontColor;
    private boolean mDefaultShowHours;
    private Font mDefaultFont;
    private int mDefaultAction;


    @Override
    protected String getWidgetName() {
        return "Countdown-Widget";
    }


    @Override
    protected List<OptionPage> getOptionPages() {
        List<OptionPage> pages = new ArrayList<>();

        final ColorOptionPage colorOptionPage =
                new ColorOptionPage()
                        .setDefaultColor(mDefaultColor)
                        .setSelectableColors(
                                0xff000000,
                                ContextUtil.getColorFromTheme(this, R.attr.colorPrimaryDark),
                                0xff10525e,
                                0xff311005,
                                0xfff4f4f4
                        )
                        .setLimits(MIN_LIGHTNESS, MAX_LIGHTNESS);

        final FontColorOptionPage fontColorOptionPage =
                new FontColorOptionPage()
                        .setDefaultColor(mDefaultFontColor)
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
        pages.add(TextOptionPage.newInstance(mDefaultFont, mDefaultShowHours));
        pages.add(ActionOptionPage.newInstance(mDefaultAction));
        return pages;
    }


    @Override
    protected void initWithWidgetId(int appWidgetId) {
        WidgetSettingsHelper settings = new WidgetSettingsHelper(this, appWidgetId);
        mDefaultColor = settings.getInt(SilhouetteWidgetProvider.SETTING_COLOR, DEFAULT_COLOR);
        mDefaultFontColor = settings.getInt(
                SilhouetteWidgetProvider.SETTING_FONT_COLOR,
                SilhouetteWidgetProvider.DEFAULT_FONT_COLOR
        );
        mDefaultFont = Font.fromStringOrNull(
                settings.getString(SilhouetteWidgetProvider.SETTING_FONT,
                        Font.RobotoSlab.getFileName())
        );
        mDefaultShowHours = settings.getBoolean(SilhouetteWidgetProvider.SETTING_SHOW_HOUR, false);
        mDefaultAction = settings.getInt(SilhouetteWidgetProvider.SETTING_ACTION,
                ActionOptionPage.ACTION_EDIT_WIDGET);
    }

    @Override
    protected SilhouettePreview createPreview() {
        SilhouettePreview preview = new SilhouettePreview(this);
        preview.setColorsBy(mDefaultColor);
        preview.setFontColor(mDefaultFontColor);
        preview.setFont(mDefaultFont);
        preview.setShowHours(mDefaultShowHours);
        return preview;
    }
}
