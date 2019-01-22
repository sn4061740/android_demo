package android.com.baselibrary.base;

import android.com.baselibrary.presenter.base.BasePresent;
import android.os.Bundle;
import android.support.annotation.Nullable;

public abstract class MvpActivity<V,P extends BasePresent<V>> extends BaseActivity{

    protected P presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        presenter = initPresenter();
        presenter.attach((V) this);
        super.onCreate(savedInstanceState);
        if (getLayoutId() != 0) {
            setContentView(getLayoutId());
        }

        initView(savedInstanceState);
        initData();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        if(presenter!=null) {
            presenter.detach();
        }
        super.onDestroy();
    }
    public abstract P initPresenter();
    public String getParamsStr(){
        return "这个是"+className+"页面哦!!!";
    }


}