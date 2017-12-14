package com.iron.view.banner;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * @author iron
 *         created at 2017/12/13
 */
public abstract class SimpleBannerAdapter<T> extends BannerAdapter<T> {

    private Context mContext;

    public SimpleBannerAdapter(Context context, List<T> models) {
        super(models);
        mContext = context;
    }

    @Override
    protected View getItemView(int position, T t) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.simple_banner, null, false);
        TextView textView = view.findViewById(R.id.banner_text);
        mBanner.setTextStyle(textView);
        bindTips(textView, position, t);

        ImageView imageView = view.findViewById(R.id.banner_image);
        bindImage(imageView, position, t);

        return view;
    }

    protected abstract void bindImage(ImageView imageView, int position, T t);

    protected abstract void bindTips(TextView textView, int position, T t);
}
