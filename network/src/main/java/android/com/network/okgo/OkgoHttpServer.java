package android.com.network.okgo;

import android.com.network.IHttpServer;
import android.com.network.okgo.call.AbsCall;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.HttpHeaders;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.request.GetRequest;
import com.lzy.okgo.request.PostRequest;

import java.util.Map;

public class OkgoHttpServer implements IHttpServer {

    @Override
    public void get(String url, final AbsCall absCall, Map<String,String> headers) {
        final String xUrl=url;
        HttpHeaders httpHeaders= Utils.getHttpHeader(headers);
        GetRequest getRequest=OkGo.get(xUrl);
        if(httpHeaders!=null){
            getRequest.headers(httpHeaders);
        }
        if(absCall!=null&&absCall.getTag()!=null){
            getRequest.tag(absCall.getTag());
        }
        getRequest.execute(new StringCall(absCall));
    }

    @Override
    public void post(String url, AbsCall absCall, Map<String,String> headers,Map<String,String> params) {
        final String xUrl=url;
        HttpHeaders httpHeaders= Utils.getHttpHeader(headers);
        PostRequest postRequest=OkGo.post(xUrl);
        if(httpHeaders!=null){
            postRequest.headers(httpHeaders);
        }
        HttpParams httpParams= Utils.getHttpParams(params);
        if(httpParams!=null){
            postRequest.params("from","en");
            postRequest.params("to","zh");
            postRequest.params("query","Test");
        }
        if(absCall!=null&&absCall.getTag()!=null){
            postRequest.tag(absCall.getTag());
        }
        postRequest.execute(new StringCall(absCall));
    }

    @Override
    public void getFile(String url, final AbsCall absCall, String rootPath, String fileName, Map<String, String> headers) {
        final String xUrl=url;
        HttpHeaders httpHeaders= Utils.getHttpHeader(headers);
        GetRequest getRequest=OkGo.get(xUrl);
        if(absCall!=null&&absCall.getTag()!=null){
            getRequest.tag(absCall.getTag());
        }
        if(httpHeaders!=null){
            getRequest.headers(httpHeaders);
        }
        getRequest.execute(new FileCall(absCall));
    }

    @Override
    public void onCancel(Object tag) {
        OkGo.getInstance().cancelTag(tag);
    }
}
