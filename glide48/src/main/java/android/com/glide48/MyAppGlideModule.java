package android.com.glide48;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool;
import com.bumptech.glide.load.engine.cache.ExternalPreferredCacheDiskCacheFactory;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.load.engine.executor.GlideExecutor;
import com.bumptech.glide.module.AppGlideModule;
import com.bumptech.glide.request.RequestOptions;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

import static com.bumptech.glide.load.engine.executor.GlideExecutor.newDiskCacheExecutor;
import static com.bumptech.glide.load.engine.executor.GlideExecutor.newSourceExecutor;

@GlideModule
public final class MyAppGlideModule extends AppGlideModule {

    OkHttpClient client=new OkHttpClient.Builder()
            .connectTimeout(2000, TimeUnit.SECONDS)
            .readTimeout(2000, TimeUnit.SECONDS)
            .writeTimeout(2000, TimeUnit.SECONDS)
            .build();

    @Override
    public void applyOptions(@NonNull Context context, @NonNull GlideBuilder builder) {
        super.applyOptions(context, builder);
        //也可以直接覆写缓存大小
        int memoryCacheSizeBytes = 1024 * 1024 * 20; // 20mb
        builder.setMemoryCache(new LruResourceCache(memoryCacheSizeBytes));

        //可以直接复写这个池的大小
        int bitmapPoolSizeBytes = 1024 * 1024 * 30; // 30mb
        builder.setBitmapPool(new LruBitmapPool(bitmapPoolSizeBytes));

        //设置磁盘缓存
        long diskCacheSizeBytes=1024*1024*500;//500M
        ExternalPreferredCacheDiskCacheFactory diskCacheFactory=new ExternalPreferredCacheDiskCacheFactory(context,"image_cache",diskCacheSizeBytes);
        builder.setDiskCache(diskCacheFactory);

        //异常捕获
        final GlideExecutor.UncaughtThrowableStrategy myUncaughtThrowableStrategy = new GlideExecutor.UncaughtThrowableStrategy() {
            @Override
            public void handle(Throwable t) {
                IGlideListener glideErrorListener= GlideUtils.getInstance().getGlideErrorListener();
                if(glideErrorListener!=null){
                    glideErrorListener.onError("UncaughtThrowableStrategy 异常",new Exception(t),0);
                }
                Log.e("TAG","异常...");
            }
        };
        builder.setDiskCacheExecutor(newDiskCacheExecutor(myUncaughtThrowableStrategy));
        builder.setResizeExecutor(newSourceExecutor(myUncaughtThrowableStrategy));

        //设置日志级别
        builder.setLogLevel(Log.DEBUG);

        builder.setDefaultRequestOptions(
                new RequestOptions()
                        .format(DecodeFormat.PREFER_RGB_565).disallowHardwareConfig());
//                        .disallowHardwareBitmaps());
    }
    @Override
    public void registerComponents(@NonNull Context context, @NonNull Glide glide, @NonNull Registry registry) {
        super.registerComponents(context, glide, registry);
//        OkHttpUrlLoader.Factory factory = new OkHttpUrlLoader.Factory(client);
//        registry.replace(GlideUrl.class, InputStream.class,factory);
//        registry.replace(GlideUrl.class, InputStream.class, new OkHttpUrlLoader.Factory());
    }
    @Override
    public boolean isManifestParsingEnabled() {
        return false;//super.isManifestParsingEnabled();
    }
}
