package android.com.m3u8down;

import java.io.IOException;

public class M3u8Utils {
    private M3u8Utils(){}

    private static M3u8Utils instance;

    public static M3u8Utils getInstance() {
        if(instance==null){
            instance=new M3u8Utils();
        }
        return instance;
    }
    PlayServer playServer;
    int port=8080;

    public int getPort() {
        return port;
    }
    private IPlayListener playListener;

    public void setPlayListener(IPlayListener playListener) {
        this.playListener = playListener;
    }

    IPlayListener iPlayListener=new IPlayListener() {
        @Override
        public void onError(String model, String errMsg, int code) {
            if(playListener!=null){
                playListener.onError(model,errMsg,code);
            }
        }
    };

    public void startServer(){
        new Thread(new Runnable() {
            @Override
            public void run() {
            for(int i=0;i<10;i++) {
                try {
                    playServer = new PlayServer(null, port);
                    playServer.setPlayListener(iPlayListener);
                    playServer.setRoot(M3u8DownTaskManager.root);
                    playServer.start();
                    Thread.sleep(3000);
                    break;
                } catch (IOException e) {
                    //e.printStackTrace();
                    port++;
                } catch (InterruptedException e) {
                    //e.printStackTrace();
                    port++;
                }
            }
            }
        }).start();
    }

}
