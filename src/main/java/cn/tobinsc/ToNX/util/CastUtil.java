package cn.tobinsc.ToNX.util;

/**
 * Created by Tobin on 2017/8/3.
 */
public class CastUtil {
    public static String castString(Object obj){
        return castString(obj,"");
    }
    public static String castString(Object obj,String defaultValue){
        return obj!=null?String.valueOf(obj):defaultValue;
    }
    public static double castDouble(Object obj){
        return castDouble(obj,0);
    }
    public static double castDouble(Object obj,double defaultValue){
        double doubleValue = defaultValue;
        if(obj!=null){
            String str = castString(obj);
            if(StringUtil.isNotEmpty(str)){
                try{
                    doubleValue=Double.parseDouble(str);
                }catch (NumberFormatException e){
                    doubleValue=defaultValue;
                }
            }
        }
        return doubleValue;
    }
    public static long castLong(Object obj){
        return castLong(obj,0);
    }
    public static long castLong(Object obj,long defaultValue){
        long longValue=defaultValue;
        if(obj!=null){
            String str = castString(obj);
            if(StringUtil.isNotEmpty(str)){
                try{
                    longValue=Long.parseLong(str);
                }catch (NumberFormatException e){
                    longValue=defaultValue;
                }
            }
        }
        return longValue;
    }
    public static int castInt(Object obj){
        return castInt(obj,0);
    }
    public static int castInt(Object obj,int defaultValue){
        int intValue=defaultValue;
        if(obj!=null){
            String str = castString(obj);
            if(StringUtil.isNotEmpty(str)){
                try{
                    intValue=Integer.parseInt(str);
                }catch (NumberFormatException e){
                    intValue=defaultValue;
                }
            }
        }
        return intValue;
    }
    public static boolean castBoolean(Object obj){
        return castBoolean(obj,false);
    }
    public static boolean castBoolean(Object obj,boolean defaultValue){
        boolean booleanValue = defaultValue;
        if(obj!=null){
            String str = castString(obj);
            if(StringUtil.isNotEmpty(str)){
                try{
                    booleanValue=Boolean.parseBoolean(str);
                }catch (NumberFormatException e){
                    booleanValue=defaultValue;
                }
            }
        }
        return booleanValue;
    }
}
