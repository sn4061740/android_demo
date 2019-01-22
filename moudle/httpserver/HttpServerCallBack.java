package com.xcore.httpserver;

import android.content.Context;
import android.os.Build;
import android.os.Message;
import android.webkit.WebSettings;

import com.xcore.MainApplication;
import com.xcore.MainApplicationContext;
import com.xcore.utils.LogUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Hashtable;
import java.util.Map;

public abstract class HttpServerCallBack extends NanoHTTPD implements HttpCall {
    private Map<String,InputStream> infos=new Hashtable<>();
    private String cKey = "ABCDEFGHIKLMNOPQ";
    private String sRoot;

    HttpURLConnection conn;
    private IHttpCallBack httpCallBack;

    String userAgent="http_ua";

    public void setHttpCallBack(IHttpCallBack httpCallBack) {
        this.httpCallBack = httpCallBack;
    }

    public HttpServerCallBack(int port, String sdRoot) {
        super(port);
        this.sRoot=sdRoot;
//        userAgent=getUserAgent(MainApplicationContext.context);
    }

    private String getUserAgent(Context ctx) {
//        String userAgent = "";
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
//            try {
//                if(ctx!=null) {
//                    userAgent = WebSettings.getDefaultUserAgent(ctx);
//                }else {
//                    userAgent = System.getProperty("http.agent");
//                }
//            } catch (Exception e) {
//                userAgent = System.getProperty("http.agent");
//            }
//        } else {
//            userAgent = System.getProperty("http.agent");
//        }
//        try {
//            StringBuffer sb = new StringBuffer();
//            for (int i = 0, length = userAgent.length(); i < length; i++) {
//                char c = userAgent.charAt(i);
//                if (c <= '\u001f' || c >= '\u007f') {
//                    sb.append(String.format("\\u%04x", (int) c));
//                } else {
//                    sb.append(c);
//                }
//            }
//            return sb.toString();
//        }catch (Exception ex){}
        return "okhttp/agent";
    }

    @Override
    public Response stop(String uri) {
        try{
            if(conn!=null){//关闭连接
                conn.disconnect();
            }
            conn=null;
        }catch (Exception ex){}
        //关闭流
        try {
            for (InputStream fis : infos.values()) {
                if (fis != null) {
                    try {
                        fis.close();
                    } catch (IOException e) {
                    }
                }
            }
        }catch (Exception ex){}
        infos=new Hashtable<>();
        return newFixedLengthResponse("停止失败");
    }

    @Override
    public Response stopAll() {
        try {
            for (InputStream fis : infos.values()) {
                if (fis != null) {
                    try {
                        fis.close();
                    } catch (IOException e) {
                    }
                }
            }
        }catch (Exception ex){}
        infos=new Hashtable<>();
       
        return newFixedLengthResponse("停止成功");
    }

    @Override
    public Response start(String uri) {//根据uri 来开始任务
        String uStr=uri;
        try {
            URL url=new URL(uStr);
            String path=url.getPath();
            String auth=url.getHost();
            if(!auth.equals("127.0.0.1")){
                return responseError("auth 验证失败");
            }
            String fpath=sRoot+path;//源路径
            String oPath= Md5Util.byteToHexString(uStr.getBytes());
            try {
                InputStream fis=AESUtils.getInputStream(fpath);
                if(fis==null){
                    return responseError("fis 获取流为null");
                }
                infos.put(oPath,fis);
                return newFixedLengthResponse(Response.Status.OK, "application/octet-stream", fis, fis.available());
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return newFixedLengthResponse("开始失败,出现异常...");
    }
    @Override
    public Response start(String uri,String p) {//根据uri 来开始任务
        String uStr=uri;
        String errorStr="";
        int statusCode=-1;
        try {
        	URL url = new URL(uStr);
    		conn = (HttpURLConnection)url.openConnection();
    		conn.setRequestMethod("GET");
            conn.setConnectTimeout(10*1000);
            conn.setRequestProperty("User-agent",userAgent);//为连接设置ua
            conn.setRequestProperty("Connection","close");

    		statusCode=conn.getResponseCode();
    		if(statusCode==200) {
                InputStream inStream = conn.getInputStream();//通过输入流获取数据
                String oPath = Md5Util.byteToHexString(uStr.getBytes());
//                InputStream fis = AESUtils.getInputStream(inStream, p.substring(0, 16), p.substring(16),MainApplicationContext.M_PATH+"1.m3u8");
                InputStream fis = AESUtils.getInputStream(inStream, p.substring(0, 16), p.substring(16));
                if (fis == null) {
                    return responseError("fis 获取流为null");
                }
                infos.put(oPath, fis);
                return newFixedLengthResponse(Response.Status.OK, "application/octet-stream", fis, fis.available());
            }else{
                String error="M3U8请求失败:";
                try {
                    errorStr=conn.getResponseMessage();
                    errorStr=error+errorStr;
                }catch (Exception ex){}
            }
        } catch (Exception e) {
            errorStr=LogUtils.getException(e);
        }
        if(httpCallBack!=null){
            httpCallBack.onM3u8Error(errorStr,statusCode);
        }
        return null;//newFixedLengthResponse("开始失败,出现异常...");
    }

    @Override
    public Response start(String uri,String p,String v) {
        try {
            if(p!=null&&p.length()>0&&"1".equals(p)) {//是1
            	return start(uri,v);
            }else {
            	return start(uri);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return newFixedLengthResponse("开始失败,出现异常...");
    }
    
    private boolean isEmpty(String s){
        return s==null||s.length()<=0;
    }
    @Override
    public Response responseError(String msg) {
        return newFixedLengthResponse(msg);
    }
}
