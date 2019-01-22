package android.com.m3u8down;

public interface IM3U8DownListener {
    void onStart(String taskId);//开始下载
    void onDownSuccess(String taskId);//下载成功
    void onDownError(String taskId, String errMsg);//下载错误

    void onProgress(int prerent);//进度
    void onSpeed(long speed);//速度

    void onCurrentSize(long curLeng);
    void onStatusChange();
}
