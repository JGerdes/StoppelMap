package com.jonasgerdes.stoppelmap.widget;

import android.app.WallpaperManager;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RemoteViews;

import com.jonasgerdes.stoppelmap.R;
import com.jonasgerdes.stoppelmap.widget.options.OptionPage;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by jonas on 23.02.2017.
 */
public abstract class AbstractWidgetSettingsActivity extends AppCompatActivity {
    @BindView(R.id.previewBackground)
    ImageView mPreviewBackground;
    @BindView(R.id.preview)
    FrameLayout mPreviewHolder;
    @BindView(R.id.options_pager)
    ViewPager mOptionsPager;
    @BindView(R.id.fab)
    FloatingActionButton mFab;

    private WidgetPreview mPreview;
    private boolean mIsFabVisible = true;
    private int mAppWidgetId;
    private int mCurrentItem;
    private OptionsPagerAdapter mOptionsAdapter;

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
        getSupportActionBar().setSubtitle(getWidgetName() + " konfigurieren");


        WallpaperManager wallpaperManager = WallpaperManager.getInstance(this);
        Drawable wallpaperDrawable = wallpaperManager.getDrawable();
        mPreviewBackground.setImageDrawable(wallpaperDrawable);


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


        mPreview = createPreview();
        mPreview.setLayoutParams(new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT)
        );
        mPreviewHolder.addView(mPreview);
        // TODO: 23.02.2017 remove this line
        mPreview.update();

    }

    protected Drawable getWallpaperDrawable() {
        return mPreviewBackground.getDrawable();
    }

    @OnClick(R.id.fab)
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

}
