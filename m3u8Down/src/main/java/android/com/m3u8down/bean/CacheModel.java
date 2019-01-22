package android.com.m3u8down.bean;

import android.com.m3u8down.utils.MD5Utils;

public class CacheModel {
    private String url;
    private String title;
    private String conver;
    private int prent;
    private String taskId;
    private String shortId;

    private boolean selected=false;

    public String getTaskId(){
        if(taskId==null||taskId.length()<=0) {
            return MD5Utils.MD5Encode(getUrl());
        }
        return taskId;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getShortId() {
        return shortId;
    }

    public void setShortId(String shortId) {
        this.shortId = shortId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getConver() {
        return conver;
    }

    public void setConver(String conver) {
        this.conver = conver;
    }

    public int getPrent() {
        return prent;
    }

    public void setPrent(int prent) {
        this.prent = prent;
    }
}
