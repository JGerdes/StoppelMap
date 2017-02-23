package com.jonasgerdes.stoppelmap.widget.heart;

import android.app.WallpaperManager;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RemoteViews;

import com.jonasgerdes.stoppelmap.R;
import com.jonasgerdes.stoppelmap.util.BitmapUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by jonas on 31.01.2017.
 */

public class GingerbreadHeartWidgetSettingsActivity extends AppCompatActivity {

    private static final String TAG = "GingerbreadHeartWidgetS";


    @BindView(R.id.previewBackground)
    ImageView mPreviewBackground;

    @BindView(R.id.preview)
    GingerbreadHeartPreview mPreview;

    @BindView(R.id.options_pager)
    ViewPager mOptionsPager;

    @BindView(R.id.fab)
    FloatingActionButton mFab;

    private boolean mIsFabVisible = true;


    private int mAppWidgetId;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.widget_settings_gingerbread_heart);
        ButterKnife.bind(this);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("StoppelMap");
        getSupportActionBar().setSubtitle("Countdown-Widget konfigurieren");


        WallpaperManager wallpaperManager = WallpaperManager.getInstance(this);
        Drawable wallpaperDrawable = wallpaperManager.getDrawable();
        mPreviewBackground.setImageDrawable(wallpaperDrawable);

        int defaultColor = Color.parseColor("#7d56c2");

        final ColorOptionPage colorOptionPage = new ColorOptionPage().setDefaultColor(defaultColor);

        mOptionsPager.setAdapter(
                new OptionsPagerAdapter(
                        getSupportFragmentManager(),
                        colorOptionPage
                )
        );

        mOptionsPager.setOffscreenPageLimit(5);

        mPreview.setColorsBy(defaultColor);
        mPreview.update();

        mOptionsPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Log.d(TAG, "onPageScrolled() called with: position = [" + position + "], positionOffset = [" + positionOffset + "], positionOffsetPixels = [" + positionOffsetPixels + "]");
                if (positionOffsetPixels > 5 && mIsFabVisible) {
                    mIsFabVisible = false;
                    mFab.hide();
                }
                if (positionOffsetPixels < 5 && !mIsFabVisible) {
                    mIsFabVisible = true;
                    mFab.show();
                }
            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        Bitmap wallpaperBitmap = BitmapUtil.drawableToBitmap(wallpaperDrawable);
        Palette.from(wallpaperBitmap).generate(new Palette.PaletteAsyncListener() {

            @Override
            public void onGenerated(Palette palette) {
                colorOptionPage.setPalette(palette);
            }
        });

    }


    @OnClick(R.id.fab)
    void saveSettings() {

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(getBaseContext());
        RemoteViews views = new GingerbreadHeartWidgetProvider().initWidget(getBaseContext());

        mPreview.applyToWidget(views);

        appWidgetManager.updateAppWidget(mAppWidgetId, views);

        Intent resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
        setResult(RESULT_OK, resultValue);
        finish();
    }

    public WidgetPreview getWidgetPreview() {
        return mPreview;
    }
}
