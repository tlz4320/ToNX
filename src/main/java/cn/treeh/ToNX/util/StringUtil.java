package cn.treeh.ToNX.util;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by Tobin on 2017/8/3.
 */
public class StringUtil {
    public static boolean isEmpty(String str){
        if(str!=null){
            str=str.trim();
        }
        return StringUtils.isEmpty(str);
    }
    public static boolean isNotEmpty(String str){
        return !isEmpty(str);
    }
    public static String[] splitString(String str,String regex){
        return str.split(regex);
    }
    /**
     * [start, end)
     */
    public static String paste(String[] sp, String collapse, int start, int end){
        if(sp.length <= start || start < 0 || end < start || sp.length < end)
            return "";
        StringBuilder builder = new StringBuilder(sp[start]);
        for(int index = start + 1; index < end; index++){
            builder.append(collapse).append(sp[index]);
        }
        return builder.toString();
    }
    public static String paste(String[] sp, String collapse){
        return paste(sp, collapse, 0, sp.length);
    }
    public static String paste(String[] sp, String collapse, int start){
        return paste(sp, collapse, start, sp.length);
    }
}
