//package android.com.android_module.tinker;
//
//import android.content.Context;
//import android.util.Log;
//
//import com.tencent.tinker.entry.ApplicationLike;
//import com.tencent.tinker.lib.tinker.Tinker;
//import com.tencent.tinker.lib.tinker.TinkerInstaller;
//
//import java.io.File;
//
//public class TinkerManager {
//    private static ApplicationLike mAppLike;
//    private static boolean isInstalled=false;
//
//    public static void installTinker(ApplicationLike applicationLike){
//        mAppLike=applicationLike;
//        if(isInstalled){
//            return;
//        }
//        TinkerInstaller.install(mAppLike);
//        isInstalled=true;
//    }
//
//    public static void loadPatch(String path) throws Exception {
//        Context context=getApplicationContext();
//        if(context==null){
//            return;
//        }
//        File file=new File(path);
//        if(!file.exists()){
//            throw new Exception("文件不存在");
//        }
//        try {
//            if (Tinker.isTinkerInstalled()) {
//                //Toast.makeText(context,"开始patch",Toast.LENGTH_LONG).show();
//                TinkerInstaller.onReceiveUpgradePatch(context, path);
//            } else {
//                //Toast.makeText(context,"没有初始化Tinker",Toast.LENGTH_LONG).show();
//                Log.e("TAG", "没有初始化Tinker");
//            }
//        }catch (Exception e){}
//    }
//    private static Context getApplicationContext(){
//        if(mAppLike==null){
//            return null;
//        }
//        return mAppLike.getApplication().getApplicationContext();
//    }
//}
