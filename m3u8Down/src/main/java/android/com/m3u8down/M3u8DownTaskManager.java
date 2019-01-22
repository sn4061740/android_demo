package android.com.m3u8down;

import android.com.m3u8down.m3u8.IOnM3U8LocalListener;
import android.com.m3u8down.m3u8.M3U8TaskModel;
import android.os.Environment;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//下载任务管理
public class M3u8DownTaskManager {

    private M3u8DownTaskManager(){}
    public static int MAX=1;
    private static M3u8DownTaskManager instance;
    public static M3u8DownTaskManager getInstance() {
        if(instance==null){
            instance=new M3u8DownTaskManager();
        }
        return instance;
    }

    //清理空间
    public void clearSpeace(){
        File file=new File(root);
        if(!file.exists()){
            return;
        }
        if(file.isDirectory()){
            File[] files= file.listFiles();
            for(File file1:files){
                String key=file1.getName();
                String name=key;
                if(file1.isFile()&&key.contains(".")){
                    name=key.substring(0,key.indexOf("."));
                }
                M3U8TaskModel model= taskMaps.get(name);
                if(model!=null){
                    continue;
                }
                deleteFolderOrFile(file1);
            }
        }
    }

    //删除文件夹或文件
    private void deleteFolderOrFile(File file){
        if(file.isFile()){
            file.delete();
            return;
        }
        try {
            for (File f : file.listFiles()) {
                if (f.isFile()) {
                    f.delete();
                } else {
                    deleteFolderOrFile(f);
                }
            }
            file.delete();
        }catch (Exception ex){}
    }

    //所有下载任务
    private Map<String,M3U8TaskModel> taskMaps=new HashMap<>();//所有任务
    public Map<String, M3U8TaskModel> getTaskMaps() {
        if(taskMaps==null){
            taskMaps=new HashMap<>();
        }
        return taskMaps;
    }
    //查看是否有等下载中的任务并开始下载
    private IOnM3U8LocalListener onM3U8LocalListener=new IOnM3U8LocalListener() {
        @Override
        public void onChecked() {
            checkTaskAndStart();
        }
    };

    public static String root=Environment.getExternalStorageDirectory().getPath() + File.separator + "www";

    public static void setRoot(String root) {
        M3u8DownTaskManager.root = root;
    }

    //初始化
    public void init(M3U8TaskModel...m3U8TaskModels){
        for(M3U8TaskModel model:m3U8TaskModels){
            taskMaps.put(model.getTaskId(),model);
        }
    }

    public M3U8TaskModel getM3u8TaskModel(String taskId){
        return taskMaps.get(taskId);
    }
    public M3U8TaskModel getM3u8TaskModelByShortId(String shortId){
        M3U8TaskModel model=null;
        Collection<M3U8TaskModel> m3U8TaskModels= taskMaps.values();
        if(m3U8TaskModels!=null&&m3U8TaskModels.size()>0){
            for(M3U8TaskModel taskModel:m3U8TaskModels){
                if(taskModel.getShortId().equals(shortId)){
                    model=taskModel;
                    break;
                }
            }
        }
        return model;
    }

    //停止所有任务
    public void stopTaskAll(){
        stopBatch(taskMaps.keySet());
    }
    public void startTaskAll(){
        startBatch(taskMaps.keySet());
    }

    //添加下载任务
    public boolean addTask(M3U8TaskModel model){
        //1.判断最大下载数
        String tId=model.getTaskId();
        M3U8TaskModel taskModel=null;
        if(runTaskCount()>=MAX){
            if(tId.length()>0){//没有才添加进去
                taskModel= taskMaps.get(tId);
                if(taskModel==null){//没有才添加
                    taskModel=model;
                    taskMaps.put(tId,taskModel);
                }
                taskModel.setOnM3U8LocalListener(onM3U8LocalListener);
                //把当前添加的任务设置为等待中...
                taskModel.setStatus(M3U8TaskModel.TaskStatus.STOP);
            }
            return false;
        }
        if(tId.length()>0){
            taskModel= taskMaps.get(tId);
            if(taskModel==null){//没有才添加
                taskModel=model;
                taskMaps.put(tId,model);
            }
        }else{
            taskModel=model;
        }
        if(taskModel.getStatus()!=M3U8TaskModel.TaskStatus.RUNNING){
            taskModel.setOnM3U8LocalListener(onM3U8LocalListener);
            taskModel.startTask();
        }else{
            taskModel.stopTask();
        }
        return true;
    }
    //批量开始任务(暂停后开始)
    public void startBatch(Collection<String> tasks){
        if(tasks==null||tasks.size()<=0){
            return;
        }
        int runCount=runTaskCount();
        for(String tId:tasks){
            if(runCount>=MAX){
                break;
            }
            M3U8TaskModel model= taskMaps.get(tId);
            if(model!=null&&model.getStatus()!=M3U8TaskModel.TaskStatus.RUNNING){
                runCount++;
                model.startTask();
            }
        }
    }
    //停止任务
    public void stopTask(String taskId){
        M3U8TaskModel model= taskMaps.get(taskId);
        if(model!=null){
            model.stopTask();
        }
    }
    //批量停止任务
    public void stopBatch(Collection<String> tasks){
        if(tasks==null||tasks.size()<=0){
            return;
        }
        for(String tId:tasks){
            stopTask(tId);
        }
    }
    //批量删除任务
    public void deleteTask(String...tasks){
        if(tasks==null||tasks.length<=0){
            return;
        }
        try {
            for (String tId : tasks) {
                stopTask(tId);//先停止任务
                M3U8TaskModel mod = taskMaps.remove(tId);//删除任务
                deleteDiskTask(mod);//删除缓存
            }
        }catch (Exception ex){}
    }

    ExecutorService cachedThreadPool = Executors.newCachedThreadPool();

    private void deleteDiskTask(final M3U8TaskModel mod){
        final M3U8TaskModel model=mod;
        cachedThreadPool.execute(new Runnable() {
            @Override
            public void run() {
            if(model!=null){
                try {
                    String rootPath=root+"/"+model.getTaskId();
                    //删除m3u8 文件
                    String fPath=rootPath+".m3u8";
                    File file=new File(fPath);
                    if(file.exists()){
                        file.delete();
                    }
                    //删除对应的ts 文件夹
                    File dicFile = new File(rootPath);
                    if (dicFile.exists() && dicFile.isDirectory()) {
                        File[] fileList = dicFile.listFiles();
                        for (File f : fileList) {
                            if (f.exists() && f.isFile()) {
                                f.delete();
                            }
                        }
                        dicFile.delete();
                    }
                }catch (Exception ex){}
            }
            }
        });
    }

    //检查任务并开始下载
    private void checkTaskAndStart(){
        Collection<M3U8TaskModel> taskModels= taskMaps.values();
        for(M3U8TaskModel taskModel:taskModels){
            if(taskModel.getStatus()==M3U8TaskModel.TaskStatus.WAITING){
                addTask(taskModel);//添加到开始
                break;
            }
        }
    }

    //运行下载中的数量
    private int runTaskCount(){
        Collection<M3U8TaskModel> taskModels= taskMaps.values();
        int count=0;
        for(M3U8TaskModel model:taskModels){
            if(model.getStatus()==M3U8TaskModel.TaskStatus.RUNNING||model.getStatus()==M3U8TaskModel.TaskStatus.GETTING){
                count++;
            }
        }
        return count;
    }

    //得到当前运行中的第一个任务
    private M3U8TaskModel getCurrentTask(){
        M3U8TaskModel model=null;
        Collection<M3U8TaskModel> taskModels= taskMaps.values();
        for(M3U8TaskModel taskModel:taskModels){
            if(taskModel.getStatus()==M3U8TaskModel.TaskStatus.RUNNING){
                model=taskModel;
                break;
            }
        }
        return model;
    }
}
