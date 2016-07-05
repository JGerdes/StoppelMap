package com.jonasgerdes.stoppelmap.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.TextView;

import com.jonasgerdes.stoppelmap.StoppelMapApp;

/**
 * Created by Jonas on 03.07.2016.
 */
public class FontTextView extends TextView {
    public FontTextView(Context context) {
        super(context);
        setFont();
    }

    public FontTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFont();
    }

    public FontTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setFont();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public FontTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        setFont();
    }

    private void setFont() {
        if (getContext().getApplicationContext() instanceof StoppelMapApp) {
            Typeface typeface = ((StoppelMapApp) getContext().getApplicationContext())
                    .getMainTypeface();
            setTypeface(typeface);
        }
    }
}
