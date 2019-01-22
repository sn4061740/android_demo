package android.com.network.net;

import android.com.network.HttpUtils;
import android.com.network.R;
import android.com.network.okgo.OkgoHttpServer;
import android.com.network.okgo.call.AbsCall;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;

public class NetActivity extends AppCompatActivity {

    View.OnClickListener onClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
        if(v.getId()==R.id.btn_get_okgo){
            touchGetOkgo();
        }else if(v.getId()==R.id.btn_post_okgo){
            touchPostOkgo();
        }else if(v.getId()==R.id.btn_file_okgo){
            touchFileOkgo();
        }
        }
    };


    public static void toActivity(Context context){
        Intent intent=new Intent(context,NetActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_net);
        //初始化网络服务
        HttpUtils.getInstance().init(new OkgoHttpServer());

        findViewById(R.id.btn_get_okgo).setOnClickListener(onClickListener);
        findViewById(R.id.btn_post_okgo).setOnClickListener(onClickListener);
        findViewById(R.id.btn_file_okgo).setOnClickListener(onClickListener);
    }
    //GET...
    private void touchGetOkgo(){
        final TextView infoTxt=findViewById(R.id.infoTxt);
        infoTxt.setText("数据加载中...");
        //获取 String 类型
        AbsCall absCall=new AbsCall<String>() {
            @Override
            public Object getTag() {
                return NetActivity.this;
            }
            @Override
            public void onError(String model, String errMsg, int code) {
                Log.e("TAG","CODE="+code+"\nURL="+model+"\n"+errMsg+"\n");
            }
            @Override
            public void onNext(String s) {
                if(isFinishing()){//
                    Log.e("TAG","当前界面已销毁...");
                }
                Log.e("TAG",s.toString());
                infoTxt.setText(Html.fromHtml(s));
            }
            @Override
            public void downloadProgress(long speed) {
                super.downloadProgress(speed);
                infoTxt.setText("数据加载速度:"+speed);
            }
        };
        //获取对应 javabean 类型
        AbsCall absCall1=new AbsCall<CdnBean>() {
            @Override
            public void onError(String model, String errMsg, int code) {
                Log.e("TAG","CODE="+code+"\nURL="+model+"\n"+errMsg+"\n");
            }
            @Override
            public void onNext(CdnBean o) {
                Log.e("TAG",o.toString());
            }
        };
        HttpUtils.getInstance().get("http://qzone-music.qq.com/fcg-bin/fcg_music_fav_getinfo.fcg?dirinfo=0&dirid=1&uin=993094914&p=0.519638272547262&g_tk=1284234856", absCall,null,null);
    }
    //POST...
    private void touchPostOkgo(){
        final TextView infoTxt=findViewById(R.id.infoTxt);
        infoTxt.setText("数据加载中...");
        //获取 String 类型
        AbsCall absCall=new AbsCall<String>() {
            @Override
            public Object getTag() {
                return NetActivity.this;
            }
            @Override
            public void onError(String model, String errMsg, int code) {
                Log.e("TAG","CODE="+code+"\nURL="+model+"\n"+errMsg+"\n");
            }
            @Override
            public void onNext(String s) {
                if(isFinishing()){//
                    Log.e("TAG","当前界面已销毁...");
                }
                Log.e("TAG",s.toString());
                infoTxt.setText(Html.fromHtml(s));
            }
            @Override
            public void downloadProgress(long speed) {
                super.downloadProgress(speed);
                infoTxt.setText("数据加载速度:"+speed);
            }
        };
        Map<String,String> params=new HashMap<>();
        params.put("from","en");
        params.put("to","zh");
        params.put("query","Test Engineer");
        HttpUtils.getInstance().post("https://httpbin.org/post",absCall,null,params);
    }
    //FIle...
    private void touchFileOkgo(){
        final TextView infoTxt=findViewById(R.id.infoTxt);
        infoTxt.setText("数据加载中...");
        AbsCall<File> absCall=new AbsCall<File>() {
            @Override
            public void onError(String model, String errMsg, int code) {
                Log.e("TAG","CODE="+code+"\nURL="+model+"\n"+errMsg+"\n");
            }
            @Override
            public void onNext(File result) {
                if(isFinishing()){//
                    Log.e("TAG","当前界面已销毁...");
                }
                if(result!=null) {
                    Log.e("TAG", result.getName());
                    infoTxt.setText(result.getName());
                }
            }
            @Override
            public Object getTag() {
                return NetActivity.this;
            }
            @Override
            public void downloadProgress(long speed) {
                super.downloadProgress(speed);
                infoTxt.setText("下载速度"+speed);
            }
        };
        HttpUtils.getInstance().getFile("http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4",absCall,"","",null,null);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        HttpUtils.getInstance().onCancel(NetActivity.this);
        Log.e("TAG","NetActivity 销毁...");
    }
}
