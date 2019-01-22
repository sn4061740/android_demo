package android.com.glide48;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;

public class GlideUtils {
    private GlideUtils(){}
    private static GlideUtils instance;
    public static GlideUtils getInstance(){
        if(instance==null){
            instance=new GlideUtils();
        }
        return instance;
    }
    public void init(IGlideListener  iGlideListener){
        this.glideListener=iGlideListener;
    }
    private IGlideListener glideListener;
    public IGlideListener getGlideErrorListener() {
        return glideListener;
    }

    private int placeholderRes=R.drawable.default_load;
    public void setPlaceholder(int res){
        this.placeholderRes=res;
        options.placeholder(res);
    }

    RequestOptions options=new RequestOptions().placeholder(placeholderRes)
            .centerCrop()
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
            .dontAnimate()
            .dontTransform();

    //加载image 图片
    public void load(String url, ImageView img){
        if(verify(url)){
            return;
        }
        load(img.getContext(),url,img);
    }
    //加载image 图片
    public void load(Context context,String url,ImageView img){
        if(verify(url)){
            return;
        }
        GlideApp.with(context)
            .load(url)
            .thumbnail(0.1f)
            .apply(options).transition(DrawableTransitionOptions.withCrossFade(200))
            .addListener(new GlideRequestListener(glideListener))
            .centerCrop().into(img);
    }
    //加载webp 图片
    public void loadWebp(String url,ImageView img){
        if(verify(url)){
            return;
        }
        load(img.getContext(),url,img);
    }
    //加载webp 图片
    public void loadWebp(Context  context, String url,ImageView img){
        if(verify(url)){
            return;
        }
        load(context,url,img);
    }

    //清理内存
    public void clearMemory(Context context,int level){
        GlideApp.get(context).clearMemory();
        GlideApp.get(context).trimMemory(level);
    }
    //低内存时清理
    public void onLowMemory(Context context){
        try {
            GlideApp.get(context).clearMemory();
        }catch (Exception ex){}
    }

    //验证
    private boolean verify(String url){
        if(isEmpty(url)){
            if(glideListener!=null){
                glideListener.onError(url,new Exception("加载图片地址为空"),0);
            }
            return true;
        }
        return false;
    }
    private boolean isEmpty(String s){
        return s==null||s.length()<=0;
    }

}
