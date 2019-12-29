package cn.tobinsc.ToNX;

import java.util.Scanner;

/**
 * Created by Tobin on 2017/7/18.
 */
public class I {
    /**
     * 该方法用于输入一行字符串
     * @return String
     */
    public static String sfln(){
        String res = "";
        Scanner scanner = new Scanner(System.in);
        res = scanner.nextLine();
        return res;
    }

    /**
     * 该方法用于简化int输入
     * @return int
     */
    public static int sfint(){
        int res = 0;
        Scanner scanner = new Scanner(System.in);
        res = scanner.nextInt();
        return res;
    }

    /**
     * 该方法用于简化float输入
     * @return
     */
    public static float sfft(){
        float res = 0;
        Scanner scanner = new Scanner(System.in);
        res = scanner.nextFloat();
        return res;
    }

    /**
     * 该方法用户简化char输入
     * @return
     */
    public static char sfch(){
        try {
            char res = (char) System.in.read();
            return res;
        }catch (Exception e){
            e.printStackTrace();
        }
        return 0;
    }
}
