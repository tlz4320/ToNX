module ToNX {
    requires org.apache.commons.collections4;
    requires commons.dbcp2;
    requires commons.dbutils;
    requires java.sql;
    requires org.xerial.sqlitejdbc;
    requires java.desktop;
    requires org.apache.commons.lang3;
    requires commons.cli;
    requires java.base;
    requires com.fasterxml.jackson.databind;
    requires java.management;
    requires com.google.common;
    requires org.slf4j;
    requires org.apache.commons.compress;
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