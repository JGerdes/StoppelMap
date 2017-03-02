package com.jonasgerdes.stoppelmap.views;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.View;

import com.jonasgerdes.stoppelmap.R;

import butterknife.ButterKnife;

/**
 * Created by jonas on 28.02.2017.
 */

public class OldDataNotice extends CardView implements View.OnClickListener {

    public static boolean sWasClosed = false;

    public OldDataNotice(Context context) {
        super(context);
    }

    public OldDataNotice(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public OldDataNotice(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (sWasClosed) {
            setVisibility(GONE);
            return;
        }
        View close = ButterKnife.findById(this, R.id.old_data_hint_close);
        if (close != null) {
            close.setOnClickListener(this);
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        if (hasWindowFocus && sWasClosed) {
            setVisibility(GONE);
        }
    }

    @Override
    public void onClick(View v) {
        setVisibility(View.GONE);
        sWasClosed = true;
    }
}
