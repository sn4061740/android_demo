package com.xcore.base;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.umeng.analytics.MobclickAgent;
import com.xcore.MainApplicationContext;
import com.xcore.R;
import com.xcore.cache.CacheManager;
import com.xcore.cache.LocalDownLoader;
import com.xcore.utils.StatusBarUtil;
import com.xcore.utils.ToastUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public abstract class BaseActivity extends AppCompatActivity implements BaseView {
    protected Activity activity;
    protected ProgressDialog progress;
    private boolean hasBus = false;
    protected View noDataStubView;
    protected View sysErrStubView;

    protected String className=this.getClass().getName();

    public Toolbar toolbar;
    private Timer _autoTimer;

    //不处理自动下载的类
    List<String> start_stopList= Arrays.asList(
                                    "PVideoViewActivity8",
                                    "LoginActivity",
                                    "RegisterActivity",
                                    "SplashActivity",
                                    "VideoActivity",
                                    "PlayActivity",
                                    "CuestomerCaptureActivity"
                                    );

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.
                FLAG_KEEP_SCREEN_ON);   //应用运行时，保持屏幕高亮，不锁屏

        activity = this;
        MainApplicationContext.addActivity(activity);
        if (getLayoutId() != 0) {
            setContentView(getLayoutId());
        }
        toolbar = findViewById(R.id.toolbar1);
        if (toolbar != null) {
            toolbar.setFitsSystemWindows(true);
        }
        StatusBarUtil.darkMode(this);

        initBind();
        initViews(savedInstanceState);
        onBack();

        //先判断空闲自动缓存是否开启
        boolean idleBoo= MainApplicationContext.IS_IDLE_CACHE;
        if(idleBoo==true&&!start_stopList.contains(getClass().getSimpleName())) {
            //先把所有任务停止了
            final LocalDownLoader localDownLoader=CacheManager.getInstance().getLocalDownLoader();
            if(_autoTimer==null){
                _autoTimer = new Timer();
                _autoTimer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        localDownLoader.startAll();
                    }
                }, MainApplicationContext.IDLE_TIMER);
            }

//            final DownHandler downHandler= CacheManager.getInstance().getDownHandler();
//            downHandler.stopRunAll();// stopAll();
//            if (_autoTimer == null) {
//                _autoTimer = new Timer();
//                _autoTimer.schedule(new TimerTask() {
//                    @Override
//                    public void run() {
//                        downHandler.startRunAll();// startAll();
//                    }
//                }, MainApplicationContext.IDLE_TIMER);
//            }
        }
//        MobclickAgent.openActivityDurationTrack(false);
//        MobclickAgent.setScenarioType(this, MobclickAgent.EScenarioType.E_UM_NORMAL);
    }

    protected void initBind() {
//        ButterKnife.bind(activity);
    }

    protected abstract int getLayoutId();

    protected abstract void initViews(Bundle savedInstanceState);

    protected abstract void initData();

    /**
     * 打开一个Activity 默认 不关闭当前activity
     */
    public void gotoActivity(Class<?> clz) {
        gotoActivity(clz, false, null);
    }

    public void gotoActivity(Class<?> clz, boolean isCloseCurrentActivity) {
        gotoActivity(clz, isCloseCurrentActivity, null);
    }

    public void gotoActivity(Class<?> clz, boolean isCloseCurrentActivity, Bundle ex) {
        Intent intent = new Intent(this, clz);
        if (ex != null) intent.putExtras(ex);
        startActivity(intent);
        if (isCloseCurrentActivity) {
            finish();
        }
    }
    Toast toast;
    public void toast(String msg){
        if(toast!=null){
            toast.setText(msg);
        }else {
            toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
            toast.setText(msg);
        }
        toast.setGravity(Gravity.CENTER,0,0);
        toast.show();
    }
    public void toast(String msg,int color){
        TextView view=null;
        if(toast!=null){
            view=  (TextView) toast.getView().findViewById(android.R.id.message);
            toast.setText(msg);
        }else {
            toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
            toast.setText(msg);
            view= (TextView) toast.getView().findViewById(android.R.id.message);
        }
        toast.setGravity(Gravity.CENTER,0,0);
        if(view!=null) {
            view.setTextColor(this.getResources().getColor(color));
        }
        toast.show();
    }

    //返回 可重写
    public void onBack(){
        View view=findViewById(R.id.btn_back);
        if(view!=null) {
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //finish();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    finishAfterTransition();
                }else{
                    finish();
                }
                }
            });
        }
    }
    //设置标题
    public void setTitle(String title){
        TextView textView=findViewById(R.id.title_txt);
        if(textView!=null) {
            textView.setText(title);
        }
    }
    //设置右边编辑
    public void setEdit(String editStr, View.OnClickListener l){
        TextView editView=findViewById(R.id.edit_txt);
        if(editStr.equals("")){
            editView.setVisibility(View.GONE);
            return;
        }
        editView.setText(editStr);
        if(l!=null){
            editView.setOnClickListener(l);
        }
    }
    //设置右边编辑
    public void setEdit(String editStr){
        TextView editView=findViewById(R.id.edit_txt);
        if(editView==null){
            return;
        }
        editView.setText(editStr);
        if(editStr.equals("")){
            editView.setVisibility(View.GONE);
            return;
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
//        MainApplicationContext.onWindowFocusChanged(hasFocus,this);
    }

    @Override
    public void showProgress() {
        if (progress == null) {
            progress = new ProgressDialog(activity);
            progress.setMessage("加载中...");
        }
        progress.show();
    }

    @Override
    public void hideProgress() {
        if (progress != null && progress.isShowing()) {
            progress.hide();
        }
    }

    @Override
    public void toast(CharSequence s) {
        ToastUtils.showShortToast(s);
    }

    @Override
    public void toast(int id) {
        ToastUtils.showShortToast(id);
    }

    @Override
    public void toastLong(CharSequence s) {
        ToastUtils.showLongToast(s);
    }

    @Override
    public void toastLong(int id) {
        ToastUtils.showLongToast(id);
    }


    @Override
    public void showNullLayout() {

    }

    @Override
    public void hideNullLayout() {

    }

    @Override
    public void showErrorLayout(View.OnClickListener listener) {

    }

    @Override
    public void hideErrorLayout() {

    }

    @Override
    public void onResume() {
        super.onResume();
//        MobclickAgent.onPageStart(className);
        try {
            MobclickAgent.onResume(this);
        }catch (Exception ex){}
//        MobclickAgent.onPageStart(className);
    }
    @Override
    public void onPause() {
        super.onPause();
        try {
//        MobclickAgent.onPageEnd(className);
            MobclickAgent.onPause(this);
//        MobclickAgent.onPageEnd(className);
        }catch (Exception ex){}
    }

    @Override
    public void onDestroy() {
        if(_autoTimer!=null){
            _autoTimer.cancel();
        }
        _autoTimer=null;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    boolean idleBoo = MainApplicationContext.IS_IDLE_CACHE;
                    if (idleBoo == true && !start_stopList.contains(getClass().getSimpleName())) {
                        CacheManager.getInstance().getLocalDownLoader().stopRunAll();// .getDownHandler().stopRunAll();// stopAll();
                    }
                }catch (Exception ex){}
            }
        }).start();
        super.onDestroy();
    }
}
