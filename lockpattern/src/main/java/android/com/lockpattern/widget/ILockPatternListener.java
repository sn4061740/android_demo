package android.com.lockpattern.widget;

import android.os.Parcelable;

import java.io.Serializable;

public interface ILockPatternListener {
    void onOpenSuccess();//解锁成功
    void onSetSuccess();//设置成功

    void onCancelOpenSuccess();//取消打开
    void onCancelSetSuccess();//取消设置
}
