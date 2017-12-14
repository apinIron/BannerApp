package com.iron.view.banner;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.animation.Interpolator;
import android.widget.Scroller;

import java.lang.reflect.Field;

/**
 * 可设置速度的滚动
 *
 * @author iron
 *         created at 2017/11/4
 */
class FixedSpeedScroller extends Scroller {

    private int mScrollDuration = 800;
    private boolean mIsDrag;

    public FixedSpeedScroller(Context context) {
        super(context);
    }

    public FixedSpeedScroller(Context context, Interpolator interpolator) {
        super(context, interpolator);
    }

    public FixedSpeedScroller(Context context, Interpolator interpolator, boolean flywheel) {
        super(context, interpolator, flywheel);
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy, int duration) {
        duration = mScrollDuration;
        //如果是手动拖拽的，则更快速滚动
        if (mIsDrag) {
            duration /= 2;
            mIsDrag = false;
        }
        super.startScroll(startX, startY, dx, dy, duration);
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy) {
        startScroll(startX, startY, dx, dy, mScrollDuration);
    }

    void bindViewPager(ViewPager viewPager) {
        try {
            Field field = ViewPager.class.getDeclaredField("mScroller");
            field.setAccessible(true);
            field.set(viewPager, this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void setScrollDuration(int time) {
        mScrollDuration = time;
    }

    void setScrollState(int state) {
        if (state == ViewPager.SCROLL_STATE_DRAGGING) mIsDrag = true;
    }

}
