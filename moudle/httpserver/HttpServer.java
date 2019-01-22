package com.xcore.httpserver;

public class HttpServer extends HttpServerCallBack {

    public HttpServer(int port,String sPath) {
        super(port,sPath);
    }
    //当接受到连接时会调用此方法
    public Response serve(IHTTPSession session){
        return new HttpRout(this).build(session);
    }
}
