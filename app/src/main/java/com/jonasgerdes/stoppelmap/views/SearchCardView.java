package com.jonasgerdes.stoppelmap.views;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.annotation.IdRes;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.EditText;

import com.jonasgerdes.stoppelmap.R;
import com.jonasgerdes.stoppelmap.util.ViewUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Jonas on 22.07.2016.
 */
public class SearchCardView extends CardView {
    private static final long ANIMATION_DURATION = 300;

    private boolean mIsVisible;

    @BindView(R.id.back)
    View mBackButton;

    @BindView(R.id.clear)
    View mClearButton;

    @BindView(R.id.search_field)
    EditText mSearchField;

    @BindView(R.id.result_list)
    RecyclerView mResultList;

    private Toolbar mToolbar;
    private int mItemId;
    private Activity mActivity;
    private ResultAdapter mResultAdapter;

    public SearchCardView(Context context) {
        super(context);
    }

    public SearchCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SearchCardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setUpWith(Activity activity, Toolbar toolbar, @IdRes int itemId) {
        setVisibility(INVISIBLE);
        mActivity = activity;
        mToolbar = toolbar;
        mItemId = itemId;

        View content = LayoutInflater.from(getContext()).inflate(R.layout.search_card, this);
        ButterKnife.bind(this, content);

        mBackButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                hide();
            }
        });

        mClearButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                clearField();
            }
        });

        mResultList.setLayoutManager(new LinearLayoutManager(getContext()));

        mSearchField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (mResultAdapter != null && mIsVisible) {
                    mResultAdapter.onQueryChanged(s.toString());
                    mResultList.smoothScrollToPosition(0);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private void clearField() {
        mSearchField.setText("");
    }

    public void show() {
        if (mToolbar == null) {
            throw new IllegalStateException("SearchCardView hasn't been initialised yet");
        }
        mIsVisible = true;
        clearField();
        Animator anim = null;
        if (Build.VERSION.SDK_INT >= 21) {
            View menuItem = ViewUtil.getMenuItemViewFromToolbar(mToolbar, mItemId);
            int[] location = new int[2];
            menuItem.getLocationInWindow(location);
            int cx = location[0] + menuItem.getWidth() / 2;
            int cy = location[1] + menuItem.getHeight() / 2 - ViewUtil.dpToPx(getContext(), 24);
            int finalRadius = Math.max(this.getWidth(), this.getHeight());
            anim = ViewAnimationUtils.createCircularReveal(this, cx, cy, 0, finalRadius);
        } else {
            this.setAlpha(0);
            anim = ObjectAnimator.ofFloat(this, "alpha", 1);
            anim.setDuration(ANIMATION_DURATION);
        }
        anim.start();
        this.setVisibility(View.VISIBLE);
        mSearchField.requestFocus();
        ViewUtil.showKeyboard(mActivity);
    }

    public void hide() {
        if (!mIsVisible) {
            return;
        }
        mIsVisible = false;
        ViewUtil.hideKeyboard(mActivity);
        Animator anim = null;
        clearField();
        if (Build.VERSION.SDK_INT >= 21) {
            View menuItem = ViewUtil.getMenuItemViewFromToolbar(mToolbar, mItemId);
            if (menuItem == null) {
                return;
            }
            int[] location = new int[2];
            menuItem.getLocationInWindow(location);
            int cx = location[0] + menuItem.getWidth() / 2;
            int cy = location[1] + menuItem.getHeight() / 2 - ViewUtil.dpToPx(getContext(), 24);
            int radius = Math.max(this.getWidth(), this.getHeight());
            anim = ViewAnimationUtils.createCircularReveal(this, cx, cy, radius, 0);
        } else {
            this.setAlpha(0);
            anim = ObjectAnimator.ofFloat(this, "alpha", 1);
            anim.setDuration(ANIMATION_DURATION);
        }

        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                setVisibility(View.GONE);
            }
        });
        anim.start();
        mSearchField.clearFocus();
    }

    public boolean isVisible() {
        return mIsVisible;
    }


    public static abstract class ResultAdapter<VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {
        public abstract void onQueryChanged(String query);
    }

    public void setResultAdapter(ResultAdapter resultAdapter) {
        mResultAdapter = resultAdapter;
        mResultList.setAdapter(resultAdapter);
    }
}
