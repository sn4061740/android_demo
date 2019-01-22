package android.com.m3u8down;

import android.com.m3u8down.m3u8.NanoHTTPD;
import android.com.m3u8down.utils.AESUtils;
import android.com.m3u8down.utils.ErrorUtils;
import android.com.m3u8down.utils.MD5Utils;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

public class PlayServer extends NanoHTTPD {
    String root="";
    boolean cacheBelow=false;//边看边缓存,暂时无用。

    public void setRoot(String root) {
        this.root = root;
    }

    private IPlayListener playListener;

    public void setPlayListener(IPlayListener playListener) {
        this.playListener = playListener;
    }

    public PlayServer(String hostname, int port) {
        super(hostname, port);
        File file=new File(root);
        if(!file.exists()){//文件夹不存在就创建http://m01.xxxlutubexxx.com/m3u8/xsp1545724908-480.m3u8?v1=b24w9AwfAspg3QfpWUGuTPZCi8E48v8P
            file.mkdir();
        }
    }

    @Override
    public Response serve(IHTTPSession session) {
        String uri=session.getUri();
        Map<String,String> params=session.getParms();
        String qStr=session.getQueryParameterString();
        int startIndex1=qStr.indexOf("{");
        int lastIndex1=qStr.indexOf("}");
        String vPath=qStr.substring(startIndex1+1,lastIndex1);//params.get("uri");
        Log.e("TAG","请求URL"+uri+"请求URI="+vPath);
        String key=MD5Utils.MD5Encode(vPath);
        if(uri.contains("play")){//播放
            if(vPath.contains(".m3u8")){//请求的m3u8
                try {
                    String fPath=root+key+".m3u8";
                    File file=new File(fPath);
                    String v1=params.get("v1");
                    InputStream in=null;
                    if(file.exists()){
                        in=new FileInputStream(file);//直接解密文件流
                        InputStream inputStream=getPlayLocalEncodeFileInputStream(in,v1,key);
                        return newFixedLengthResponse(Response.Status.OK, "application/octet-stream", inputStream, inputStream.available());
                    }else{//网络播放
                        //请求网络，但不下载
                        URL url = new URL(vPath);
                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                        conn.setConnectTimeout(10000);
                        conn.setReadTimeout(10000);
                        int code=conn.getResponseCode();
                        if ( code== 200) {
                            in = conn.getInputStream();
                            InputStream inputStream=null;
                            if(cacheBelow){
                                inputStream=AESUtils.getInputStreamV2(in,v1,key,file);//getPlayLocalEncodeFileInputStream(in,v1,key);
                            }else {
                                inputStream = AESUtils.getInputStream(in, v1.substring(0, 16), v1.substring(16));
                            }
                            if(inputStream==null){
                                return newFixedLengthResponse("错误");
                            }
                            return newFixedLengthResponse(Response.Status.OK, "application/octet-stream", inputStream, inputStream.available());
                        }else{
                            if(playListener!=null){
                                String errMsg=conn.getResponseMessage();
                                playListener.onError(vPath,errMsg,code);
                            }
                        }
                    }
                }catch (Exception ex){
                    String errMsg=ErrorUtils.getException(ex);
                    if(playListener!=null) {
                        playListener.onError(vPath, errMsg, -1);
                    }
                }
            }else if(vPath.contains(".key")){
                try {
                    String v1=params.get("v1");
                    int lastIndex=vPath.lastIndexOf("/");
                    String name=vPath.substring(lastIndex+1);
                    String fPath = root+v1 +"/"+ name;
                    File file = new File(fPath);
                    if (file.exists()) {
                        InputStream inputStream = new FileInputStream(file);//直接解密文件流
                        return newFixedLengthResponse(Response.Status.OK, "application/octet-stream", inputStream, inputStream.available());
                    }else{
                        return newFixedLengthResponse("错误");
                    }
                }catch (Exception ex){}
            }else if(vPath.contains(".ts")){
                try {
                    String v1=params.get("v1");
                    int lastIndex=vPath.lastIndexOf("/");
                    String name=vPath.substring(lastIndex+1);
                    String fPath = root+v1 +"/"+ name;
                    File file = new File(fPath);
                    if (file.exists()) {
                        InputStream in = new FileInputStream(file);//直接解密文件流
                        return newFixedLengthResponse(Response.Status.OK, "application/octet-stream", in, in.available());
                    }else{
                        return newFixedLengthResponse("错误");
                    }
                }catch (Exception ex){
                }
            }
        }else if(uri.contains("start")){
            try {
                String v1=params.get("v1");
                //请求网络，但不下载
                URL url = new URL(vPath);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(10000);
                conn.setReadTimeout(10000);
                int code=conn.getResponseCode();
                if ( code== 200) {
                    InputStream in = conn.getInputStream();
                    InputStream inputStream= AESUtils.getInputStream(in, v1.substring(0, 16), v1.substring(16));
                    if(inputStream==null){
                        return newFixedLengthResponse("错误");
                    }
                    return newFixedLengthResponse(Response.Status.OK, "application/octet-stream", inputStream, inputStream.available());
                }else{//错误
                    if(playListener!=null){
                        String errMsg=conn.getResponseMessage();
                        playListener.onError(vPath,errMsg,code);
                    }
                }
            }catch (Exception ex){
                if(playListener!=null){
                    String errMsg=ErrorUtils.getException(ex);
                    playListener.onError(vPath,errMsg,-1);
                }
            }
        }
        return newFixedLengthResponse("错误");
    }
    private InputStream getPlayLocalEncodeFileInputStream(InputStream in,String v,String md5){
        try {
            InputStream inputStream=null;
            //FileInputStream in=new FileInputStream(file);
            //解密并且替换内容 把ts 请求地址换成本地的

            return AESUtils.getInputStreamV1(in,v,md5);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
