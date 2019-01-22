package android.com.m3u8down.utils;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;

public class ErrorUtils {
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
