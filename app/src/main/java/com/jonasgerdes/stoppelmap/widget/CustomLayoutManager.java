package com.jonasgerdes.stoppelmap.widget;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.util.LayoutDirection;

public class CustomLayoutManager extends LinearLayoutManager {
    private int mParentWidth;
    private int mItemWidth;

    public CustomLayoutManager(Context context, int parentWidth, int itemWidth) {
        super(context, LinearLayoutManager.HORIZONTAL, false);
        mParentWidth = parentWidth;
        mItemWidth = itemWidth;
    }

    @Override
    public int getPaddingLeft() {
        return Math.round(mParentWidth / 2f - mItemWidth / 2f);
    }

    @Override
    public int getPaddingRight() {
        return getPaddingLeft();
    }
}