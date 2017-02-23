package com.jonasgerdes.stoppelmap.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.LayoutRes;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.RemoteViews;

import butterknife.ButterKnife;

/**
 * Created by jonas on 23.02.2017.
 */
public abstract class WidgetPreview extends FrameLayout {
    public WidgetPreview(Context context) {
        super(context);
        init();

    }

    public WidgetPreview(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public WidgetPreview(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public WidgetPreview(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    protected void init() {
        LayoutInflater.from(getContext()).inflate(getLayout(), this);
        ButterKnife.bind(this);
    }

    @LayoutRes
    protected abstract int getLayout();

    public abstract RemoteViews createWidget();

    public abstract void update();

    public abstract void saveSettings(WidgetSettingsHelper appWidgetId);
}
