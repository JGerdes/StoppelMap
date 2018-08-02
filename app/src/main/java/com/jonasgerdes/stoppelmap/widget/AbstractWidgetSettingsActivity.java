package com.jonasgerdes.stoppelmap.widget;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.WallpaperManager;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RemoteViews;

import com.jonasgerdes.stoppelmap.R;
import com.jonasgerdes.stoppelmap.widget.options.OptionPage;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.List;

/**
 * Created by jonas on 23.02.2017.
 */
@SuppressWarnings("ResultOfMethodCallIgnored")
public abstract class AbstractWidgetSettingsActivity extends AppCompatActivity {
    private static final String TAG = "AbstractWidgetSettingsA";
    ImageView mPreviewBackground;
    FrameLayout mPreviewHolder;
    ViewPager mOptionsPager;
    FloatingActionButton mFab;

    private WidgetPreview mPreview;
    private boolean mIsFabVisible = true;
    protected int mAppWidgetId;
    private int mCurrentItem;
    private OptionsPagerAdapter mOptionsAdapter;
    private RxPermissions mPermissions;

    @Nullable
    private Drawable mWallpaperDrawable = null;

    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.widget_settings_gingerbread_heart);

        mPermissions = new RxPermissions(this);

        mPreviewBackground = findViewById(R.id.previewBackground);
        mPreviewHolder = findViewById(R.id.preview);
        mOptionsPager = findViewById(R.id.options_pager);
        mFab = findViewById(R.id.fab);
        mFab.setOnClickListener(view -> onFabClicked());

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        Log.d(TAG, "onCreate: " + mAppWidgetId);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("StoppelMap");
        getSupportActionBar().setSubtitle(getWidgetName() + " konfigurieren");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            mPermissions.request(Manifest.permission.READ_EXTERNAL_STORAGE)
                    .subscribe(this::init);
        } else {
            init(true);
        }


    }

    private void init(boolean useWallpaper) {

        if (useWallpaper) {
            WallpaperManager wallpaperManager = WallpaperManager.getInstance(this);
            mWallpaperDrawable = wallpaperManager.getDrawable();
            mPreviewBackground.setImageDrawable(mWallpaperDrawable);
        } else {
            mPreviewBackground.setImageResource(R.drawable.bg_gradient_app_colors);
        }
        initWithWidgetId(mAppWidgetId);

        final List<OptionPage> mOptionsPages = getOptionPages();
        mOptionsAdapter = new OptionsPagerAdapter(
                getSupportFragmentManager(),
                mOptionsPages
        );
        mOptionsPager.setAdapter(mOptionsAdapter);

        mOptionsPager.setOffscreenPageLimit(mOptionsPages.size());


        mOptionsPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (positionOffsetPixels > 5 && mIsFabVisible) {
                    mIsFabVisible = false;
                    mFab.hide();
                }
                if (positionOffsetPixels < 5 && !mIsFabVisible) {
                    if (position < mOptionsAdapter.getCount() - 1) {
                        mFab.setImageResource(R.drawable.ic_arrow_forward_white_24dp);
                    } else {
                        mFab.setImageResource(R.drawable.ic_check_white_24dp);
                    }
                    mIsFabVisible = true;
                    mFab.show();
                }
            }

            @Override
            public void onPageSelected(int position) {
                mCurrentItem = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        if (mOptionsAdapter.getCount() > 1) {
            mFab.setImageResource(R.drawable.ic_arrow_forward_white_24dp);
        } else {
            mFab.setImageResource(R.drawable.ic_check_white_24dp);
        }

        initPreview();
    }

    @Override
    protected void onStop() {
        super.onStop();
        //finish so clicking another widget doesn't just reopen this activity
        finish();
    }

    private void initPreview() {
        mPreview = createPreview();
        mPreview.setLayoutParams(new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT)
        );
        mPreviewHolder.addView(mPreview);
        mPreview.setWidgetId(mAppWidgetId);
        mPreview.update();
    }

    @Nullable
    protected Drawable getWallpaperDrawable() {
        return mWallpaperDrawable;
    }

    void onFabClicked() {

        if (mCurrentItem < mOptionsAdapter.getCount() - 1) {
            mOptionsPager.setCurrentItem(mCurrentItem + 1);
        } else {
            //save widget
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(getBaseContext());
            mPreview.saveSettings(new WidgetSettingsHelper(getBaseContext(), mAppWidgetId));
            RemoteViews views = mPreview.createWidget();

            appWidgetManager.updateAppWidget(mAppWidgetId, views);

            Intent resultValue = new Intent();
            resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
            setResult(RESULT_OK, resultValue);
            finish();
        }
    }

    public WidgetPreview getWidgetPreview() {
        return mPreview;
    }

    protected abstract String getWidgetName();

    protected abstract List<OptionPage> getOptionPages();

    protected abstract WidgetPreview createPreview();

    protected abstract void initWithWidgetId(int appWidgetId);

}
