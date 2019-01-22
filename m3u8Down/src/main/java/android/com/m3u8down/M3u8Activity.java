package android.com.m3u8down;

import android.com.m3u8down.m3u8.OnDownloadListener;
import android.com.m3u8down.m3u8.library.M3U8DownloadTask;
import android.com.m3u8down.utils.NetSpeedUtils;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;


//使用的第三方库 M3U8Manager
public class M3u8Activity extends AppCompatActivity {
    long lastLength=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_m3u8);

        M3u8Utils.getInstance().startServer();

        findViewById(R.id.btn_down).setOnClickListener(onClickListener);
        findViewById(R.id.btn_play).setOnClickListener(onClickListener);
    }

    View.OnClickListener onClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(v.getId()==R.id.btn_down){
                onDown();
            }else if(v.getId()==R.id.btn_play){
                onPlay();
            }
        }
    };

    //下载
    private void onDown(){
        //http://m01.xxxlutubexxx.com/m3u8/xsp1545724456-hd.m3u8?priv=1&v1=b24w9AwfAspg3QfpWUGuTPZCi8E48v8P
        final M3U8DownloadTask task1 = new M3U8DownloadTask("1005");
        //task1.setSaveFilePath(M3u8Activity.this.getExternalCacheDir()+"/m3u8/xsp1545724456-hd.m3u8");
        task1.setThreadCount(3);
        task1.download("http://m01.xxxlutubexxx.com/m3u82/xsp1545724908-480.m3u8", new OnDownloadListener() {
            @Override
            public void onDownloading(long itemFileSize, int totalTs, int curTs) {
                Log.e("TAG","itemFileSize="+itemFileSize+":::: totalTs="+totalTs+":::: curTs="+curTs);
            }
            @Override
            public void onSuccess() {
                Log.e("TAG","成功");
            }
            @Override
            public void onProgress(long curLength) {
                if (curLength - lastLength >= 0) {
                    final String speed = NetSpeedUtils.getInstance().displayFileSize(curLength - lastLength) + "/s";
                    Log.e("TAG",task1.getTaskId() +"当前速度:" + speed);
                    lastLength = curLength;

                }
            }
            @Override
            public void onStart() {
                Log.e("TAG","开始");
            }
            @Override
            public void onError(Throwable errorMsg) {

            }
        });
    }


    //播放
    private void onPlay(){
//        player.setVideoPath("http://127.0.0.1:8080/play?vPath=17891-1005.m3u8&static=1");
        //http://jzvd.nathen.cn/c6e3dc12a1154626b3476d9bf3bd7266/6b56c5f0dc31428083757a45764763b0-5287d2089db37e62345123a1be272f8b.mp4
//        player.setUp("http://jzvd.nathen.cn/c6e3dc12a1154626b3476d9bf3bd7266/6b56c5f0dc31428083757a45764763b0-5287d2089db37e62345123a1be272f8b.mp4"
//                , "饺子闭眼睛" , Jzvd.SCREEN_WINDOW_NORMAL);
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    HttpURLConnection conn = (HttpURLConnection) new URL("http://127.0.0.1:8080/play?vPath=17891-1005.m3u8&static=1").openConnection();
//                    int code=conn.getResponseCode();
//                    Log.e("TAG","code:"+code);
//                    if(code==200){
//                        InputStream in=conn.getInputStream();
//                        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
//                        String v=reader.readLine();
//                        Log.e("TAG","返回信息:"+v);
//                    }else{
//
//                    }
//                }catch (Exception ex){
//                    ex.printStackTrace();
//                }
//            }
//        }).start();
    }
}
