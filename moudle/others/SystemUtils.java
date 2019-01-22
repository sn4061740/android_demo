package com.xcore.utils;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.widget.Toast;

import com.xcore.MainApplicationContext;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.lang.reflect.Method;
import java.net.NetworkInterface;
import java.security.MessageDigest;
import java.text.DecimalFormat;
import java.util.Enumeration;
import java.util.Locale;

public class SystemUtils {
    public static final int SIZETYPE_B = 1;//获取文件大小单位为B的double值
    public static final int SIZETYPE_KB = 2;//获取文件大小单位为KB的double值
    public static final int SIZETYPE_MB = 3;//获取文件大小单位为MB的double值
    public static final int SIZETYPE_GB = 4;//获取文件大小单位为GB的double值




    public static String getDeviceInfo(Context context) {
        try {
            org.json.JSONObject json = new org.json.JSONObject();
            android.telephony.TelephonyManager tm = (android.telephony.TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);
            String device_id = null;
            if (checkPermission(context, Manifest.permission.READ_PHONE_STATE)) {
                device_id = tm.getDeviceId();
            }
            String mac = getMac(context);

            json.put("mac", mac);
            if (TextUtils.isEmpty(device_id)) {
                device_id = mac;
            }
            if (TextUtils.isEmpty(device_id)) {
                device_id = android.provider.Settings.Secure.getString(context.getContentResolver(),
                        android.provider.Settings.Secure.ANDROID_ID);
            }
            json.put("device_id", device_id);
            return json.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getMac(Context context) {
        String mac = "";
        if (context == null) {
            return mac;
        }
        if (Build.VERSION.SDK_INT < 23) {
            mac = getMacBySystemInterface(context);
        } else {
            mac = getMacByJavaAPI();
            if (TextUtils.isEmpty(mac)){
                mac = getMacBySystemInterface(context);
            }
        }
        return mac;

    }

    @TargetApi(9)
    private static String getMacByJavaAPI() {
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface netInterface = interfaces.nextElement();
                if ("wlan0".equals(netInterface.getName()) || "eth0".equals(netInterface.getName())) {
                    byte[] addr = netInterface.getHardwareAddress();
                    if (addr == null || addr.length == 0) {
                        return null;
                    }
                    StringBuilder buf = new StringBuilder();
                    for (byte b : addr) {
                        buf.append(String.format("%02X:", b));
                    }
                    if (buf.length() > 0) {
                        buf.deleteCharAt(buf.length() - 1);
                    }
                    return buf.toString().toLowerCase(Locale.getDefault());
                }
            }
        } catch (Throwable e) {
        }
        return null;
    }

    private static String getMacBySystemInterface(Context context) {
        if (context == null) {
            return "";
        }
        try {
            WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            if (checkPermission(context, Manifest.permission.ACCESS_WIFI_STATE)) {
                WifiInfo info = wifi.getConnectionInfo();
                return info.getMacAddress();
            } else {
                return "";
            }
        } catch (Throwable e) {
            return "";
        }
    }

    public static boolean checkPermission(Context context, String permission) {
        boolean result = false;
        if (context == null) {
            return result;
        }
        if (Build.VERSION.SDK_INT >= 23) {
            try {
                Class<?> clazz = Class.forName("android.content.Context");
                Method method = clazz.getMethod("checkSelfPermission", String.class);
                int rest = (Integer) method.invoke(context, permission);
                if (rest == PackageManager.PERMISSION_GRANTED) {
                    result = true;
                } else {
                    result = false;
                }
            } catch (Throwable e) {
                result = false;
            }
        } else {
            PackageManager pm = context.getPackageManager();
            if (pm.checkPermission(permission, context.getPackageName()) == PackageManager.PERMISSION_GRANTED) {
                result = true;
            }
        }
        return result;
    }






    /**
     * 可用空间验证是否足够
     */
    public static boolean availableSpaceVerification(long fileSize){
        long memorySize=getAvailableInternalMemorySize();
        if(memorySize>fileSize){
            return true;
        }else{
            return false;
        }
    }

    /**
     * 实现文本复制功能
     * add by wangqianzhou
     * @param content
     */
    public static boolean copy(String content, Context context)
    {
        boolean isBoo=true;
        try {
            // 得到剪贴板管理器
            ClipboardManager cmb = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            cmb.setText(content.trim());
        }catch (Exception e){
            isBoo=false;
            e.printStackTrace();

        }
        //Toast.makeText(context,"复制成功",Toast.LENGTH_SHORT).show();
        return isBoo;
    }

    /**
     * 截屏
     * @param  activity
     * @return
     */
    public static void captureScreen(Activity activity) {
        try {
            String rootPath=activity.getExternalCacheDir()+"/share/";
            File f=new File(rootPath);
            if(!f.exists()) {
                f.mkdirs();
            }
            String pathStr=activity.getExternalCacheDir()+"/share/" + (DateUtils.getDate(DateUtils.M_D_H_M)) + ".jpg";
            activity.getWindow().getDecorView().setDrawingCacheEnabled(true);
            Bitmap bmp = activity.getWindow().getDecorView().getDrawingCache();
            saveFile(bmp, pathStr, activity);
        }catch (Exception e){}
    }
    /**
     * 保存Bitmap图片为本地文件
     */
    private static void saveFile(Bitmap bitmap, String filename,Activity activity) {
        FileOutputStream fileOutputStream = null;
        try {
            File file=new File(filename);
            if(!file.exists()){
                file.createNewFile();
            }
            fileOutputStream = new FileOutputStream(file);
            if (fileOutputStream != null) {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 80, fileOutputStream);
                fileOutputStream.flush();
                fileOutputStream.close();
            }

            try {
                //播放到相册
                MediaStore.Images.Media.insertImage(activity.getContentResolver(), file.getAbsolutePath(), file.getName(), null);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            // 最后通知图库更新
            Uri contentUri;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                contentUri = FileProvider.getUriForFile(activity, "com.xcore.provider", file);
            } else {
                contentUri = Uri.parse("file://" + file.getAbsolutePath());
            }
            activity.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, contentUri));
            Toast.makeText(activity, "图片已保存到相册,快去分享吧!!!", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(activity,"保存失败",Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    /**
     * 得到版本 code
     * @param context
     * @return
     */
    public static int getVersionCode(Context context){
        int localVersion = 0;
        try {
            PackageInfo packageInfo = context.getApplicationContext()
                    .getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            localVersion = packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return localVersion;
    }

    /**
     * 得到版本name
     * @param context
     * @return
     */
    public static String getVersion(Context context){
        int localVersion = 0;
        String versionName="";
        try {
            PackageInfo packageInfo = context.getApplicationContext()
                    .getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            localVersion = packageInfo.versionCode;
            versionName=packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionName;
    }

    //手机内存可用空间
    public static long getAvailableInternalMemorySize(){
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSizeLong();
        long availableBlocks = stat.getAvailableBlocksLong();
        return availableBlocks*blockSize;
    }

    public static long getTotalInternalMemorySize(){
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSizeLong();
        long totalBlocks = stat.getBlockCountLong();
        return totalBlocks*blockSize;
    }
    //
    public static boolean externalMemoryAvailable(){
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    public static long getAvailableExternalMemorySize(){
        if(externalMemoryAvailable()){
            File path = Environment.getExternalStorageDirectory();
            StatFs stat = new StatFs(path.getPath());
            long blockSize = stat.getBlockSizeLong();
            long availableBlocks = stat.getAvailableBlocksLong();
            return availableBlocks*blockSize;
        }
        else{
            return -1;
        }
    }

    public static long getTotalExternalMemorySize(){
        if(externalMemoryAvailable()){
            File path = Environment.getExternalStorageDirectory();
            StatFs stat = new StatFs(path.getPath());
            long blockSize = stat.getBlockSizeLong();
            long totalBlocks = stat.getBlockCountLong();
            return totalBlocks*blockSize;
        }
        else{
            return -1;
        }
    }

    //循环删除文件夹中的文件
    public static boolean deleteDirWihtFile(File dir) {
        boolean b=false;
        try {
            if (dir == null || !dir.exists() || !dir.isDirectory())
                return false;
            for (File file : dir.listFiles()) {
                if (file.isFile())
                    file.delete(); // 删除所有文件
                else if (file.isDirectory())
                    deleteDirWihtFile(file); // 递规的方式删除文件夹
            }
            b=dir.delete();// 删除目录本身
        }catch (Exception e){

        }
        return b;
    }

    //判断空间是否足够
    public static boolean isEnough(long size){
        long tSize=getAvailableExternalMemorySize();
        return tSize>size;
    }


    /**
     * 获取指定文件或指定文件夹的的指定单位的大小
     * @param filePath 文件路径
     * @param sizeType 获取大小的类型1为B、2为KB、3为MB、4为GB
     * @return double值的大小
     */
    public static double getFolderOrFileSize(String filePath,int sizeType){
        File file=new File(filePath);
        long blockSize=0;
        try {
            if(file.isDirectory()){
                blockSize = getFolderSize(file);
            }else{
                blockSize = getFileSize(file);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("获取文件大小","获取失败!");
        }
        return FormetFileSize(blockSize, sizeType);
    }
    /**
     * 调用此方法自动计算指定文件或指定文件夹的大小
     * @param filePath 文件路径
     * @return 计算好的带B、KB、MB、GB的字符串
     */
    public static String getAutoFolderOrFileSize(String filePath){
        File file=new File(filePath);
        long blockSize=0;
        try {
            if(file.isDirectory()){
                blockSize = getFolderSize(file);
            }else{
                blockSize = getFileSize(file);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("获取文件大小","获取失败!");
        }
        return FormetFileSize(blockSize);
    }
    /**
     * 获取指定文件的大小
     * @param file
     * @return
     * @throws Exception
     */
    private static long getFileSize(File file) throws Exception
    {
        long size = 0;
        if (file.exists()){
            FileInputStream fis = null;
            fis = new FileInputStream(file);
            size = fis.available();
            fis.close();
        }
        else{
            file.createNewFile();
            Log.e("获取文件大小","文件不存在!");
        }

        return size;
    }

    /**
     * 获取指定文件夹的大小
     * @param file
     * @return
     * @throws Exception
     */
    public static long getFolderSize(File file) throws Exception
    {
        long size = 0;
        File flist[] = file.listFiles();
        for (int i = 0; i < flist.length; i++){
            if (flist[i].isDirectory()){
                size = size + getFolderSize(flist[i]);
            }
            else{
                size =size + getFileSize(flist[i]);
            }
        }
        return size;
    }
    /**
     * 转换文件大小
     * @param fileSize
     * @return
     */
    public static String FormetFileSize(long fileSize)
    {
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        String wrongSize="0B";
        if(fileSize==0){
            return wrongSize;
        }
        if (fileSize < 1024){
            fileSizeString = df.format((double) fileSize) + "B";
        }
        else if (fileSize < 1048576){
            fileSizeString = df.format((double) fileSize / 1024) + "KB";
        }
        else if (fileSize < 1073741824){
            fileSizeString = df.format((double) fileSize / 1048576) + "MB";
        }
        else{
            fileSizeString = df.format((double) fileSize / 1073741824) + "GB";
        }
        return fileSizeString;
    }
    /**
     * 转换文件大小,指定转换的类型
     * @param fileSize
     * @param sizeType
     * @return
     */
    public static double FormetFileSize(long fileSize,int sizeType)
    {
        DecimalFormat df = new DecimalFormat("#.00");
        double fileSizeLong = 0;
        switch (sizeType) {
            case SIZETYPE_B:
                fileSizeLong=Double.valueOf(df.format((double) fileSize));
                break;
            case SIZETYPE_KB:
                fileSizeLong=Double.valueOf(df.format((double) fileSize / 1024));
                break;
            case SIZETYPE_MB:
                fileSizeLong=Double.valueOf(df.format((double) fileSize / 1048576));
                break;
            case SIZETYPE_GB:
                fileSizeLong=Double.valueOf(df.format((double) fileSize / 1073741824));
                break;
            default:
                break;
        }
        return fileSizeLong;
    }


    ////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////// 获取手机信息 ////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////

    /**
     * 编码
     * @param text
     * @return
     */
    public static String encode(String text) {
        try {
            //获取md5加密对象
            MessageDigest instance = null;
            try {
                instance = MessageDigest.getInstance("MD5");
                //对字符串加密，返回字节数组
                byte[] digest = instance.digest(text.getBytes());
                StringBuffer sb = new StringBuffer();
                for (byte b : digest) {
                    //获取低八位有效值
                    int i = b & 0xff;
                    //将整数转化为16进制
                    String hexString = Integer.toHexString(i);
                    if (hexString.length() < 2) {
                        //如果是一位的话，补0
                        hexString = "0" + hexString;
                    }
                    sb.append(hexString);
                }
                return sb.toString();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "";
        }catch (Exception e){
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 获取当前手机系统语言。
     *
     * @return 返回当前系统语言。例如：当前设置的是“中文-中国”，则返回“zh-CN”
     */
    public static String getSystemLanguage() {
        return Locale.getDefault().getLanguage();
    }

    /**
     * 获取当前手机系统版本号
     *
     * @return 系统版本号
     */
    public static String getSystemVersion() {
        return android.os.Build.VERSION.RELEASE;
    }

    /**
     * 获取手机型号
     *
     * @return 手机型号
     */
    public static String getSystemModel() {
        return android.os.Build.MODEL;
    }

    /**
     * 获取手机厂商
     *
     * @return 手机厂商
     */
    public static String getDeviceBrand() {
        return android.os.Build.BRAND;
    }

    /**
     * 生产商家
     *
     * @return
     */
    public static String getManufacturer() {
        return android.os.Build.MANUFACTURER;
    }

    /**
     * 获得固件版本
     *
     * @return
     */
    public static String getRelease() {
        return android.os.Build.VERSION.RELEASE;
    }

    /**
     * 获得手机品牌
     *
     * @return
     */
    public static String getBrand() {
        return android.os.Build.BRAND;
    }

    //设备驱动名称
    public static String getDevice() {
        return android.os.Build.DEVICE;
    }

    //设备唯一标识符
    public static String getFingerprint() {
        String fingerprint=android.os.Build.FINGERPRINT;
        String id=android.os.Build.ID;
        String mimeStr="";
        try {
            mimeStr = getM(MainApplicationContext.context);
        }catch (Exception e){}
        String onlyId=fingerprint+"-"+id+"-"+mimeStr;
//        return encode(android.os.Build.FINGERPRINT)
        return encode(onlyId);
    }
    //获取手机mime
    public static String getM(Context context) {
        try {
            TelephonyManager tm = (TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) !=
                    PackageManager.PERMISSION_GRANTED) {
                return "1";
            }
            String deviceId = tm.getDeviceId();
            return deviceId;
        }catch (Exception ex){}
        return "";
    }


    //设备类型
    public static String getType() {
        return android.os.Build.TYPE;
    }

    //设备主机地址
    public static String getHost() {
        return android.os.Build.HOST;
    }

    /**
     * 屏幕分辨率
     *
     * @param context
     * @return
     */
    public static float getDip(Context context) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1f,
                context.getResources().getDisplayMetrics());
    }

}
