package android.com.glide48;

//图片请求错误监听
public interface IGlideListener {
    void onError(String model, Exception e, long stime);
    void onSuccess();
}
