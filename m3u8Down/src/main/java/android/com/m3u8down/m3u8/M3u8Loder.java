package android.com.m3u8down.m3u8;

import android.com.m3u8down.M3u8DownTaskManager;
import android.com.m3u8down.m3u8.util.M3u8Conver;
import android.net.Uri;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.model.Response;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class M3u8Loder {

    private String baseUrl;
    String root;
    String name="";
    private boolean isRunning=false;

    private IDownListener idownListener;
    TsLoader tsLoader;

    public M3u8Loder(String url,String taskId,IDownListener iDownListener){
        this.baseUrl=url;
        this.idownListener=iDownListener;
        root=M3u8DownTaskManager.root;
        name=taskId;
    }
    ExecutorService service = Executors.newCachedThreadPool();//.newFixedThreadPool(3);
    public void loader(){
        isRunning=true;
        downListener.onStart();
        String rPath=root+"/"+name+".m3u8";
        final File file=new File(rPath);
        service.execute(new Runnable() {
            @Override
            public void run() {
            if(file.exists()){
                converFileByM3u8(file);
            }else{
                OkGo.<File>get(baseUrl).execute(new android.com.m3u8down.m3u8.FileCallback(root,name+".m3u8") {
                    @Override
                    public void onSuccess(Response<File> response) {
                        File file1=response.body();
                        if(file1!=null) {
                            //一定是下载成功了
                            converFileByM3u8(file1);
                        }else{
//                            if(service!=null) {
//                                service.shutdown();
//                            }
                            if(downListener!=null) {
                                downListener.onError(new Throwable("文件下载失败,文件为null"));
                            }
                        }
                    }
                    @Override
                    public void downloadProgress(Progress progress) {
                        super.downloadProgress(progress);
                        //Log.e("TAG","速度:"+progress.speed);
                    }
                    @Override
                    public void onError(Response<File> response) {
                        super.onError(response);
//                        if(service!=null) {
//                            service.shutdown();
//                        }
                        //Log.e("TAG","出错了");
                        if(downListener!=null) {
                            downListener.onError(response.getException());
                        }
                    }
                });
            }
            }
        });
    }
    //将文件转成 M3u8 对象
    private void converFileByM3u8(File file){
        Uri uri = Uri.parse(baseUrl);
        String appid = uri.getQueryParameter("v1");//md5 码验证
        try{
            M3U8 ret=M3u8Conver.getM3u8(file,name,appid);
            if(ret!=null) {
                loaderTs(ret);
            }else{
//                if(service!=null) {
//                    service.shutdown();
//                }
                if(downListener!=null) {
                    downListener.onError(new Exception("得到的m3u8为null"));
                }
            }
        }catch (Exception ex){
//            if(service!=null) {
//                service.shutdown();
//            }
            if(downListener!=null) {
                downListener.onError(ex);
            }
        }
    }

    public boolean isRunning() {
        return isRunning;
    }

    private void loaderTs(M3U8 m3U8){
        if(isRunning=false){
            return;
        }
        if(tsLoader!=null){
            tsLoader.stop();
        }
        downListener.onStartDown();
        if(tsLoader==null) {
            tsLoader = new TsLoader(m3U8, onDownloadListener);
        }
        tsLoader.loader();
    }
    IDownListener downListener=new IDownListener() {
        @Override
        public void onDownloading(long itemFileSize, int totalTs, int curTs) {
            if(idownListener!=null){
                idownListener.onDownloading(itemFileSize,totalTs,curTs);
            }
        }
        @Override
        public void onSuccess() {
            stop();
            if(idownListener!=null){
                idownListener.onSuccess();
            }
        }
        @Override
        public void onProgress(long curLength) {
            if(idownListener!=null){
                idownListener.onProgress(curLength);
            }
        }
        @Override
        public void onStart() {
            if(idownListener!=null){
                idownListener.onStart();
            }
        }
        @Override
        public void onStartDown() {
            if(idownListener!=null){
                idownListener.onStartDown();
            }
        }
        @Override
        public void onError(Throwable errorMsg) {
            stop();
            if(idownListener!=null){
                idownListener.onError(errorMsg);
            }
        }
    };

    OnDownloadListener onDownloadListener=new OnDownloadListener() {
        @Override
        public void onDownloading(long itemFileSize, int totalTs, int curTs) {
            if(downListener!=null) {
                downListener.onDownloading(itemFileSize, totalTs, curTs);
            }
        }
        @Override
        public void onSuccess() {
            if(downListener!=null) {
                downListener.onSuccess();
            }
            stop();
        }
        @Override
        public void onProgress(long curLength) {
            if(downListener!=null) {
                downListener.onProgress(curLength);
            }
        }
        @Override
        public void onStart() {
        }
        @Override
        public void onError(Throwable errorMsg) {
            if(downListener!=null) {
                downListener.onError(errorMsg);
            }
            stop();
        }
    };

    //获取进度
    public int getPerent(){
        String rPath=root+"/"+name;
        final File file=new File(rPath);
        if(!file.exists()){
            return 0;
        }
        int len=file.listFiles().length;
        try {
            Uri uri = Uri.parse(baseUrl);
            String appid = uri.getQueryParameter("v1");//md5 码验证
            M3U8 m3U8= M3u8Conver.getM3u8(new File(root+"/"+name+".m3u8"), name, appid);
            if(m3U8!=null) {
                int tLen = m3U8.getTsList().size();
                double value = len * 1.0 / tLen * 100;
                return (int) value;
            }
            return 0;
        }catch (Exception ex){}
        return 0;
    }

    //停止任务
    public void stop(){
        isRunning=false;
        if(tsLoader!=null) {
            tsLoader.stop();
        }
    }

}
