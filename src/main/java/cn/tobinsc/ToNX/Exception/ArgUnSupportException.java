package cn.tobinsc.ToNX.Exception;

import cn.tobinsc.ToNX.O;

public class ArgUnSupportException extends Exception {
    Class<?> cls;
    String val = null;
    public ArgUnSupportException(Class<?> cls) {
        super();
        this.cls = cls;
    }
    public ArgUnSupportException(Class<?> cls, String v){
        super();
        this.cls = cls;
        val = v;
    }
    @Override
    public void printStackTrace() {
        String typeName = cls.getTypeName();
        typeName = typeName.substring(typeName.lastIndexOf('.') + 1);
        if(val == null)
                System.err.println("Arg is not support type: " + typeName);
        else{
            System.err.println("Arg input Error, type: " + typeName + "  value you input: " + val);
        }
    }
}
