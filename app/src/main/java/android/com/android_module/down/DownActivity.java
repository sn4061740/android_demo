package android.com.android_module.down;

import android.com.android_module.R;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.jay.down.HttpServerManager;
import com.jay.down.config.DownConfig;
import com.jay.down.listener.IDownListener;

import java.io.File;

import demo.com.downmanager.DownManager;
import demo.com.downmanager.IDownListenner;
import demo.com.downmanager.MD5Utils;

public class DownActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_down);

        HttpServerManager.getInstance().init(getApplicationContext().getExternalCacheDir().getAbsolutePath());

        findViewById(R.id.btn_down).setOnClickListener(onClickListener);
    }

    View.OnClickListener onClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btn_down:
                    onDown();
                break;
            }
        }
    };

    private void onDown(){
        String root=getApplicationContext().getExternalCacheDir().getAbsolutePath();
        DownConfig config=new DownConfig();
        config.setDownUrl("http://m01.xxxxapixxx.com/m3u8/xsp1545998826-480.m3u8?code=dfasdfsdafsdffa");
        config.setV1("b24w9AwfAspg3QfpWUGuTPZCi8E48v8P");
        config.setRoot(root);
        config.setThreadNum(3);
        HttpServerManager.getInstance().startDown(config);
        HttpServerManager.getInstance().addListener(config.getId(), downListener1);

//        String url="http://m01.xxxxapixxx.com/m3u8/VENU840-hd.m3u8";
//        String taskId=MD5Utils.MD5Encode(url);
//
//        DownManager.getInstance().start("http://m01.xxxxapixxx.com/m3u8/VENU840-hd.m3u8");
//        DownManager.getInstance().addTaskListenner(taskId, new IDownListenner() {
//            @Override
//            public void onStart() {
//                Log.e("TAG","开始下载");
//            }
//
//            @Override
//            public void onProgress(int progress) {
//                Log.e("TAG","进度;"+progress);
//            }
//
//            @Override
//            public void onCurrentSize(long curSize) {
//                Log.e("TAG","当前大小："+curSize);
//            }
//
//            @Override
//            public void onSpeed(long speed) {
//                Log.e("TAG","速度："+speed);
//            }
//
//            @Override
//            public void onSuccess() {
//                Log.e("TAG","下载成功");
//            }
//
//            @Override
//            public void onError(Throwable errorMsg) {
//                Log.e("TAG","下载出错:"+errorMsg.getMessage());
//            }
//        });
    }

    IDownListener downListener1=new IDownListener() {
        @Override
        public void onStart() {
            System.out.println("MAIN  M3u8... 获取资源：：");
        }

        @Override
        public void onProgress(int progress) {
            System.out.println("MAIN   M3u8... 进度：：" + progress);
        }

        @Override
        public void onDownSize(long curSize) {
            System.out.println("MAIN 当前下载大小："+curSize);
        }

        @Override
        public void onSpeed(long speed) {
            System.out.println("MAIN  M3u8... 速度：：" + speed);
        }

        @Override
        public void onDownloading(long fileSize, int curLen, int totalLen) {
            System.out.println("MAIN  正在下载第："+curLen +"个文件,文件大小:"+fileSize+"总文件:"+totalLen);
        }

        @Override
        public void onSuccess(File file) {
            System.out.println("MAIN  M3u8... 成功：：" + file.getName());
        }

        @Override
        public void onDownError(int code, Throwable e) {
            System.out.println("MAIN 下载出错:"+code+"--"+e.getMessage());
        }
    };

}
