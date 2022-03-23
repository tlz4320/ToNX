package cn.treeh.ToNX.util;

import cn.treeh.ToNX.ClassLoader.TURL.MemoryJarURL;
import cn.treeh.ToNX.ClassLoader.TClassLoader;
import cn.treeh.ToNX.Handler.JarDecry;
import cn.treeh.ToNX.O;

import java.io.*;
import java.lang.reflect.Method;
import java.net.URL;

public class JarRunUtil {
    public static void run(String jarFile, String mainClass, String[] argv){
        run(jarFile, mainClass, argv, new JarDecry() {
            @Override
            public byte[] decode(byte[] data, String key) {
                return data;
            }
        }, "");
    }
    public static void run(String jarFile, String mainClass, String[] argv, JarDecry decry, String key){
        TClassLoader util = new TClassLoader(new URL[]{});
        try{
            InputStream reader = new FileInputStream(jarFile);
            File jar = new File(jarFile);
            byte[] jarData = new byte[(int)jar.length()];
            if(reader.read(jarData) == jar.length())
                O.ptln("Read Jar file Data Finished");
            else{
                throw new RuntimeException("Jar File Error");
            }
            jarData = decry.decode(jarData, key);
            URL jarURL = MemoryJarURL.getInstance().build("/" + jar.getName() + "/", jarData);
            util.addURL(jarURL);
            Class<?> cls = util.loadClass(mainClass);
            Method mtd = cls.getDeclaredMethod("main", String[].class);
            mtd.invoke(cls.getDeclaredConstructor().newInstance(), new Object[]{argv});
            util.close();
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}
