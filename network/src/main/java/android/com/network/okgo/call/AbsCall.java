package android.com.network.okgo.call;

import android.util.Log;

import com.lzy.okgo.request.base.Request;

/**
 * 泛型类 T 必须传,不然默认为 Object.class
 * @param <T>
 */
public abstract class AbsCall<T> {
    protected Object tag;
    public Object getTag(){
        return null;
    }
    public void onStart(Request request){
        String url=request.getBaseUrl();
        Log.e("Okgo","请求地址:"+url);
    }
    public void downloadProgress(long speed){
        Log.e("Okgo","请求速度:"+speed);
    }

    public void requestError(String model,String errMsg,int code){
        onError(model,errMsg,code);

        //统一的错误 发送到服务器
    }

    //错误
    public abstract void onError(String model,String errMsg,int code);
    public abstract void onNext(T result);

    public void onTypeNull(){
        Log.e("Okgo","请求的泛型类型为null");
    }
}
