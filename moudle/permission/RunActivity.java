package com.xcore;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.xcore.base.BasePopActivity;
import com.xcore.utils.PermissionsChecker;

public class RunActivity extends BasePopActivity {

    final private static int PERMISSIONS_CODE = 29; // 请求码

    static final String[] PERMISSIONS = new String[]{
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
                Manifest.permission.MODIFY_AUDIO_SETTINGS,
//                Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS,
            Manifest.permission.CAMERA,
            Manifest.permission.WAKE_LOCK
//            Manifest.permission.STATUS_BAR
//            Manifest.permission.REQUEST_INSTALL_PACKAGES
//            Manifest.permission.WRITE_SETTINGS,
//                Manifest.permission.INTERNET
    };

    private PermissionsChecker permissionsChecker;

    @Override
    protected int getLayoutId() {
        return 0;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            Intent intent = getIntent();
//            Log.e(TAG, "scheme:" + intent.getScheme());
            Uri uri = intent.getData();
//            Log.e(TAG, "scheme: " + uri.getScheme());
//            Log.e(TAG, "host: " + uri.getHost());
//            Log.e(TAG, "port: " + uri.getPort());
//            Log.e(TAG, "path: " + uri.getPath());
//            Log.e(TAG, "queryString: " + uri.getQuery());
//            Log.e(TAG, "queryParameter: " + uri.getQueryParameter("shortid"));
            MainApplicationContext.SHORT_ID=uri.getQueryParameter("shortid");
        }catch (Exception ex){}
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {

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
        finish();
    }

    private void showMainActivity() {
        toMain();
    }
    private void toMain(){
        Intent intent = new Intent(this, SplashActivity.class);
        startActivity(intent);
    }

}