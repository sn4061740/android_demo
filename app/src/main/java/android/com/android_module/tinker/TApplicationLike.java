//package android.com.android_module.tinker;
//
//import android.app.Application;
//import android.content.Context;
//import android.content.Intent;
//import android.support.multidex.MultiDex;
//
////import com.tencent.tinker.anno.DefaultLifeCycle;
////import com.tencent.tinker.entry.DefaultApplicationLike;
////import com.tencent.tinker.loader.shareutil.ShareConstants;
//
//@DefaultLifeCycle(
//        application = ".TApplication",
//        flags = ShareConstants.TINKER_ENABLE_ALL,
//        loadVerifyFlag = false)
//public class TApplicationLike extends DefaultApplicationLike {
//    public TApplicationLike(Application application, int tinkerFlags, boolean tinkerLoadVerifyFlag, long applicationStartElapsedTime, long applicationStartMillisTime, Intent tinkerResultIntent) {
//        super(application, tinkerFlags, tinkerLoadVerifyFlag, applicationStartElapsedTime, applicationStartMillisTime, tinkerResultIntent);
//    }
//
//    @Override
//    public void onBaseContextAttached(Context base) {
//        super.onBaseContextAttached(base);
//
//        MultiDex.install(base);
//        TinkerManager.installTinker(this);
//    }
//}
