package cn.treeh.ToNX.util;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Created by Tobin on 2017/8/4.
 */
public final class JsonUtil {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    public static <T> String toJson(T obj) {
        String json;
        try {
            json = OBJECT_MAPPER.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return json;
    }
    public static <T> T fromJson(String json ,Class<T> type){
        T pojo;
        try{
            pojo = OBJECT_MAPPER.readValue(json,type);
        }catch (Exception e){
            throw new RuntimeException(e);
        }
        return pojo;
    }
}

