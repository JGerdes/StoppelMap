package com.jonasgerdes.stoppelmap.util.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

/**
 * Layout which enables swiping the activity to the right to close it, by using it as root view
 * in an activity. Actual action to execute when swiped to right (like finishing the activity) has
 * to be implemented in said activity inside an instance of {@link SwipeListener}.
 *
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 14.04.2017
 */
public class SwipeBackLayout extends FrameLayout {

    /**
     * Sensitivity of dragging. Should be between 0 for none at all and 1 for instance action.
     * Since we want to prevent user accidentally closing activity while scrolling vertically, a
     * value of one fourth feels right here.
     */
    public static final float DRAG_SENSITIVITY = 1 / 4f;

    /**
     * Threshold how far the activity should be dragged to the right until action is fired.
     * Since it should be possible for user to abort closing by swiping back or just releasing it,
     * small values would lead to a bad usability. To large numbers on the other hand would need
     * a lot of movement from user. Since people are lazy (and don't want to get cramps in their
     * thumbs), a value of one fourth is used here. After some testing this value felt acceptable.
     */
    public static final float BACK_ACTION_THRESHOLD = 1 / 4f;

    /**
     * Listener to execute code during swiping and when activity has been swiped all the way back.
     */
    public interface SwipeListener {

        void onFullSwipeBack();

        void onSwipe(float progress);

    }

    /**
     * DragHelper used to actual move all views inside this layout
     */
    private ViewDragHelper mDragHelper;

    /**
     * Listener called on swipe and when swiping completes
     */
    private SwipeListener mListener;

    public SwipeBackLayout(Context context) {
        super(context, null);
        init();
    }

    public SwipeBackLayout(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
        init();
    }

    public SwipeBackLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    /**
     * Init {@link #mDragHelper} to enable dragging of all child views. Only allows dragging
     * horizontally and to the right (left only to reach starting position).
     */
    private void init() {
        mDragHelper = ViewDragHelper.create(this, DRAG_SENSITIVITY, new ViewDragHelper.Callback() {
            @Override
            public boolean tryCaptureView(View child, int pointerId) {
                return true;
            }

            @Override
            public int clampViewPositionHorizontal(View child, int left, int dx) {
                //prevent dragging to left
                return Math.max(0, left);
            }

            @Override
            public int getViewHorizontalDragRange(View child) {
                return getWidth();
            }

            @Override
            public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
                //call listener during dragging with current progress (left is current position and
                //getWidth() is all the way on the right side so maximum which can be reached)
                if (mListener != null) {
                    mListener.onSwipe(left / (float) getWidth());
                }
            }

            @Override
            public void onViewReleased(View releasedChild, float xvel, float yvel) {
                //call listener when a specific distance is reached
                if (releasedChild.getLeft() > getWidth() * BACK_ACTION_THRESHOLD) {
                    if (mListener != null) {
                        mListener.onFullSwipeBack();
                    }
                } else {
                    //move child which was released back to its original position
                    mDragHelper.settleCapturedViewAt(0, 0);
                    //actually execute movement to original position
                    invalidate();
                }
            }
        });
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return mDragHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        mDragHelper.processTouchEvent(event);
        return true;
    }

    @Override
    public void computeScroll() {
        //prevent stopping animation during settling (moving back) of child view
        if (mDragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    public void setSwipeListener(SwipeListener listener) {
        mListener = listener;
    }
}