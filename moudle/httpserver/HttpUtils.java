package com.xcore.httpserver;

import com.xcore.MainApplicationContext;

import java.io.IOException;

public class HttpUtils {
    private HttpUtils(){}
    private static HttpUtils instance;

    private HttpServer httpServer;
    public void setHttpCall(IHttpCallBack iHttpCallBack){
        if(httpServer!=null) {
            httpServer.setHttpCallBack(iHttpCallBack);
        }
    }

    public static HttpUtils getInstance() {
        if(instance==null){
            instance=new HttpUtils();
        }
        return instance;
    }
    public boolean start(int port,String sPath){
        boolean isOk=false;
        try {
            httpServer = new HttpServer(port, sPath);
            httpServer.start();
            isOk=true;
        } catch (IOException e) {
            isOk=false;
            //e.printStackTrace();
        }
        return isOk;
    }
}
