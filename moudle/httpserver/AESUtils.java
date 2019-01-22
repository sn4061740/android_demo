package com.xcore.httpserver;

import com.xcore.MainApplicationContext;
import com.xcore.utils.LogUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.IllegalBlockSizeException;
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
     * 对文件进行AES加密
     *
     * @param key
     * @param sourceFilePath
     * @param destFilePath
     * @return
     */
    public static File encryptFile(String key, String sourceFilePath, String destFilePath) {
        System.out.printf(sourceFilePath);
        FileInputStream in = null;
        FileOutputStream out = null;
        File destFile = null;
        File sourceFile = null;
        try {
            sourceFile = new File(sourceFilePath);
            destFile = new File(destFilePath);
            if (sourceFile.exists() && sourceFile.isFile()) {
                if (!destFile.getParentFile().exists()) {
                    destFile.getParentFile().mkdirs();
                }
                destFile.createNewFile();
                in = new FileInputStream(sourceFile);
                out = new FileOutputStream(destFile);
                Cipher cipher = initAESCipher(key, Cipher.ENCRYPT_MODE);
                //以加密流写入文件
                CipherInputStream cipherInputStream = new CipherInputStream(in, cipher);
                byte[] cache = new byte[1024];
                int nRead = 0;
                while ((nRead = cipherInputStream.read(cache)) != -1) {
                    out.write(cache, 0, nRead);
                    out.flush();
                }
                cipherInputStream.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } finally {
            try {
                if(out!=null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
            try {
                if(in!=null) {
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
        return destFile;
    }
    public static File encryptFile(String key,String vKey, String sourceFilePath, String destFilePath) {
        System.out.printf(sourceFilePath);
        FileInputStream in = null;
        FileOutputStream out = null;
        File destFile = null;
        File sourceFile = null;
        try {
            sourceFile = new File(sourceFilePath);
            destFile = new File(destFilePath);
            if (sourceFile.exists() && sourceFile.isFile()) {
                if (!destFile.getParentFile().exists()) {
                    destFile.getParentFile().mkdirs();
                }
                destFile.createNewFile();
                in = new FileInputStream(sourceFile);
                out = new FileOutputStream(destFile);
                Cipher cipher = initAESCipher(key,vKey, Cipher.ENCRYPT_MODE);
                //以加密流写入文件
                CipherInputStream cipherInputStream = new CipherInputStream(in, cipher);
                byte[] cache = new byte[1024];
                int nRead = 0;
                while ((nRead = cipherInputStream.read(cache)) != -1) {
                    out.write(cache, 0, nRead);
                    out.flush();
                }
                cipherInputStream.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } finally {
            try {
                if(out!=null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
            try {
                if(in!=null) {
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
        return destFile;
    }
    /**
     * AES方式解密文件
     *
     * @param key
     * @param sourceFilePath
     * @param destFilePath
     * @return
     */
    public static File decryptFile(String key, String sourceFilePath, String destFilePath) {
        FileInputStream in = null;
        FileOutputStream out = null;
        File destFile = null;
        File sourceFile = null;
        try {
            sourceFile = new File(sourceFilePath);
            destFile = new File(destFilePath);
            if (sourceFile.exists() && sourceFile.isFile()) {
                if (!destFile.getParentFile().exists()) {
                    destFile.getParentFile().mkdirs();
                }
                destFile.createNewFile();
                in = new FileInputStream(sourceFile);
                out = new FileOutputStream(destFile);
                Cipher cipher = initAESCipher(key, Cipher.DECRYPT_MODE);
                CipherOutputStream cipherOutputStream = new CipherOutputStream(out, cipher);
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
                in.close();
            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
        return destFile;
    }//3353  2719.68
    public static File decryptFile(String key,String vKey, String sourceFilePath, String destFilePath) {
        FileInputStream in = null;
        FileOutputStream out = null;
        File destFile = null;
        File sourceFile = null;
        try {
            sourceFile = new File(sourceFilePath);
            destFile = new File(destFilePath);
            if (sourceFile.exists() && sourceFile.isFile()) {
                if (!destFile.getParentFile().exists()) {
                    destFile.getParentFile().mkdirs();
                }
                destFile.createNewFile();
                in = new FileInputStream(sourceFile);
                out = new FileOutputStream(destFile);
                Cipher cipher = initAESCipher(key,vKey, Cipher.DECRYPT_MODE);
                CipherOutputStream cipherOutputStream = new CipherOutputStream(out, cipher);
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
                in.close();
            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
        return destFile;
    }
    /**
	 * 加密字符串
	 * 
	 * @param content 需要加密的内容
	 * @param
	 * @return
	 */
	public static byte[] encrypt(String content, String sKey) {
		try {
			// 初始化 加密器
			Cipher cipher = initAESCipher(sKey, Cipher.ENCRYPT_MODE);
			byte[] byteContent = content.getBytes("utf-8");
			byte[] result = cipher.doFinal(byteContent);
			return result; // 加密
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		}
		return null;
	}
 
	/**
	 * 解密字符串
	 * 
	 * @param content 
	 * @param
	 * @return
	 */
	public static byte[] decrypt(byte[] content, String sKey) {
		try {
			// 初始化 加密器
			Cipher cipher = initAESCipher(sKey, Cipher.DECRYPT_MODE);
			byte[] result = cipher.doFinal(content);
			return result; // 加密
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		}
		return null;
	}
    /**
     * AES方式解密文件
     *
     * @param key
     * @param sourceFilePath
     * @param destFilePath
     * @return
     * @throws IllegalBlockSizeException 
     */
    public static File decryptFile1(String key, String sourceFilePath, String destFilePath) {
    	 FileInputStream in = null;
         FileOutputStream out = null;
         File destFile = null;
         File sourceFile = null;
         try {
             sourceFile = new File(sourceFilePath);
             destFile = new File(destFilePath);
             if (sourceFile.exists() && sourceFile.isFile()) {
                 if (!destFile.getParentFile().exists()) {
                     destFile.getParentFile().mkdirs();
                 }
                 destFile.createNewFile();
                 in = new FileInputStream(sourceFile);
                 out = new FileOutputStream(destFile);
                 Cipher cipher = initAESCipher(key, Cipher.DECRYPT_MODE);
                 
                 CipherOutputStream cipherOutputStream = new CipherOutputStream(out, cipher);
                 byte[] buffer = new byte[1024];
                 
//                 byte[] lenbytes=new byte[1];
//                 int len=in.read(lenbytes, 0, 1);
//                 String v2=new String(lenbytes);
//                 int v1=Integer.valueOf(v2);
//                 int v=in.read(buffer,0,v1);
                 
                 int r;
                 while ((r = in.read(buffer)) >= 0) {
                     cipherOutputStream.write(buffer,0, r);
                 }
                 cipherOutputStream.close();
             }
         } catch (IOException e) {
             e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
         } finally {
             try {
                 in.close();
             } catch (IOException e) {
                 e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
             }
             try {
                 out.close();
             } catch (IOException e) {
                 e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
             }
         }
         return destFile;
    }
    
    public static File decryptFile1(String key,String vKey,String sourceFilePath, String destFilePath) {
   	 FileInputStream in = null;
        FileOutputStream out = null;
        File destFile = null;
        File sourceFile = null;
        try {
            sourceFile = new File(sourceFilePath);
            destFile = new File(destFilePath);
            if (sourceFile.exists() && sourceFile.isFile()) {
                if (!destFile.getParentFile().exists()) {
                    destFile.getParentFile().mkdirs();
                }
                destFile.createNewFile();
                in = new FileInputStream(sourceFile);
                out = new FileOutputStream(destFile);
                Cipher cipher = initAESCipher(key,vKey, Cipher.DECRYPT_MODE);
                
                CipherOutputStream cipherOutputStream = new CipherOutputStream(out, cipher);
                byte[] buffer = new byte[1024];
                
//                byte[] lenbytes=new byte[1];
//                int len=in.read(lenbytes, 0, 1);
//                String v2=new String(lenbytes);
//                int v1=Integer.valueOf(v2);
//                int v=in.read(buffer,0,v1);
                
                int r;
                while ((r = in.read(buffer)) >= 0) {
                    cipherOutputStream.write(buffer,0, r);
                }
                cipherOutputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
        return destFile;
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
    //将输入流转为输出流
    public static ByteArrayOutputStream getDecryptOutStream(InputStream in,String key,String vKey,int contentLength) {
        ByteArrayOutputStream out1 = new ByteArrayOutputStream();
        try {
            Cipher cipher = initAESCipher(key, vKey,Cipher.DECRYPT_MODE);
            CipherOutputStream cipherOutputStream = new CipherOutputStream(out1, cipher);
            byte[] buffer = new byte[1024];
            int r;
            long totalReaded = 0;
            long old=0;
            long len = 0;
            long current = 0L;
            long timer = System.currentTimeMillis();
            long speed = 0L;
            while ((r = in.read(buffer)) >= 0) {
                totalReaded += r;
                current += r;
                speed += r;
//                LogUtils.showLog("run: Speed:" + speed);
//                timer=currentTimer;
//                speed = 0L;
                long currentTimer = System.currentTimeMillis();
                if(currentTimer- timer>=1000){
                    LogUtils.showLog("run: Speed:" + (speed*1.0/1024)+"KB/S");
                    timer=currentTimer;
                    speed = 0L;
                }
                cipherOutputStream.write(buffer, 0, r);
            }
            LogUtils.showLog("run: Speed:" + (speed*1.0/1024)+"KB/S");
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
        ByteArrayOutputStream out=getDecryptOutStream(in,key,vKey);
        try {
            InputStream inStream = new ByteArrayInputStream(out.toByteArray());
            return inStream;
        }catch (Exception ex){}
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
            FileOutputStream fileOutputStream=new FileOutputStream(MainApplicationContext.SD_PATH+"2.m3u8");
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
