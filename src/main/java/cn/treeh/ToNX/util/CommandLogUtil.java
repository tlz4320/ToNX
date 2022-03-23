package cn.treeh.ToNX.util;

import cn.treeh.ToNX.O;
import com.google.common.base.Strings;
import java.util.HashMap;

public class CommandLogUtil {
    static String preLog = "";
    static int preCount = 1;
    static String realLog = "";
    static int preLevel = 0;
    static synchronized public void log(String info){
        if(preLog.equals(info)) {
            info = info + "  " + (++preCount);
            O.pt(Strings.repeat("\b", realLog.length()));
        }
        else {
            preLog = info;
            preCount = 1;
            O.ptln("");
        }
        realLog = info;
        O.pt(realLog);
    }
}
