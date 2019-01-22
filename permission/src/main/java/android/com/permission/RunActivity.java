package android.com.permission;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class RunActivity extends AppCompatActivity {

    final private static int PERMISSIONS_CODE = 29; // 请求码

    static String[] PERMISSIONS = new String[]{
//            Manifest.permission.INTERNET,
//            Manifest.permission.RECORD_AUDIO,
//            Manifest.permission.ACCESS_WIFI_STATE,
////            Manifest.permission.ACCESS_NETWORK_STATE,
//            Manifest.permission.CHANGE_NETWORK_STATE,
//            Manifest.permission.READ_PHONE_STATE,
//            Manifest.permission.READ_CONTACTS,
//            Manifest.permission.WRITE_EXTERNAL_STORAGE,
//            Manifest.permission.READ_EXTERNAL_STORAGE,
//            Manifest.permission.WRITE_SETTINGS,
//            Manifest.permission.ACCESS_FINE_LOCATION
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE,
//                Manifest.permission.READ_LOGS,
//                Manifest.permission.MODIFY_AUDIO_SETTINGS,
//                Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS,
            Manifest.permission.CAMERA,
//            Manifest.permission.WAKE_LOCK
//            Manifest.permission.STATUS_BAR
//            Manifest.permission.REQUEST_INSTALL_PACKAGES
//            Manifest.permission.WRITE_SETTINGS,
//                Manifest.permission.INTERNET
    };
    private static IPermissionListener permissionListener;
    private PermissionsChecker permissionsChecker;

    public static void toActivity(Context context,IPermissionListener iPermissionListener){
        permissionListener=iPermissionListener;

        Intent intent=new Intent(context,RunActivity.class);
        context.startActivity(intent);
    }
    public static void toActivity(Context context,IPermissionListener iPermissionListener,String...args){
        permissionListener=iPermissionListener;
        PERMISSIONS=args;

        Intent intent=new Intent(context,RunActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        permissionsChecker = new PermissionsChecker(this);
    }
    @Override
    public void onResume() {
        super.onResume();
        if (permissionsChecker.lacksPermissions(PERMISSIONS)) {
            startPermissionsActivity();
        } else {
            showMainActivity();
            finish();
        }
    }
    private void startPermissionsActivity() {
        PermissionsActivity.startActivityForResult(this, PERMISSIONS_CODE, PERMISSIONS);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 拒绝时, 关闭页面, 缺少主要权限, 无法运行
        if (requestCode == PERMISSIONS_CODE &&
                resultCode == PermissionsActivity.PERMISSIONS_DENIED) {
        } else {
            showMainActivity();
        }

    }

    private void showMainActivity() {
        Log.e("TAG","验证成功");
        if(permissionListener!=null){
            permissionListener.onSuccess();
        }
        finish();
    }

}