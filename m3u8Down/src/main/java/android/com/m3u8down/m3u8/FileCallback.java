package android.com.m3u8down.m3u8;

import android.util.Log;

import com.lzy.okgo.callback.AbsCallback;

import java.io.File;

import okhttp3.Response;

public abstract class FileCallback extends AbsCallback<File> {

    private FlieConvert convert;    //文件转换类

    public FileCallback() {
        this(null);
    }

    public FileCallback(String destFileName) {
        this(null, destFileName);
    }

    public FileCallback(String destFileDir, String destFileName) {
        convert = new FlieConvert(destFileDir, destFileName);
        convert.setCallback(this);
    }
    public void setRunning(boolean isRunning){
        convert.setRunning(isRunning);
    }
    @Override
    public File convertResponse(Response response) throws Throwable {
        File file = convert.convertResponse(response);
        Log.e("TAG","关闭连接...");
        response.close();
        return file;
    }
}
