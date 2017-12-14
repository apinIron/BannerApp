package com.iron.view.banner;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Banner的适配器
 *
 * @author iron
 *         created at 2017/11/4
 */
public abstract class BannerAdapter<T> extends PagerAdapter {

    protected Banner mBanner;
    protected List<T> mData;

    private int mScrollState = -1;

    private Banner.OnItemClickListener mListener;

    public BannerAdapter(List<T> data) {
        if (data == null) data = new ArrayList<>();
        mData = data;
    }

    public void bindBanner(Banner banner){
        mBanner = banner;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        //如果实际数量大于1个，如果小于一个就在最前面和最后面添加view
        final int temp = getRealityPosition(position);
        //获取界面
        View view = getItemView(temp, mData.get(temp));
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    mListener.onItemClick(view, temp);
                }
            }
        });
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public void finishUpdate(ViewGroup container) {
        //如果已经滚动完毕并更新完毕,并且数量多余1个
        if (mScrollState == ViewPager.SCROLL_STATE_IDLE && getRealityCount() > 1) {
            ViewPager pager = (ViewPager) container;
            int position = pager.getCurrentItem();
            //如果移动到最前面的View
            if (position == 0) {
                //则移动到倒数第二个界面
                //最前面的界面和倒数第二个界面相同，为了实现无限循环
                pager.setCurrentItem(getCount() - 2, false);
            }
            if (position == getCount() - 1) {
                pager.setCurrentItem(1, false);
            }
            //重置状态
            mScrollState = -1;
        }
    }

    @Override
    public int getCount() {
        return mData.size() > 1 ? mData.size() + 2 : mData.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    void setScrollState(int state) {
        mScrollState = state;
    }

    int getRealityCount() {
        return mData.size();
    }

    int getDisplayPosition(int position) {
        return ++position;
    }

    int getRealityPosition(int position) {
        //如果是第一个位置，就插入最后一个View
        if (position == 0) return getRealityCount() - 1;
            //如果是最后一个位置，就插入第一个View
        else if (position == getCount() - 1) return 0;
        else return position - 1;
    }

    void setOnItemClickListener(Banner.OnItemClickListener listener) {
        mListener = listener;
    }

    /**
     * 获取指定位置的界面
     *
     * @param position 位置
     * @return 界面
     */
    protected abstract View getItemView(int position, T t);
}
