package com.xcore.httpserver;

import com.xcore.httpserver.NanoHTTPD;

public interface HttpCall {
    NanoHTTPD.Response stop(String uri);//停止单个任务
    NanoHTTPD.Response stopAll();//停止所有任务
    NanoHTTPD.Response start(String uri);//开始一个任务
    NanoHTTPD.Response start(String uri, String p);//开始一个任务
    NanoHTTPD.Response start(String uri, String p, String v);//开始一个任务
    NanoHTTPD.Response responseError(String msg);
}
