package cn.treeh.ToNX.Annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Arg {
    String arg();
    int level() default 0;
    boolean needed() default false;
    boolean hasArg();
    String longarg() default "";
    String val() default "";
    String description() default "";
}
