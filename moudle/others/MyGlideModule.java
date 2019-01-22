package com.xcore.utils;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool;
import com.bumptech.glide.load.engine.cache.ExternalCacheDiskCacheFactory;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.load.engine.cache.MemorySizeCalculator;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.module.GlideModule;
import com.xcore.MainApplicationContext;
import com.xcore.NetInterceptor;

import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;

public class MyGlideModule implements GlideModule {
    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        // 防止图片变绿，在有ALPHA通道的情况下
        builder.setDecodeFormat(DecodeFormat.PREFER_RGB_565);

        //设置磁盘缓存大小
        int size = 1000 * 1024 * 1024;

//        MemorySizeCalculator calculator = new MemorySizeCalculator(context);
//        int defaultMemoryCacheSize = calculator.getMemoryCacheSize();
//        int defaultBitmapPoolSize = calculator.getBitmapPoolSize();
////         设置 Glide的 RAM缓存大小为 APP的20%。
//        int customMemoryCacheSize = (int) (1.2 * defaultMemoryCacheSize);
//        int customBitmapPoolSize = (int) (1.2 * defaultBitmapPoolSize);
//        builder.setMemoryCache(new LruResourceCache(customMemoryCacheSize));
//        builder.setBitmapPool(new LruBitmapPool(customBitmapPoolSize));

        //设置磁盘缓存
        builder.setDiskCache(new ExternalCacheDiskCacheFactory(context,"img_cache", size));
    }

    //设置长时间读取和断线重连
    OkHttpClient client=new OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(15, TimeUnit.SECONDS)
        .writeTimeout(15, TimeUnit.SECONDS)
        .retryOnConnectionFailure(true)
        .addInterceptor(new NetInterceptor())
        .build();
    @Override
    public void registerComponents(Context context, Glide glide) {
        glide.register(GlideUrl.class, InputStream.class, new OkHttpUrlLoader.Factory(client));
    }
}
