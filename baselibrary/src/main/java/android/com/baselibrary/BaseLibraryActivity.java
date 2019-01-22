package android.com.baselibrary;

import android.com.baselibrary.base.MvpActivity;
import android.com.baselibrary.presenter.base.MainView;
import android.com.baselibrary.presenter.view.MainPresenter;
import android.com.baselibrary.test.WebviewActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

public class BaseLibraryActivity extends MvpActivity<MainView,MainPresenter> implements MainView{
    View.OnClickListener onClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(v.getId()==R.id.btn_webView){
                Intent intent=new Intent(BaseLibraryActivity.this,WebviewActivity.class);
                startActivity(intent);
            }
        }
    };

    WebView webView;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_baselibrary;
    }
    @Override
    protected void initView(Bundle savedInstanceState) {
        TextView txtInfo= findViewById(R.id.txt_info);
        txtInfo.setText("测试测试测试...");

        findViewById(R.id.btn_webView).setOnClickListener(onClickListener);
    }

    @Override
    public void initData() {
        presenter.getNetResult();
    }

    @Override
    public void getNetResult() {
        Log.e("TAG","返回结果了...");
    }
    @Override
    public MainPresenter initPresenter() {
        return new MainPresenter();
    }


//    @Override
//    public void onDestroy() {
////        super.onDestroy();
//        super.onDestroy();
//        if(webView!=null){
//            webView.destroy();
//        }
//
//    }


}
