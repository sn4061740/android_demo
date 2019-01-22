package com.xcore.httpserver;

import java.util.Map;

public class HttpRout {
    private HttpCall httpCall;
    public HttpRout(HttpCall call){
        this.httpCall=call;
    }
    public NanoHTTPD.Response build(NanoHTTPD.IHTTPSession session){
        String uri=session.getUri();
        Map<String,String> params=session.getParms();
        if(uri.equals("/stop")){
            if(httpCall!=null){
                return httpCall.stopAll();
            }
        }else if(uri.equals("/stopAll")){
            if(httpCall!=null){
                return httpCall.stopAll();
            }
            return httpCall.responseError("httpCall 为null");
        }else if(uri.equals("/start")){
            if(params==null||params.size()<=0){
                if(httpCall!=null){
                   return httpCall.responseError("uri 不能为null");
                }
                return httpCall.responseError("httpCall 为null");
            }
            try {
	            String uriStr=params.get("uri");
	            String priv=params.get("priv");
	            String v1=params.get("v1");
	            if(httpCall!=null){
	            	return httpCall.start(uriStr, priv, v1);
	            }
            }catch(Exception ex) {}
            return httpCall.responseError("httpCall 为null");
        }
        return httpCall.responseError("uri 为其他。。。");
    }
}
