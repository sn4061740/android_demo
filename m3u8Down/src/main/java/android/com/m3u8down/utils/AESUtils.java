package android.com.m3u8down.utils;

import android.com.m3u8down.M3u8Utils;
import android.util.Log;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AESUtils {
    public static final String VIPARA = "WUGuTPZCi8E48v8P";
    public static final String VIPARA1="b24w9AwfAspg3Qfp";
    private static final String TAG = AESUtils.class.getSimpleName();
    /**
     * 初始化 AES Cipher
     *
     * @param sKey
     * @param cipherMode
     * @return
     */
    private static Cipher initAESCipher(String sKey, int cipherMode) {
        //创建Key gen
        KeyGenerator keyGenerator = null;
        Cipher cipher = null;
        try {
            IvParameterSpec zeroIv = new IvParameterSpec(VIPARA.getBytes());
            SecretKeySpec key = new SecretKeySpec(sKey.getBytes(), "AES");
            cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(cipherMode, key, zeroIv);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (InvalidKeyException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (InvalidAlgorithmParameterException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return cipher;
    }
    /**
     * 初始化 AES Cipher
     *
     * @param sKey
     * @param cipherMode
     * @return
     */
    private static Cipher initAESCipher(String sKey,String vKey, int cipherMode) {
        //创建Key gen
        KeyGenerator keyGenerator = null;
        Cipher cipher = null;
        try {
            IvParameterSpec zeroIv = new IvParameterSpec(vKey.getBytes());
            SecretKeySpec key = new SecretKeySpec(sKey.getBytes(), "AES");
            cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");//PKCS5Padding
            cipher.init(cipherMode, key, zeroIv);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (InvalidKeyException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (InvalidAlgorithmParameterException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return cipher;
    }

    public static ByteArrayOutputStream decryptFile(String sourceFilePath) {
        FileInputStream in = null;
        ByteArrayOutputStream out1 = new ByteArrayOutputStream();
        try {
            File sourceFile = new File(sourceFilePath);
            if (sourceFile.exists() && sourceFile.isFile()) {
                in = new FileInputStream(sourceFile);
                byte[] buffer = new byte[1024];
                int r;
                while ((r = in.read(buffer)) >= 0) {
                	out1.write(buffer, 0, r);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } finally {
            try {
                if(in!=null) {
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
        return out1;
    }
    //得到文件输出流
    public static ByteArrayOutputStream decryptFile(String key, String sourceFilePath) {
        FileInputStream in = null;
        ByteArrayOutputStream out1 = new ByteArrayOutputStream();
        try {
            File sourceFile = new File(sourceFilePath);
            if (sourceFile.exists() && sourceFile.isFile()) {
                in = new FileInputStream(sourceFile);
                Cipher cipher = initAESCipher(key, Cipher.DECRYPT_MODE);
                CipherOutputStream cipherOutputStream = new CipherOutputStream(out1, cipher);
                byte[] buffer = new byte[1024];
                int r;
                while ((r = in.read(buffer)) >= 0) {
                    cipherOutputStream.write(buffer, 0, r);
                }
                cipherOutputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } finally {
            try {
                if(in!=null) {
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
        return out1;
    }
    public static ByteArrayOutputStream getDecryptOutStream(String key,String vKey, String sourceFilePath) {
        FileInputStream in = null;
        ByteArrayOutputStream out1 = new ByteArrayOutputStream();
        try {
            File sourceFile = new File(sourceFilePath);
            if (sourceFile.exists() && sourceFile.isFile()) {
                in = new FileInputStream(sourceFile);
                Cipher cipher = initAESCipher(key, vKey,Cipher.DECRYPT_MODE);
                CipherOutputStream cipherOutputStream = new CipherOutputStream(out1, cipher);
                byte[] buffer = new byte[1024];
                int r;
                while ((r = in.read(buffer)) >= 0) {
                    cipherOutputStream.write(buffer, 0, r);
                }
                cipherOutputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } finally {
            try {
                if(in!=null) {
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
        return out1;
    }
    //将输入流转为输出流
    public static ByteArrayOutputStream getDecryptOutStream(InputStream in,String key,String vKey) {
        ByteArrayOutputStream out1 = new ByteArrayOutputStream();
        try {
        	Cipher cipher = initAESCipher(key, vKey,Cipher.DECRYPT_MODE);
            CipherOutputStream cipherOutputStream = new CipherOutputStream(out1, cipher);
            byte[] buffer = new byte[1024];
            int r;
            while ((r = in.read(buffer)) >= 0) {
                cipherOutputStream.write(buffer, 0, r);
            }
            cipherOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } finally {
            try {
                if(in!=null) {
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
        return out1;
    }
    //保存文件到本地
    public static ByteArrayOutputStream getDecryptOutStream(InputStream in,String key,String vKey,String outFilePath) {
        ByteArrayOutputStream out1 = new ByteArrayOutputStream();
//        FileOutputStream outputStream=null;
        try {
//            File file=new File(outFilePath);
//            if(!file.exists()){
//                file.createNewFile();
//            }
//            outputStream=new FileOutputStream(file);

            Cipher cipher = initAESCipher(key, vKey,Cipher.DECRYPT_MODE);
            CipherOutputStream cipherOutputStream = new CipherOutputStream(out1, cipher);
//            CipherOutputStream cipherOutputStream1 = new CipherOutputStream(outputStream, cipher);
            byte[] buffer = new byte[1024];
            int r;
            while ((r = in.read(buffer)) >= 0) {
                cipherOutputStream.write(buffer, 0, r);
//                cipherOutputStream1.write(buffer,0,r);
            }
            cipherOutputStream.close();
//            cipherOutputStream1.close();
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } finally {
            try {
                if(in!=null) {
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
        return out1;
    }

  //得到文件输入流、返回到播放器
    public static InputStream getInputStream(String sourceFilePath){
        ByteArrayOutputStream out=decryptFile(sourceFilePath);
        try {
            InputStream in = new ByteArrayInputStream(out.toByteArray());
            return in;
        }catch (Exception ex){}
        return null;
    }
    //得到文件输入流、返回到播放器
    public static InputStream getInputStream(String key, String sourceFilePath){
        ByteArrayOutputStream out=decryptFile(key,sourceFilePath);
        try {
            InputStream in = new ByteArrayInputStream(out.toByteArray());
            return in;
        }catch (Exception ex){}
        return null;
    }
  //得到文件输入流、返回到播放器
    public static InputStream getInputStream(String key,String vKey, String sourceFilePath){
        ByteArrayOutputStream out=getDecryptOutStream(key,vKey,sourceFilePath);
        try {
            InputStream in = new ByteArrayInputStream(out.toByteArray());
            return in;
        }catch (Exception ex){}
        return null;
    }
  //得到文件输入流、返回到播放器
    public static InputStream getInputStream(InputStream in,String key,String vKey){
        byte[] bytes= readFileBytes(in);
        return desEncrypt(bytes,vKey,key);
    }
    //得到文件输入流、返回到播放器
    public static InputStream getInputStreamV1(InputStream in,String v1,String url){
        byte[] bytes= readFileBytes(in);
        return desEncryptV1(bytes,v1.substring(16),v1.substring(0,16),url);
    }
    //得到文件输入流、返回到播放器
    public static InputStream getInputStreamV2(InputStream in,String v1,String url,final File file){
        final byte[] bytes= readFileBytes(in);
        //保存
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(bytes);
        }catch (Exception ex){}
        return desEncryptV1(bytes,v1.substring(16),v1.substring(0,16),url);
    }
    public static InputStream desEncryptV1(byte[] bytes,String iv,String key,String md5) {
        try
        {
            if(bytes==null||bytes.length<=0){
                return null;
            }
            Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
            SecretKeySpec keyspec = new SecretKeySpec(key.getBytes(), "AES");
            IvParameterSpec ivspec = new IvParameterSpec(iv.getBytes());
            cipher.init(Cipher.DECRYPT_MODE, keyspec, ivspec);
            //url 请求的原始url
            byte[] original = cipher.doFinal(bytes);

            //解密
            BufferedReader reader=new BufferedReader(new StringReader(new String(original)));
            String line="";
            StringBuffer buffer=new StringBuffer();
            int port=M3u8Utils.getInstance().getPort();
            while ((line = reader.readLine()) != null) {
                if(line.contains("KEY")){//是KEY 的一行 替换成本地的key地址
                    //拆分
                    int uriIndex=line.indexOf("URI=");
                    int vIndex=line.indexOf(",IV=");
                    String startLine=line.substring(0,uriIndex+"URI=".length()+1);
                    String xV=line.substring(uriIndex+"URI=".length()+1,vIndex-1);
                    String eV=line.substring(vIndex-1);
                    xV=startLine+"http://127.0.0.1:"+port +"/play?v1="+md5+"&uri={"+xV+"}"+eV;
                    buffer.append(xV+"\n");
                }else if(line.contains(".ts")){
                    String tsUrl="http://127.0.0.1:"+port +"/play?v1="+md5+"&uri={"+line+"}";
                    buffer.append(tsUrl+"\r\n");
                }else{
                    buffer.append(line+"\r\n");
                }
            }
            String originalString=buffer.toString().trim();
            Log.e("TAG",originalString);

            //转成输入流来替换路径
//            originalString = new String(original);
//            originalString=originalString.trim();
//            Log.e("TAG",originalString);
            return new ByteArrayInputStream(originalString.getBytes());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static InputStream getInputStream(InputStream in,String key,String vKey,File outPath){
        byte[] bytes= readFileBytes(in);
        try {
            FileOutputStream fileOutputStream=new FileOutputStream(outPath);
            fileOutputStream.write(bytes);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return desEncrypt(bytes,vKey,key);
    }
    private static byte[] readFileBytes(InputStream inStream){
        ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
        try {
            byte[] buffer = new byte[1024];
            int r;
            while ((r = inStream.read(buffer)) >= 0) {
                swapStream.write(buffer, 0, r);
            }
        }catch (Exception ex){}
        byte[] in2b = swapStream.toByteArray();
        return in2b;
    }
    public static InputStream desEncrypt(byte[] bytes,String iv,String key) {
        try
        {
            if(bytes==null||bytes.length<=0){
                return null;
            }
            Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
            SecretKeySpec keyspec = new SecretKeySpec(key.getBytes(), "AES");
            IvParameterSpec ivspec = new IvParameterSpec(iv.getBytes());
            cipher.init(Cipher.DECRYPT_MODE, keyspec, ivspec);

            byte[] original = cipher.doFinal(bytes);
            String originalString = new String(original);
            originalString=originalString.trim();
//            Log.e("TAG",originalString);
            return new ByteArrayInputStream(originalString.getBytes());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    //得到文件输入流、返回到播放器
    public static InputStream getInputStream(InputStream in,String key,String vKey,int contentLeng){
        ByteArrayOutputStream out=getDecryptOutStream(in,key,vKey);

        try {
            InputStream inStream = new ByteArrayInputStream(out.toByteArray());
            return inStream;
        }catch (Exception ex){}
        return null;
    }
    //返回输入流，保存文件解密文件到本地
    public static InputStream getInputStream(InputStream in,String key,String vKey,String outFilePath){
        ByteArrayOutputStream out=getDecryptOutStream(in,key,vKey,outFilePath);
        try{
            FileOutputStream fileOutputStream=null;////new FileOutputStream(MainApplicationContext.SD_PATH+"2.m3u8");
            fileOutputStream.write(out.toByteArray());
            fileOutputStream.close();
        }catch (Exception ex){}
        try {
            InputStream inStream = new ByteArrayInputStream(out.toByteArray());
            return inStream;
        }catch (Exception ex){}
        return null;
    }
}
