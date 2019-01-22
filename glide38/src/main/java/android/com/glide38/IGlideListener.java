package android.com.glide38;

public interface IGlideListener {
    void onError(String model, Exception e, long stime);
    void onSuccess();
}
