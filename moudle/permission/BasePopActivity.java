package com.xcore.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.umeng.analytics.MobclickAgent;

public abstract class BasePopActivity extends Activity {

    protected abstract int getLayoutId();

    protected abstract void initViews(Bundle savedInstanceState);

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            if (getLayoutId() != 0) {
                setContentView(getLayoutId());
            }
            initViews(savedInstanceState);
        }catch (Exception ex){}
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            MobclickAgent.onResume(this);
        }catch (Exception ex){}
    }
    @Override
    public void onPause() {
        super.onPause();
        try {
            MobclickAgent.onPause(this);
        }catch (Exception ex){}
    }
}
