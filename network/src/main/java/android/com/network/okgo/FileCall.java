package android.com.network.okgo;

import android.com.network.okgo.call.AbsCall;
import android.com.network.okgo.call.FileConvert;
import android.util.Log;

import com.lzy.okgo.callback.AbsCallback;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;

import java.io.File;

public class FileCall extends AbsCallback<File> {
    String baseUrl="";
    private AbsCall<File> absCall;
    private FileConvert convert;    //文件转换类

    public FileCall() {
        this("");
    }

    public FileCall(String destFileName) {
        this(null, destFileName);
    }

    public FileCall(String destFileDir, String destFileName) {
        convert = new FileConvert(destFileDir, destFileName);
        convert.setCallback(this);
    }

    public FileCall(AbsCall<File> call){
        this();
        this.absCall=call;
    }
    public FileCall(AbsCall<File> call,String destFileDir,String destFileName){
        this(destFileDir,destFileName);
        this.absCall=call;
    }

    @Override
    public void onStart(Request<File, ? extends Request> request) {
        baseUrl=request.getBaseUrl();
        if(absCall!=null){
            absCall.onStart(request);
        }
    }

    @Override
    public void downloadProgress(Progress progress) {
        if(absCall!=null){
            absCall.downloadProgress(progress.speed);
        }
    }

    @Override
    public void onSuccess(Response<File> response) {
        if(absCall!=null) {
            absCall.onNext(response.body());
        }
    }

    @Override
    public void onError(Response<File> response) {
        if(absCall!=null){
            int statusCode=response.code();
            String errMsg=Utils.getException(response.getException());
            absCall.requestError(baseUrl,errMsg,statusCode);
        }
    }

    @Override
    public File convertResponse(okhttp3.Response response) throws Throwable{
        File file = convert.convertResponse(response);
        response.close();
        return file;
    }
}
