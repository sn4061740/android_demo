package android.com.m3u8down.m3u8;

import android.com.m3u8down.M3u8DownTaskManager;
import android.com.m3u8down.utils.ErrorUtils;
import android.util.Log;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.model.Response;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

public class TsLoader {
    private M3U8 m3U8;
    private String root;
    private String folder;
    private OnDownloadListener onDownloadListener;
    private int totalTs=0;//总ts
    private int curTs=0;//当前ts
    private boolean isRunning=false;
    private FileCallback fileCallback=null;

    private long currentSpeed=0;
    private Timer speedTimer;

    public TsLoader(M3U8 m3U81, OnDownloadListener downloadListener){
        this.m3U8=m3U81;
        this.onDownloadListener=downloadListener;
        root=M3u8DownTaskManager.root;
        folder=m3U81.getBasepath();
    }
    public void loader(){
        if(this.m3U8==null){
            handlerError(new Throwable("M3U8 is null"));
            return;
        }
        totalTs=m3U8.getTsList().size();
        curTs=0;
        isRunning=true;
        onStartLoader();
        //startSpeedTimer();
    }
    private long lastSpeed=0;
    private void startSpeedTimer(){
        if(speedTimer==null){
            speedTimer=new Timer();
            speedTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    if(onDownloadListener!=null) {
                        long cLeng=currentSpeed-lastSpeed;
                        Log.e("TAG","cLeng="+cLeng);
                        if(cLeng>0) {
                            onDownloadListener.onProgress(cLeng);
                        }
                        lastSpeed=currentSpeed;
                    }
                    if(!isRunning){
                        stopSpeedTimer();
                    }
                }
            },1000,1500);
        }
    }
    private void stopSpeedTimer(){
        if(speedTimer!=null){
            speedTimer.cancel();
        }
        speedTimer=null;
    }

    private void onStartLoader() {
        if(isRunning==false){
            return;
        }
        Log.e("TAG","加载当前的ts--"+curTs);
        if (curTs < totalTs) {
            onDownloadListener.onStart();
            M3U8Ts m3U8Ts = m3U8.getTsList().get(curTs);
            String fPath = root + "/" + folder + "/" + m3U8Ts.getFileName();
            //判断文件的有效性，验证文件大小
            File file = new File(fPath);
            long len=file.length();
            if (!file.exists()||len<=0) {//下载过的或者文件不完整的就重新下载
                if(fileCallback!=null){
                    fileCallback.setRunning(false);
                }
                fileCallback=new FileCallback(root + "/" + folder + "", m3U8Ts.getFileName()) {
                    @Override
                    public void onSuccess(Response<File> response) {
                        File file1 = response.body();
                        if (file1 != null) {//下载成功
                            curTs++;
                            onStartLoader();
                        } else {//下载失败
                            onDownloadListener.onError(new Throwable("-1:下载文件为null"));
                        }
                    }
                    @Override
                    public void downloadProgress(Progress progress) {//速度
                        long sp=progress.speed;
                        Log.e("TAG", "文件名:" + progress.filePath + "总大小:" + progress.totalSize + " 本次大小:"
                                + progress.currentSize + " 速度" +sp);

                        currentSpeed+=sp;
                        onDownloadListener.onProgress(sp);
                        onDownloadListener.onDownloading(progress.totalSize, totalTs, curTs);
                    }
                    @Override
                    public void onError(Response<File> response) {
                        int code = response.code();
                        String errMsg = ErrorUtils.getException(response.getException());
                        onDownloadListener.onError(new Throwable(code + "::" + errMsg));
                    }
                };
                fileCallback.setRunning(isRunning);
                OkGo.<File>get(m3U8Ts.getFile()).execute(fileCallback);
            }else{
                curTs++;
                onStartLoader();
            }
        }else{
            onDownloadListener.onSuccess();
        }
    }
    public void stop(){
        isRunning=false;
        //stopSpeedTimer();
        if(fileCallback!=null){
            fileCallback.setRunning(isRunning);
        }
    }
    /**
     * 通知异常
     *
     * @param e
     */
    private void handlerError(Throwable e) {
        if (!"Task running".contains(e.getMessage())) {
            onDownloadListener.onError(e);
        }
        //不提示被中断的情况
        if ("thread interrupted".contains(e.getMessage())) {
            return;
        }
        onDownloadListener.onError(e);
    }
}
