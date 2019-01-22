package android.com.network;

import android.com.network.okgo.call.AbsCall;

import java.util.Map;

public interface IHttpServer {
    //GET 请求
    void get(String url, AbsCall absCall, Map<String,String> headers);

    //POST 请求
    void post(String url, AbsCall absCall, Map<String,String> headers,Map<String,String> params);

    void getFile(String url, AbsCall fileHttpCall,String rootPath,String fileName, Map<String, String> headers);

    void onCancel(Object tag);
}
