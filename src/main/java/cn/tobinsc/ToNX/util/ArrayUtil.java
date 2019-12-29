package cn.tobinsc.ToNX.util;

import org.apache.commons.lang3.ArrayUtils;

/**
 * Created by Tobin on 2017/8/4.
 */
public class ArrayUtil {
    public static boolean isEmpty(Object[] list){
        return ArrayUtils.isEmpty(list);
    }
    public static boolean isNotEmpty(Object[] list){
        return !ArrayUtils.isEmpty(list);
    }
}
