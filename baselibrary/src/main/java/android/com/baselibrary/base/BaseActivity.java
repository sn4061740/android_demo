package android.com.baselibrary.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

// 父 AppCompatActivity
public abstract class BaseActivity extends AppCompatActivity {
    protected String className=this.getClass().getName();

    //RES ID
    protected abstract int getLayoutId();
    //初始化View
    protected abstract void initView(Bundle savedInstanceState);

    //初始化数据
    public abstract void initData();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    /**
     * 打开一个Activity 默认 不关闭当前activity
     */
    public static void gotoActivity(Context context, Bundle ex) {
        Intent intent = new Intent(context, BaseActivity.class);
        if (ex != null) intent.putExtras(ex);
        context.startActivity(intent);
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
