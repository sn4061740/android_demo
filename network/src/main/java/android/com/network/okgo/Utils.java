package android.com.network.okgo;

import com.lzy.okgo.model.HttpHeaders;
import com.lzy.okgo.model.HttpParams;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;

public class Utils {
    //拼接url
    public static String getUrlMap(String url, Map<String,String> params){
        String baseUrl=url;
        if(params!=null&&params.size()>0){
            if(!baseUrl.contains("?")){//如果没有？
                baseUrl+="?";
            }
            int i=0;
            for(String key:params.keySet()){
                if(i==0){
                    baseUrl+=key+"="+params.get(key);
                }else{
                    baseUrl+="&"+key+"="+params.get(key);
                }
                i++;
            }
        }
        return baseUrl;
    }
    //拼接请求头
    public static HttpHeaders getHttpHeader(Map<String,String> headerParams){
        HttpHeaders httpHeaders=null;
        if(httpHeaders!=null&&headerParams.size()>0){
            httpHeaders=new HttpHeaders();
            for(String key:headerParams.keySet()){
                httpHeaders.put(key,headerParams.get(key));
            }
        }
        return httpHeaders;
    }

    //拼接请求参数
    public static HttpParams getHttpParams(Map<String,String> params){
        HttpParams httpParams=null;
        if(params!=null&&params.size()>0){
            httpParams=new HttpParams();
            for(String key:params.keySet()){
                httpParams.put(key,params.get(key));
            }
        }
        return httpParams;
    }

    /**
     * 得到具体的错误消息
     * @param ex
     * @return
     */
    public static String getException(Exception ex) {
        if (ex == null) {
            return "得到的错误消息是 null";
        }
        String ret = ex.getMessage();
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            PrintStream pout = new PrintStream(out);
            ex.printStackTrace(pout);
            ret = new String(out.toByteArray());
            pout.close();
            out.close();
        } catch (Exception e) {
        }
        return ret;
    }
    /**
     * 得到具体的错误消息
     * @param e
     * @return
     */
    public static String getException(Throwable e){
        if(e==null){
            return "";
        }
        try {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw, true);
            e.printStackTrace(pw);
            pw.flush();
            sw.flush();
            return sw.toString();
        }catch (Exception ex){

        }
        return "";
    }

}
