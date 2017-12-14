# BannerApp

### 如何使用

Layout XML:
```
    <com.iron.view.banner.Banner
        android:id="@+id/banner"
        android:layout_width="match_parent"
        android:layout_height="200dp"/>
````

JavaCode:
```
        //自行创建对象，Adpater传递泛型
        List<BannerMode> mData = new ArrayList<>();
        ...

        Banner banner = findViewById(R.id.image_banner);

        SimpleBannerAdapter iAdapter = new SimpleBannerAdapter<BannerModel>(mContext, mData) {

            @Override
            protected void bindImage(ImageView imgView, int position, BannerModel bannerModel) {
                Picasso.with(mContext).load(bannerModel.getImageUrl()).fit().centerCrop().into(imgView);
            }

            @Override
            protected void bindTips(TextView textView, int position, BannerModel bannerModel) {
                textView.setText(bannerModel.getmTips());
            }
        };

        banner.setAdapter(iAdapter).startAutoPlay();
        banner.setOnItemClickListener(new Banner.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(mContext, mData.get(position).getTips(), Toast.LENGTH_LONG).show();
            }
        });

```
如果集合数据发生改变调用 banner.notifyDataSetChanged() 方法使banner更新数据。
<hr/>

Banner其他自带方法：</br>

//开启自动播放</br>
startAutoPlay();

//关闭自动播放</br>
stopAutoPlay();     

//切换到下一个界面
nextView(); 

//切换到上一个界面
lastView();

//设置自动播放间隔（默认为5秒）</br>
setPlayIntervalTime(long millisecond);  

//设置圆点标识的选中和未选中状态图片资源</br>
setDotResource(int dotSelectedRes, int dotNoSelectedRes);

//设置图片滚动速度（一个view离开屏幕的时间，默认0.8秒）</br>
setScrollSpeed(int millisecond);

Banner布局参数选择:
```
        <!-- 圆点指示器的边距设置 -->
        <attr name="dot_marginBottom" format="dimension"/>
        <attr name="dot_marginTop" format="dimension"/>
        <attr name="dot_marginLeft" format="dimension"/>
        <attr name="dot_marginRight" format="dimension"/>
        <!-- 圆点指示器间隔距离 -->
        <attr name="dot_interval" format="dimension"/>
        <!-- 圆点指示器大小设置 -->
        <attr name="dot_size" format="dimension"/>
        <!-- tips字体大小 -->
        <attr name="text_size" format="dimension"/>
        <!-- tips字体颜色 -->
        <attr name="text_color" format="color"/>
        <!-- 圆点指示器不同状态资源 -->
        <attr name="dot_selectedResource" format="reference"/>
        <attr name="dot_notSelectedResource" format="reference"/>
        <!-- 圆点指示器位置 默认为 BOTTOM|CENTER_HORIZONTAL 底部居中 -->
        <attr name="dot_gravity">
            <flag name="BOTTOM" value="80"/>
            <flag name="TOP" value="48"/>
            <flag name="LEFT" value="3"/>
            <flag name="RIGHT" value="5"/>
            <flag name="CENTER_HORIZONTAL" value="1"/>
        </attr>
```
如果对自带的banner样式不满意，也可以自定义样式,只要使用BannerAdapter：

```
        BannerAdapter adapter = new BannerAdapter<BannerModel>(mData) {

            @Override
            protected View getItemView(int position, BannerModel bannerModel) {
                View bannerView = LayoutInflater.from(mContext).inflate(R.layout.item_banner, null, false);
                ImageView imgBannerImage = bannerView.findViewById(R.id.img_banner);
                TextView tvBannerTitle = bannerView.findViewById(R.id.tv_banner);
                tvBannerTitle.setText(bannerModel.getmTips());
                Picasso.with(mContext).load(bannerModel.getImageUrl()).fit().centerCrop().into
                        (imgBannerImage);
                return bannerView;
            }
        };
```
