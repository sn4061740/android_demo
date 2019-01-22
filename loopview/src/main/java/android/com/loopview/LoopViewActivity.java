package android.com.loopview;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.kevin.loopview.BannerView;
import com.kevin.loopview.internal.BaseLoopAdapter;
import com.kevin.loopview.internal.ImageLoader;
import com.kevin.loopview.internal.LoopData;

import java.util.ArrayList;
import java.util.List;

public class LoopViewActivity extends AppCompatActivity implements BaseLoopAdapter.OnItemClickListener{

    BannerView mBannerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loop_view);

        initViews();
        initEvents();
    }

    private void initViews() {
        mBannerView = findViewById(R.id.bannerView);
        mBannerView.setImageLoader(new ImageLoader() {
            @Override
            public void loadImage(ImageView imageView, String url, int placeholder) {
                //Glide.with(imageView.getContext()).load(url).into(imageView);
                //GlideUtils.getInstance().load(url,imageView);
            }
        });
        initBannerView();
//        String json = LocalFileUtils.getStringFormAsset(this, "loopview_date.json");
//        LoopData loopData = new Gson().fromJson(json, LoopData.class);
//        mBannerView.setData(loopData);
//        mBannerView.startAutoLoop();
    }

    /**
     * 初始化BannerView
     *
     * @return void
     * @date 2015-10-9 21:32:12
     */
    private void initBannerView() {
        // 设置自定义布局
//        mLoopView.setLoopLayout(R.layout.ad_loopview_layout);
        // 设置数据
        String json = LocalFileUtils.getStringFormAsset(this, "loopview_date.json");
        LoopData loopData = new Gson().fromJson(json, LoopData.class);

        if (null != loopData) {
//            mBannerView.setData(loopData);

            List<String> images = new ArrayList<>(); // 图片集合
            List<String> descs = new ArrayList<>();
            List<String> links = new ArrayList<>();
            for (LoopData.ItemData item : loopData.items) {
                images.add(item.img);
                descs.add(item.desc);
                links.add(item.link);
            }
//            mBannerView.setData(images);
//            mBannerView.setData(images, links);
            mBannerView.setData(images, descs, links);
        }
        // 设置页面切换过度事件
        mBannerView.setScrollDuration(2000);
        // 设置页面切换时间间隔
        mBannerView.setInterval(3000);

        ViewPager viewPager = mBannerView.getViewPager();
        viewPager.setPageTransformer(true, new ZoomOutPageTransformer());

        mBannerView.startAutoLoop();
    }

    /**
     * 初始化事件
     *
     * @return void
     * @date 2015-10-20 14:05:47
     */
    private void initEvents() {
        mBannerView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(View view, LoopData.ItemData itemData, int position) {
        LoopData loopData = mBannerView.getData();
        String url = loopData.items.get(position).link;

//        Intent intent = new Intent();
//        intent.setData(Uri.parse(url));
//        intent.setAction(Intent.ACTION_VIEW);
//        startActivity(intent);
    }

}
