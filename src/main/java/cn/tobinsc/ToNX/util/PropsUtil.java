package cn.tobinsc.ToNX.util;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by Tobin on 2017/8/3.
 */
public class PropsUtil {
    public static Properties loadProps(String filename){
        Properties properties = null;
        InputStream is = null;
        try{
            is = Thread.currentThread().getContextClassLoader().getResourceAsStream(filename);
            if(is==null){
                throw new FileNotFoundException(filename+"not found");
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
