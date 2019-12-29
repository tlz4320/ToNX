package cn.tobinsc.ToNX.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by Tobin on 2017/8/4.
 */
public final class ReflectionUtil {
    public static Object newInstance(Class<?> cls){
        Object instance;
        try{
            instance = cls.newInstance();
        }catch(Exception e){
            throw new RuntimeException(e);
        }
        return instance;
    }
    public static Object invokeMethod(Object obj, Method method, Object...args){
        Object result;
        try{
            method.setAccessible(true);
            result = method.invoke(obj,args);
        }catch (Exception e){
            throw new RuntimeException(e);
        }
        return result;
    }
    // only support int double float
    public static Object cast(Class<?> cls, String val) {
        if(val == null)
            val = "";
        String type = cls.getTypeName();
        if (type.equals("java.lang.String"))
            return val;
        if (val.length() == 0)
            val = "0";
        if (type.equals("java.lang.Integer") || type.equals("int"))
            return Integer.parseInt(val);
        if (type.equals("java.lang.Double") || type.equals("double"))
            return Double.parseDouble(val);
        if (type.equals("java.lang.Float") || type.equals("float"))
            return Float.parseFloat(val);
        return null;
    }
    public static void setField(Object obj,Field field,Object value){
        try{
            field.setAccessible(true);
            field.set(obj,value);
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}
