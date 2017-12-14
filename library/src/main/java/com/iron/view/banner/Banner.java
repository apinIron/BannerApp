package com.iron.view.banner;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Banner控件
 *
 * @author iron
 *         created at 2017/11/4
 */
public class Banner extends FrameLayout {

    private static final int MSG_CHANGE = 1000;

    //是否循环播放
    private boolean mIsAutoPlay = false;
    //播放间隔时间
    private long mIntervalTime = 5 * 1000;
    //当前显示的界面位置
    private int mPosition = 0;
    //适配器
    private BannerAdapter mAdapter;
    //控件
    private ViewPager mViewPager;
    private LinearLayout mDotLayout;
    private LayoutParams mDotLayoutParams;
    private FixedSpeedScroller mScroller;
    private int mDotSelectedResId;
    private int mDotNoSelectedResId;
    private int mTextColor;
    private float mTextSize;
    private int mTextMargin;
    //图片循环处理器
    private Handler mHandler;

    private static class MyHandler extends Handler {

        private Banner mView;

        MyHandler(Banner view) {
            mView = view;
        }

        @Override
        public void handleMessage(Message msg) {
            if (msg.what == MSG_CHANGE && mView.mIsAutoPlay) {
                mView.nextView();
            }
        }
    }

    public Banner(@NonNull Context context) {
        this(context, null);
    }

    public Banner(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Banner(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(attrs);
    }

    private void init(AttributeSet attrs) {

        mHandler = new MyHandler(this);

        //添加viewpager
        initViewPager();

        //初始化位置点布局
        initAttributeSet(attrs);
    }

    /**
     * 初始化ViewPager控件
     */
    private void initViewPager() {
        //创建控件
        mViewPager = new ViewPager(getContext());
        //配置布局参数
        LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams
                .WRAP_CONTENT);
        mViewPager.setLayoutParams(lp);
        this.addView(mViewPager);
        //监听手动改变事件
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                position = mAdapter.getRealityPosition(position);
                if (null != mDotLayout && mDotLayout.getChildCount() > position) {
                    //取消上一位置的圆点选中的状态
                    ((ImageView) mDotLayout.getChildAt(mPosition)).setImageResource(mDotNoSelectedResId);
                    //选中当前的位置的圆点选中状态
                    ((ImageView) mDotLayout.getChildAt(position)).setImageResource(mDotSelectedResId);
                }
                //更新位置
                mPosition = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                mAdapter.setScrollState(state);
                mScroller.setScrollState(state);
            }
        });

        mScroller = new FixedSpeedScroller(mViewPager.getContext(), new AccelerateInterpolator());
        mScroller.bindViewPager(mViewPager);
    }

    /**
     * 初始化位置点的布局
     *
     * @param attrs 属性
     */
    private void initAttributeSet(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.Banner);
        //创建布局
        mDotLayout = new LinearLayout(getContext());
        //圆点的图片资源
        mDotSelectedResId = typedArray.getResourceId(R.styleable.Banner_dot_selectedResource, R.drawable
                .dot_selected);
        mDotNoSelectedResId = typedArray.getResourceId(R.styleable.Banner_dot_notSelectedResource, R
                .drawable.dot_no_selected);
        //圆点的布局属性
        mDotLayoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams
                .WRAP_CONTENT);
        int size = (int) typedArray.getDimension(R.styleable.Banner_dot_size, getResources().getDimension(R
                .dimen.dot_size));
        mDotLayoutParams.width = size;
        mDotLayoutParams.height = size;
        mDotLayoutParams.leftMargin = (int) typedArray.getDimension(R.styleable.Banner_dot_interval,
                getResources().getDimension(R.dimen.dot_default_interval));
        //配置布局参数
        LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams
                .WRAP_CONTENT);
        lp.gravity = typedArray.getInt(R.styleable.Banner_dot_gravity, Gravity.BOTTOM | Gravity
                .CENTER_HORIZONTAL);
        //设置距离
        int defaultMargin = (int) getResources().getDimension(R.dimen.dot_default_margin);
        lp.bottomMargin = (int) typedArray.getDimension(R.styleable.Banner_dot_marginBottom, defaultMargin);
        lp.topMargin = (int) typedArray.getDimension(R.styleable.Banner_dot_marginTop, defaultMargin);
        lp.leftMargin = (int) typedArray.getDimension(R.styleable.Banner_dot_marginLeft, defaultMargin);
        lp.rightMargin = (int) typedArray.getDimension(R.styleable.Banner_dot_marginRight, defaultMargin);
        mDotLayout.setLayoutParams(lp);
        this.addView(mDotLayout);
        //文字大小
        mTextSize = typedArray.getDimension(R.styleable.Banner_text_size, getResources().getDimension(R
                .dimen.default_text_size));
        mTextColor = typedArray.getColor(R.styleable.Banner_text_color, Color.WHITE);
        mTextMargin = lp.bottomMargin + mDotLayoutParams.height;

        typedArray.recycle();
    }

    /**
     * 播放下一个界面
     */
    public void nextView() {
        //设置新的位置
        setCurrentPosition(mPosition + 1);
        sendAutoPlayMessage();
    }

    /**
     * 上一个界面
     */
    public void lastView() {
        //设置新的位置
        setCurrentPosition(mPosition - 1);
        sendAutoPlayMessage();
    }

    /**
     * 发送自动播放message
     */
    private void sendAutoPlayMessage() {
        //发送下一次切换的消息
        if (mIsAutoPlay) {
            mHandler.removeMessages(MSG_CHANGE);
            mHandler.sendEmptyMessageDelayed(MSG_CHANGE, mIntervalTime);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //停止循环播放
                mHandler.removeMessages(MSG_CHANGE);
                break;
            case MotionEvent.ACTION_UP:
                //开启循环播放
                sendAutoPlayMessage();
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * ViewPager设置适配器
     *
     * @param adapter 适配器
     * @return Banner控件
     */
    public Banner setAdapter(BannerAdapter adapter) {
        mAdapter = adapter;
        mAdapter.bindBanner(this);
        mViewPager.setAdapter(mAdapter);
        //设置为显示第一个
        mPosition = 0;
        setCurrentPosition(mPosition);
        //初始化底部圆点布局
        initDotLayout();

        return this;
    }

    /**
     * 初始化底部圆点布局
     */
    private void initDotLayout() {
        //如果还有圆点就全部删除
        if (mDotLayout.getChildCount() > 0) mDotLayout.removeAllViews();
        //添加圆点
        for (int i = 0; i < mAdapter.getRealityCount(); i++) {
            ImageView view = new ImageView(getContext());
            view.setImageResource(mDotNoSelectedResId);
            view.setLayoutParams(mDotLayoutParams);
            mDotLayout.addView(view);
        }
        //选中
        ((ImageView) mDotLayout.getChildAt(mPosition)).setImageResource(mDotSelectedResId);
    }

    /**
     * 设置当前显示视图
     *
     * @param position 位置
     */
    private void setCurrentPosition(int position) {
        position = mAdapter.getDisplayPosition(position);
        mViewPager.setCurrentItem(position);
    }

    /**
     * 开始循环播放
     */
    public void startAutoPlay() {
        //如果数量不足，不用自动播放
        if (mAdapter.getRealityCount() < 2) {
            return;
        }
        //如果没有在循环播放
        if (!mIsAutoPlay) {
            //标记为自动播放
            mIsAutoPlay = true;
            //发送message
            sendAutoPlayMessage();
        }
    }

    /**
     * 停止循环播放
     */
    public void stopAutoPlay() {
        if (mIsAutoPlay) {
            mIsAutoPlay = false;
            mHandler.removeMessages(MSG_CHANGE);
        }
    }

    /**
     * 设置自动播放时多久切换一个界面(默认5秒)
     *
     * @param millisecond 时长
     * @return Banner控件
     */
    public Banner setPlayIntervalTime(long millisecond) {
        mIntervalTime = millisecond;
        return this;
    }

    /**
     * 设置图片底部的圆点图片资源
     *
     * @param dotSelectedRes   选中状态
     * @param dotNoSelectedRes 非选中状态
     * @return Banner控件
     */
    public Banner setDotResource(int dotSelectedRes, int dotNoSelectedRes) {
        mDotSelectedResId = dotSelectedRes;
        mDotNoSelectedResId = dotNoSelectedRes;
        return this;
    }

    /**
     * 设置viewpage切换时的滚动速度（默认1秒）
     *
     * @param millisecond 切换图片时间
     * @return Banner控件
     */
    public Banner setScrollSpeed(int millisecond) {
        mScroller.setScrollDuration(millisecond);
        return this;
    }

    /**
     * 数据发生改变
     */
    public void notifyDataSetChanged() {
        mAdapter.notifyDataSetChanged();
        initDotLayout();
    }

    /**
     * 设置样式
     *
     * @param textView 控件
     */
    public void setTextStyle(TextView textView) {
        textView.setTextSize(mTextSize);
        textView.setTextColor(mTextColor);
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) textView.getLayoutParams();
        lp.setMargins(15, 0, 0, mTextMargin);
    }

    @Nullable
    @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        return new SavedState(superState, mPosition);
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());

        if (ss.position < 0 || ss.position > mAdapter.getRealityCount() - 1) {
            return;
        }

        if (mPosition != ss.position) {
            setCurrentPosition(ss.position);
        }

        mPosition = ss.position;
    }

    private static class SavedState extends BaseSavedState {

        private int position;

        SavedState(Parcel source) {
            super(source);
            position = source.readInt();
        }

        SavedState(Parcelable sourceState, int position) {
            super(sourceState);
            this.position = position;
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(position);
        }

        public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {
            @Override
            public SavedState createFromParcel(Parcel source) {
                return new SavedState(source);
            }

            @Override
            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }

    /**
     * 设置banner的条目点击事件
     *
     * @param listener 监听事件
     */
    public void setOnItemClickListener(OnItemClickListener listener) {
        mAdapter.setOnItemClickListener(listener);
    }

    public interface OnItemClickListener {

        void onItemClick(View view, int position);
    }
}
