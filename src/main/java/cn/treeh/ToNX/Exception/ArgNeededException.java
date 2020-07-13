package cn.treeh.ToNX.Exception;

import cn.treeh.ToNX.Annotation.Arg;
import cn.treeh.ToNX.O;

import java.lang.reflect.Field;

public class ArgNeededException extends Exception {
    Arg annotation;
    Field field;

    public ArgNeededException(Arg annotation, Field field) {
        super();
        this.annotation = annotation;
        this.field = field;

    }

    @Override
    public void printStackTrace() {
        String typeName = field.getType().getTypeName();
        System.err.println(annotation.arg() + " is needed. type: " +
                typeName.substring(typeName.lastIndexOf('.') + 1));
    }

}
