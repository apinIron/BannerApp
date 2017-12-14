package com.github.iron.bannerapp;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.iron.view.banner.Banner;
import com.iron.view.banner.BannerAdapter;
import com.iron.view.banner.SimpleBannerAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<BannerModel> mData;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;

        mData = new ArrayList<>();
        mData.add(new BannerModel("第一幅图片第一幅图片第一幅图片第一幅图片", "https://timgsa.baidu" + "" + "" + "" + "" + "" +
                ".com/timg?image&quality=80&size=b9999_10000&sec=1513178836538&di" +
                "=c6a0f47fb4ff635af24797f073697097&imgtype=0&src=http%3A%2F%2Ff.hiphotos.baidu" + "" + "" +
                ".com%2Fimage%2Fpic%2Fitem%2Fbf096b63f6246b60914025b4e2f81a4c500fa2a0.jpg"));
        mData.add(new BannerModel("第二幅图片第二幅图片第二幅图片", "https://timgsa.baidu" + "" + "" + "" + "" + "" + "" +
                "" + ".com/timg?image&quality=80&size=b9999_10000&sec=1513178836538&di" +
                "=cdd091305645381938e44c7a165237a6&imgtype=0&src=http%3A%2F%2Fb.hiphotos.baidu" + "" + "" +
                ".com%2Fimage%2Fpic%2Fitem%2Fe4dde71190ef76c606ac2feb9416fdfaae5167f0.jpg"));
        mData.add(new BannerModel("第三幅图片第三幅图片第三幅图片", "https://timgsa.baidu" + "" + "" + "" + "" + "" + "" +
                "" + ".com/timg?image&quality=80&size=b9999_10000&sec=1513178836537&di" +
                "=ee63e582296dcf3a22a4eda7c0796260&imgtype=0&src=http%3A%2F%2Fa.hiphotos.baidu" + "" + "" +
                ".com%2Fimage%2Fpic%2Fitem%2F1ad5ad6eddc451dad158462bbffd5266d1163247.jpg"));
        mData.add(new BannerModel("第四幅图片第四幅图片第四幅图片第四幅图片", "https://timgsa.baidu" + "" + "" + "" + "" + "" +
                "" + "" + ".com/timg?image&quality=80&size=b9999_10000&sec=1513178836536&di" +
                "=a113d003a2abadb539ad71745a794051&imgtype=0&src=http%3A%2F%2Fa.hiphotos.baidu" + "" + "" +
                ".com%2Fimage%2Fpic%2Fitem%2Fb812c8fcc3cec3fdcc9b00b7df88d43f8694274e.jpg"));

        final Banner baner = findViewById(R.id.banner);

        final BannerAdapter adapter = new BannerAdapter<BannerModel>(mData) {

            @Override
            protected View getItemView(int position, BannerModel bannerModel) {
                View bannerView = LayoutInflater.from(mContext).inflate(R.layout.item_banner, null, false);
                ImageView imgBannerImage = bannerView.findViewById(R.id.img_banner);
                TextView tvBannerTitle = bannerView.findViewById(R.id.tv_banner);
                tvBannerTitle.setText(bannerModel.getTips());
                Picasso.with(mContext).load(bannerModel.getImageUrl()).fit().centerCrop().into
                        (imgBannerImage);
                return bannerView;
            }
        };

        baner.setAdapter(adapter).startAutoPlay();
        findViewById(R.id.btn_last).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                baner.lastView();
            }
        });
        findViewById(R.id.btn_next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                baner.nextView();
            }
        });

        Banner imageBanner = findViewById(R.id.image_banner);

        SimpleBannerAdapter iAdapter = new SimpleBannerAdapter<BannerModel>(mContext, mData) {

            @Override
            protected void bindImage(ImageView imgView, int position, BannerModel bannerModel) {
                Picasso.with(mContext).load(bannerModel.getImageUrl()).fit().centerCrop().into(imgView);
            }

            @Override
            protected void bindTips(TextView textView, int position, BannerModel bannerModel) {
                textView.setText(bannerModel.getTips());
            }
        };

        imageBanner.setAdapter(iAdapter).startAutoPlay();
        imageBanner.setOnItemClickListener(new Banner.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(mContext, mData.get(position).getTips(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
