package android.com.m3u8down;

import android.com.m3u8down.m3u8.IDownListener;
import android.com.m3u8down.m3u8.M3U8TaskModel;
import android.com.m3u8down.m3u8.M3u8Loder;
import android.com.m3u8down.utils.MD5Utils;
import android.com.m3u8down.utils.SystemUtils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class DownTaskManager {
    private DownTaskManager(){
        root=M3u8DownTaskManager.root;
    }

    private static DownTaskManager instance;
    public static DownTaskManager getInstance() {
        if(instance==null){
            instance=new DownTaskManager();
        }
        return instance;
    }
    Map<String,M3u8Loder> m3u8LoderMap=new HashMap<>();
    String root;

    //下载
    public void download(final String url, IDownListener iDownListener) {
        String name=MD5Utils.MD5Encode(url.substring(0,url.indexOf("?")));
        M3u8Loder m3u8Loder= m3u8LoderMap.get(name);
        if(m3u8Loder==null){
            m3u8Loder=new M3u8Loder(url,name,iDownListener);
            m3u8LoderMap.put(name,m3u8Loder);
            m3u8Loder.loader();
        }else{//如果是正在运行的则作文
            if(m3u8Loder.isRunning()){
                m3u8Loder.stop();
            }else{//否则就开始
                m3u8Loder.loader();
            }
        }
    }


    //初始化进度
    public void initLoader(M3U8TaskModel model){
        String url=model.getUrl();
        String name= MD5Utils.MD5Encode(url);
        String rPath=root+"/"+name;
        final File file=new File(rPath);
        if(!file.exists()){
            model.setPrent(0);
            return;
        }
        if(model.getFileSize()>0){
            try {
                long folderSize = SystemUtils.getFolderSize(file);
                double v = folderSize * 1.0 / model.getFileSize()*100;
                model.setPrent((int) v);
            }catch (Exception ex){}
        }else {
            M3u8Loder loder = new M3u8Loder(url + "?v1=" + model.getKey(), name, null);
            int perent = loder.getPerent();
            model.setPrent(perent);
        }
    }

    //停止某个任务
    public void stop(String taskId){
        M3u8Loder m3u8Loder= m3u8LoderMap.get(taskId);
        if(m3u8Loder!=null){
            m3u8Loder.stop();
        }
    }

}
