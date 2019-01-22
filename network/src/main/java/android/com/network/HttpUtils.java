package android.com.network;

import android.com.network.okgo.OkgoHttpServer;
import android.com.network.okgo.Utils;
import android.com.network.okgo.call.AbsCall;

import com.lzy.okgo.OkGo;

import java.io.File;
import java.util.Map;

public class HttpUtils {
    private HttpUtils(){
        this.httpServer=new OkgoHttpServer();
    }
    private static HttpUtils instance;
    public static HttpUtils getInstance(){
        if(instance==null){
            instance=new HttpUtils();
        }
        return instance;
    }
    private IHttpServer httpServer;
    //初始化
    public void init(IHttpServer iHttpServer){
        httpServer=iHttpServer;
    }
    //////////////////////////////////////////////////////
    ///// GET
    public void get(String url, AbsCall httpCall, Map<String, String> headers, Map<String, String> params){
        if(httpServer==null){
            return;
        }
        String baseUrl=Utils.getUrlMap(url,params);
        httpServer.get(baseUrl,httpCall,headers);
    }

    ///////////////////////////////////////////////////
    ///// POST
    public void post(String url, AbsCall absCall, Map<String, String> headers, Map<String, String> params){
        if(httpServer==null){
            return;
        }
        httpServer.post(url,absCall,headers,params);
    }


    /**
     * 请求下载文件
     * @param url               请求URL
     * @param fileHttpCall     回调
     * @param rootPath          根目录(有默认值)
     * @param fileName          保存的文件名
     * @param headers           请求头
     * @param params            请求参数
     */
    public void getFile(String url, AbsCall<File> fileHttpCall, String rootPath, String fileName, Map<String,String> headers, Map<String,String> params){
        String xUrl=Utils.getUrlMap(url,params);
        httpServer.getFile(xUrl,fileHttpCall,rootPath,fileName,headers);
    }

    //取消
    public void onCancel(Object tag){
        httpServer.onCancel(tag);
    }

}
