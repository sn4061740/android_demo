package android.com.m3u8down.m3u8;

/**
 * 监听基类
 * Created by HDL on 2017/8/10.
 */

public interface BaseListener {
    /**
     * 开始的时候回调
     */
    void onStart();

    /**
     * 错误的时候回调
     *
     * @param errorMsg
     */
    void onError(Throwable errorMsg);
}