package cn.tobinsc.ToNX.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Tobin on 2017/8/4.
 */
public class StreamUtil {
    public static String getString(InputStream inputStream){
        StringBuilder stringBuilder = new StringBuilder();
        try{
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while((line=reader.readLine())!=null){
                stringBuilder.append(line);
            }
        }catch (Exception e){
            throw new RuntimeException(e);
        }
        return stringBuilder.toString();
    }

}
