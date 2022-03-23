package cn.treeh.ToNX.util;


import cn.treeh.ToNX.E;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;
import java.util.Map;
import java.util.Properties;

/**
 * Created by Tobin on 2017/8/3.
 */
public class PropsUtil {
    public static void saveProps(Properties properties, String filename){
        try {
            FileWriter writer = new FileWriter(filename);
            if(properties == null || properties.size() == 0) {
                writer.flush();
                writer.close();
                return;
            }
            for(Map.Entry<Object, Object> e : properties.entrySet()){
                String value = (String)e.getValue();
                value = value.replaceAll("\\\\", "/");
                writer.write(e.getKey() + " = " + value);
            }
            writer.flush();
            writer.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public static Properties loadProps(String filename){
        Properties properties = null;
        InputStream is = null;
        try{
            is = Thread.currentThread().getContextClassLoader().getResourceAsStream(filename);
            if(is==null){
                E.ptln(filename+" not found");
                return new Properties();
            }
            properties = new Properties();
            properties.load(is);
        } catch (IOException e) {
        }finally {
            if(is!=null){
                try{
                    is.close();
                }catch (IOException e){
                }
            }
        }
        return properties;
    }
    public static String getString(Properties properties,String key){
        return getString(properties,key,"");
    }
    public static String getString(Properties properties,String key,String defaultValue){
        String value=defaultValue;
        if(properties.containsKey(key)){
            value=properties.getProperty(key);
        }
        return value;
    }
    public static int getInt(Properties properties,String key){
        return getInt(properties,key,0);
    }
    public static int getInt(Properties properties,String key,int defaultValue){
        int value = defaultValue;
        if(properties.containsKey(key)){
            value=CastUtil.castInt(properties.getProperty(key));
        }
        return value;
    }
    public static boolean getBoolean(Properties properties,String key){
        return getBoolean(properties,key,false);
    }
    public static boolean getBoolean(Properties properties,String key,Boolean defaultValue){
        boolean value = defaultValue;
        if(properties.containsKey(key)){
            value = CastUtil.castBoolean(properties.getProperty(key));
        }
        return value;
    }

}
