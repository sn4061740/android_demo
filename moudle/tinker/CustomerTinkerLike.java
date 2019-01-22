package com.xcore.tinker;

import android.annotation.TargetApi;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.multidex.MultiDex;
import android.util.Log;

import com.tencent.tinker.anno.DefaultLifeCycle;
import com.tencent.tinker.loader.app.DefaultApplicationLike;
import com.tencent.tinker.loader.shareutil.ShareConstants;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;
import com.xcore.MainApplicationContext;
import com.xcore.utils.CrashHandler;


@SuppressWarnings("unused")
@DefaultLifeCycle(
        application = "com.xcore.MainApplication",
        flags = ShareConstants.TINKER_ENABLE_ALL,
        loadVerifyFlag = false
)
public class CustomerTinkerLike extends DefaultApplicationLike {

    public CustomerTinkerLike(Application application, int tinkerFlags, boolean tinkerLoadVerifyFlag,
                                 long applicationStartElapsedTime, long applicationStartMillisTime, Intent tinkerResultIntent) {
        super(application, tinkerFlags, tinkerLoadVerifyFlag, applicationStartElapsedTime, applicationStartMillisTime, tinkerResultIntent);
    }

//    @Override
//    public void onCreate() {
//        super.onCreate();
//        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
//        StrictMode.setVmPolicy(builder.build());
//        builder.detectFileUriExposure();
//    }

    /**
     * install multiDex before install tinker
     * so we don't need to put the tinker lib classes in the main dex
     *
     * @param base
     */
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    public void onBaseContextAttached(Context base) {
        super.onBaseContextAttached(base);

        Log.e("TAG","初始化。。。");

        //you must install multiDex whatever tinker is installed!
        MultiDex.install(base);

        MainApplicationContext.application= getApplication();
        MainApplicationContext.context=base;
        MainApplicationContext.onCreate();

        TinkerManager.installTinker(this);

        //初始化异常信息
        CrashHandler.getInstance().init(base);

        //////////////////////////////////////////////////////////////
        ////////////////////////// 友盟统计 ///////////////////////////
        ////////////////////////////////////////////////////////////
        /**
         * 初始化common库
         * 参数1:上下文，不能为空
         * 参数2:设备类型，UMConfigure.DEVICE_TYPE_PHONE为手机、UMConfigure.DEVICE_TYPE_BOX为盒子，默认为手机
         * 参数3:Push推送业务的secret
         */
        UMConfigure.init(getApplication().getApplicationContext(), "5ba468bfb465f54b5d00016b", "Umeng",UMConfigure.DEVICE_TYPE_PHONE, null);

        UMConfigure.setLogEnabled(true);
        UMConfigure.setEncryptEnabled(true);

        MobclickAgent.setScenarioType(getApplication().getApplicationContext(), MobclickAgent.EScenarioType.E_UM_NORMAL);
        MobclickAgent.setSessionContinueMillis(3000);
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public void registerActivityLifecycleCallbacks(Application.ActivityLifecycleCallbacks callback) {
        getApplication().registerActivityLifecycleCallbacks(callback);
        Log.e("TAG","这个是怎么回调的。。。");
    }


}
