package android.com.glide48;

import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

public class GlideRequestListener implements RequestListener<Drawable> {
    IGlideListener glideErrorListener;
    long startTime=System.currentTimeMillis();
    public GlideRequestListener(IGlideListener iGlideErrorListener){
        this.glideErrorListener=iGlideErrorListener;
    }

    @Override
    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
        if(glideErrorListener!=null){
            long endTime=System.currentTimeMillis();
            long eTime= endTime-startTime;
            glideErrorListener.onError((String) model,e,eTime);
        }
        return false;
    }
    @Override
    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
        return false;
    }
}
