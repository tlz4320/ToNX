module ToNX {
    requires java.sql;
    requires java.desktop;
    requires org.apache.commons.lang3;
    requires commons.cli;
    requires java.base;
    requires java.management;
    requires com.google.common;
    requires org.slf4j;
    exports cn.treeh.ToNX;
    exports cn.treeh.ToNX.ClassLoader;
    exports cn.treeh.ToNX.util;
    exports cn.treeh.ToNX.Handler;
    exports cn.treeh.ToNX.Annotation;
    exports cn.treeh.ToNX.Exception;
    exports cn.treeh.ToNX.Iterable;
    exports cn.treeh.ToNX.Iterator;
    exports cn.treeh.ToNX.ClassLoader.TURL;
}