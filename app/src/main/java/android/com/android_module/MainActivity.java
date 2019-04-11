package android.com.android_module;

import android.Manifest;
import android.com.android_module.down.DownActivity;
import android.com.android_module.glide.GlideActivity;
import android.com.android_module.tinker.TinkerActivity;
import android.com.baselibrary.BaseLibraryActivity;
import android.com.commonadapter.test.MultiTypeActivity;
import android.com.lockpattern.widget.LockPatternActivity;
import android.com.loopview.LoopViewActivity;
import android.com.network.net.NetActivity;
import android.com.permission.IPermissionListener;
import android.com.permission.RunActivity;
import android.com.sliderecyclerview.SlideRecyclerViewActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import xcore.com.gsyvideoplayer.PlayerVideoActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_net).setOnClickListener(onClickListener);
        findViewById(R.id.btn_glide).setOnClickListener(onClickListener);
        findViewById(R.id.btn_logkpattern).setOnClickListener(onClickListener);
        findViewById(R.id.btn_permission).setOnClickListener(onClickListener);
        findViewById(R.id.btn_base).setOnClickListener(onClickListener);
        findViewById(R.id.btn_banner).setOnClickListener(onClickListener);
        findViewById(R.id.btn_delegation).setOnClickListener(onClickListener);
        findViewById(R.id.btn_m3u8).setOnClickListener(onClickListener);
        findViewById(R.id.btn_sliderRecyView).setOnClickListener(onClickListener);
        findViewById(R.id.btn_tinker).setOnClickListener(onClickListener);
        findViewById(R.id.btn_gsyplayer).setOnClickListener(onClickListener);
        findViewById(R.id.btn_down).setOnClickListener(onClickListener);

    }
    View.OnClickListener onClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent=null;
            switch (v.getId()){
                case R.id.btn_net:
                    intent=new Intent(MainActivity.this,NetActivity.class);
                    break;
                case R.id.btn_glide:
                    intent=new Intent(MainActivity.this,GlideActivity.class);
                    break;
                case R.id.btn_logkpattern:
                    LockPatternActivity.toActivity(MainActivity.this,null,
                            LockPatternActivity.LockType.NONE,true);
                    break;
                case R.id.btn_permission:
                    toPermission();
                    break;
                case R.id.btn_base:
                    intent=new Intent(MainActivity.this, BaseLibraryActivity.class);
                    break;
                case R.id.btn_banner:
                    intent=new Intent(MainActivity.this,LoopViewActivity.class);
                    break;
                case R.id.btn_delegation:
                    intent=new Intent(MainActivity.this,MultiTypeActivity.class);
                    break;
                case R.id.btn_m3u8:
                    //intent=new Intent(MainActivity.this,M3u8Activity.class);
                    break;
                case R.id.btn_sliderRecyView:
                    intent=new Intent(MainActivity.this,SlideRecyclerViewActivity.class);
                    break;
                case R.id.btn_tinker:
                    TinkerActivity.toActivity(MainActivity.this);
                    break;
                case R.id.btn_gsyplayer:
                    PlayerVideoActivity.toActivity(MainActivity.this);
                    break;
                case R.id.btn_down:
                    intent=new Intent(MainActivity.this,DownActivity.class);
                    break;
            }
            if(intent!=null){
                startActivity(intent);
            }
        }
    };
    //申请权限
    private void toPermission(){
        RunActivity.toActivity(MainActivity.this, new IPermissionListener() {
            @Override
            public void onSuccess() {
                Log.e("TAG","权限申请成功");
                NetActivity.toActivity(MainActivity.this);
            }
            @Override
            public void onError() {
                Log.e("TAG","权限申请失败");
            }
        },
        new String[]{
                //            Manifest.permission.INTERNET,
                //            Manifest.permission.RECORD_AUDIO,
                //            Manifest.permission.ACCESS_WIFI_STATE,
                //            Manifest.permission.ACCESS_NETWORK_STATE,
//                                        Manifest.permission.CHANGE_NETWORK_STATE,
                Manifest.permission.READ_PHONE_STATE,
                //            Manifest.permission.READ_CONTACTS,
                //            Manifest.permission.WRITE_SETTINGS,
                //            Manifest.permission.ACCESS_FINE_LOCATION
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.READ_PHONE_STATE,
                //                Manifest.permission.READ_LOGS,
                //                Manifest.permission.MODIFY_AUDIO_SETTINGS,
                //                Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS,
//                                                        Manifest.permission.CAMERA,
                //            Manifest.permission.WAKE_LOCK
                //            Manifest.permission.STATUS_BAR
                //            Manifest.permission.REQUEST_INSTALL_PACKAGES
                //            Manifest.permission.WRITE_SETTINGS,
                //                Manifest.permission.INTERNET
        });
    }

}
