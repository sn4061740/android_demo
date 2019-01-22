package android.com.baselibrary.presenter.base;

public class BasePresent<T> {
    public T view;
    //检查网络
    protected boolean checkNetwork(){

        return true;
    }

    public void attach(T view){
        this.view = view;

    }

    public void detach(){
        this.view = null;

    }
}
