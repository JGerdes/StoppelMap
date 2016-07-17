package com.jonasgerdes.stoppelmap.util;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Jonas on 17.07.2016.
 */
public class ViewUtil {

    public static float dpToPx(Context context, float dp) {
        Resources r = context.getResources();
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics());
        return px;
    }

    public static int dpToPx(Context context, int dp) {
        Resources r = context.getResources();
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics());
        return (int) px;
    }

    public static TextView getTextViewTitle(Toolbar toolbar){
        TextView textViewTitle = null;
        for(int i = 0; i<toolbar.getChildCount(); i++) {
            View view = toolbar.getChildAt(i);
            if(view instanceof TextView) {
                textViewTitle = (TextView) view;
                break;
            }
        }
        return textViewTitle;
    }
}