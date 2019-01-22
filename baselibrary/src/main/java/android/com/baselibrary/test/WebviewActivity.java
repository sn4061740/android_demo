package android.com.baselibrary.test;

import android.com.baselibrary.R;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.ConsoleMessage;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class WebviewActivity extends AppCompatActivity {

    WebView webView;

        @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initWebview();
    }

    private void initWebview(){
        webView=findViewById(R.id.webView);
        String url="http://api01.1avapi.com:8099/CC111.html?data=fsdafsadfsadfsadfsadfsadfsa&t="+System.currentTimeMillis();
//        url="http://192.168.1.8/test.html?data=xxxxxx&t="+System.currentTimeMillis();
        webView.loadUrl(url);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.setWebChromeClient(new MyWebChromeClient());
        webView.setWebViewClient(new WebViewClient() {
            // 复写shouldOverrideUrlLoading()方法，使得打开网页时不调用系统浏览器， 而是在本WebView中显示
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //view.loadUrl(url);
                return true;
                //开始载入页面调用的，我们可以设定一个loading的页面，告诉用户程序在等待网络响应。
            }
            // 当每一个页面加载出来时的动作 可以获取当前页面的信息 如URL 如标题等
            @Override
            public void  onPageStarted(WebView view, String url, Bitmap favicon) {
                //设定加载开始的操作
                // 如可以得到当前的URL
                //current_url=view.getUrl();
                Log.e("TAG","加载开始了...");
            }
            @Override
            public void onPageFinished(WebView view, String url) {
                //设定加载结束的操作
                Log.e("TAG","加载完成了...");
//                String call = "javascript:alertMessage(\"" + "content" + "\")";
//                webView.loadUrl(call);
            }
            //加载页面的服务器出现错误时（如404）调用 使用自定义的错误界面 更符合软件的整体设计风格
            //步骤1：写一个html文件（error_handle.html），用于出错时展示给用户看的提示页面
            //步骤2：将该html文件放置到代码根目录的assets文件夹下
            //步骤3：复写WebViewClient的onRecievedError方法
            //该方法传回了错误码，根据错误类型可以进行不同的错误分类处理
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl){
                Log.e("TAG","出错了...onReceivedError(WebView view, int errorCode, String description, String failingUrl)");
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                Log.e("TAG","出错了...onReceivedError(WebView view, WebResourceRequest request, WebResourceError error)");
            }

            @Override
            public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
                super.onReceivedHttpError(view, request, errorResponse);
                //SDK 》21 的才会进这里
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    int statusCode=errorResponse.getStatusCode();
                    String errMsg=errorResponse.getReasonPhrase();
                    errMsg="WEBVIEW_ERROR_CC错误,错误消息:"+errMsg;
                    Log.e("TAG",errMsg);
                }
            }

            //处理https请求
            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();    //表示等待证书响应
                // handler.cancel();      //表示挂起连接，为默认方式
                // handler.handleMessage(null);    //可做其他处理
                Log.e("TAG","HTTPS 出错？..");
            }
        });
    }

    private class MyWebChromeClient extends WebChromeClient {
        @Override
        public boolean onConsoleMessage(ConsoleMessage cm) {
            Log.d("test", cm.message() + " -- From line "
                    + cm.lineNumber() + " of "
                    + cm.sourceId() );
            return true;
        }
        @Override
        public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
            Toast.makeText(WebviewActivity.this, message, Toast.LENGTH_SHORT).show();
            return true;
        }
    }

}
