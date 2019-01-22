package android.com.network.okgo;

import android.com.network.okgo.call.AbsCall;
import android.util.Log;

import com.google.gson.Gson;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;

import org.json.JSONObject;

import java.lang.reflect.ParameterizedType;

public class StringCall extends StringCallback {
    private AbsCall absCall;
    private String baseUrl="";
    public StringCall(AbsCall call){
        this.absCall = call;
    }
    @Override
    public void onSuccess(Response<String> response) {
        //这里转换好后调用 onNext 返回
        if(absCall!=null){
            String result=response.body();
            Class clazz=getClazz();
            if(clazz==null){//转换的类型为null,返回错误吧。。。
                absCall.onTypeNull();
                return;
            }
            if(clazz==String.class) {
                absCall.onNext(result);
            }else{
                Object o=new Gson().fromJson(result,clazz);
                absCall.onNext(o);
            }
        }
    }
    @Override
    public void onError(Response<String> response) {
        int statusCode=response.code();
        String errMsg=Utils.getException(response.getException());
        if(absCall!=null){
            absCall.requestError(baseUrl,errMsg,statusCode);
        }
    }
    @Override
    public void onStart(Request<String, ? extends Request> request) {
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
    //得到要转换的类型
    private Class<?> getClazz(){
        ParameterizedType type = null;
        try{
            type = (ParameterizedType)absCall.getClass().getGenericSuperclass();//this.getClass().getGenericSuperclass();
            Class<?> result = (Class)type.getActualTypeArguments()[0];
            if(result==null){
                return Object.class;
            }
            return result;
        }catch (ClassCastException e){
            e.printStackTrace();
        }
        return Object.class;
    }
}
