package android.com.baselibrary.presenter.view;

import android.com.baselibrary.presenter.base.BasePresent;
import android.com.baselibrary.presenter.base.MainView;

public class MainPresenter extends BasePresent<MainView> {


    public void getNetResult(){
        view.getNetResult();
    }
}
