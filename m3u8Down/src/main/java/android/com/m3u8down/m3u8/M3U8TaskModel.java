package android.com.m3u8down.m3u8;

import android.com.m3u8down.DownTaskManager;
import android.com.m3u8down.IM3U8DownListener;
import android.com.m3u8down.utils.ErrorUtils;
import android.com.m3u8down.utils.MD5Utils;
import android.util.Log;

public class M3U8TaskModel {

    public enum TaskStatus{
        GETTING,//获取文件信息中
        GET_ERROR,//获取文件信息失败
        WAITING,//等待中
        RUNNING,//下载中,
        COMPLETE,//下载完成
        STOP,//停止中,
        ERROR,//下载出错
    }
//    M3U8DownloadTask task;//任务
    TaskStatus status= TaskStatus.STOP;//任务状态
    String taskId="";//任务Id
    String url;//下载url
    String key;//md5文件验证,判断下载是否完整
    int prent=0;//当前进度
    String conver;//封面
    boolean oldTask=false;//是否是老的下载,是的话只显示下载完成了的
    String title;
    String shortId;
    long fileSize;//文件总大小

    long curSpeed=0;//当前速度

    public long getCurSpeed() {
        return curSpeed;
    }

    public boolean isOldTask() {
        return oldTask;
    }
    public void setOldTask(boolean oldTask) {
        this.oldTask = oldTask;
    }

    //获取状态
    public String getStatusText(){
        String statusStr="停止";
        try {
            switch (status) {
                case STOP:
                    statusStr = "停止";
                    break;
                case RUNNING:
                    statusStr = "下载中";
                    break;
                case ERROR:
                    statusStr = "下载出错";
                    break;
                case WAITING:
                    statusStr = "等待下载";
                    break;
                case COMPLETE:
                    statusStr = "下载完成";
                    break;
                case GETTING:
                    statusStr="获取资源中";
                    break;
                default:
                    statusStr = "停止";
                    break;
            }
        }catch (Exception ex){}
        return statusStr;
    }

    private OnDownListener onDownListener;
    private IOnM3U8LocalListener onM3U8LocalListener;

    public void setOnM3U8LocalListener(IOnM3U8LocalListener onM3U8LocalListener) {
        this.onM3U8LocalListener = onM3U8LocalListener;
    }

    //开始下载
    public void startTask(){
        taskId=MD5Utils.MD5Encode(this.getUrl());
        if(onDownListener==null) {//保证监听还是一个对象
            onDownListener = new OnDownListener(getTaskId());
        }
        status= TaskStatus.RUNNING;
        DownTaskManager.getInstance().download(this.getUrl()+"?v1="+key,onDownListener);

    }
    //停止下载
    public void stopTask(){
        status= TaskStatus.STOP;
        if(onM3U8LocalListener!=null){
            onM3U8LocalListener.onChecked();
        }
        DownTaskManager.getInstance().stop(taskId);
    }

    //设置监听
    public void setDownListener(IM3U8DownListener im3U8DownListener){
        if(onDownListener!=null){
            onDownListener.setIm3U8DownListener(im3U8DownListener);
        }
    }


    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    //获取进度
    public int getPrent() {
        return prent;
    }

    public void setPrent(int prent) {
        this.prent = prent;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getShortId() {
        return shortId;
    }

    public void setShortId(String shortId) {
        this.shortId = shortId;
    }
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTaskId() {
        if(this.getUrl()==null||this.getUrl().length()<=0){
            return System.currentTimeMillis()+"";
        }
        if(this.getUrl()!=null&&(taskId==null||taskId.length()<=0)){
            taskId=MD5Utils.MD5Encode(this.getUrl());
        }
        return taskId;
    }

    public String getConver() {
        return conver;
    }
    public void setConver(String conver) {
        this.conver = conver;
    }
    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }
    class OnDownListener implements IDownListener {
        String taskId;
        private IM3U8DownListener im3U8DownListener;
        int errorCount=0;

        public void setIm3U8DownListener(IM3U8DownListener im3U8DownListener) {
            this.im3U8DownListener = im3U8DownListener;
        }

        public OnDownListener(String tId){
            this.taskId=tId;
        }
        @Override
        public void onDownloading(long itemFileSize, int totalTs, int curTs) {
            try {
                double p = curTs * 1.0 / totalTs * 1.0 * 100;
                int prent1 = (int) p;
                setPrent(prent1);
                double v = fileSize * prent1 / 100;
                if (im3U8DownListener != null) {
                    im3U8DownListener.onProgress(prent1);
                    im3U8DownListener.onCurrentSize((long) v);
                }
            }catch (Exception ex){}
        }
        @Override
        public void onSuccess() {
            try {
                setPrent(100);
                Log.e("TAG", "下载成功");
                status = TaskStatus.COMPLETE;
                if (onM3U8LocalListener != null) {
                    onM3U8LocalListener.onChecked();
                }
                if (im3U8DownListener != null) {
                    im3U8DownListener.onDownSuccess(taskId);
                    im3U8DownListener.onStatusChange();
                }
            }catch (Exception ex){}
        }
        @Override
        public void onProgress(long curLength) {
            curSpeed=curLength;
            if(im3U8DownListener!=null){
                im3U8DownListener.onSpeed(curLength);
            }
        }
        @Override
        public void onStart() {
            errorCount=0;
            status= TaskStatus.GETTING;
        }
        @Override
        public void onStartDown() {
            if(status!= TaskStatus.RUNNING) {
                status = TaskStatus.RUNNING;
            }
            if(im3U8DownListener!=null){
                im3U8DownListener.onStatusChange();
                im3U8DownListener.onStart(taskId);
            }
        }
        @Override
        public void onError(Throwable errorMsg) {
            if(errorCount>=0){
                return;
            }
            errorCount++;
            if(status!= TaskStatus.ERROR) {
                status = TaskStatus.ERROR;
            }
            if(onM3U8LocalListener!=null){
                onM3U8LocalListener.onChecked();
            }
            String eMsg=ErrorUtils.getException(errorMsg);
            if(im3U8DownListener!=null){
                im3U8DownListener.onStatusChange();
                im3U8DownListener.onDownError(taskId,eMsg);
            }
        }
    }
}
