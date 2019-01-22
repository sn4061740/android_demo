package android.com.android_module.glide;

import android.com.android_module.R;
import android.com.glide48.GlideUtils;
import android.com.glide48.IGlideListener;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

public class GlideActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_glide);
        //引不同的库,监听方式和调用方式一样,导入对应的包即可,未使用缓存
        GlideUtils.getInstance().init(new IGlideListener() {
            @Override
            public void onError(String model, Exception e, long stime) {
                //图片加载出错 model: URL   e:异常信息  stime:加载总时长 ms
            }
            @Override
            public void onSuccess() {
                //图片加载成功
            }
        });

        ImageView imageView= findViewById(R.id.loadImage);
        GlideUtils.getInstance().load("http://img.hb.aicdn.com/7dc2da90e5567e7675cd6cb803285b0c70ac245c3253c-jLdCPS_fw658",imageView);

    }
}
