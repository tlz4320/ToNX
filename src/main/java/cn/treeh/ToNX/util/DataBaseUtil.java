package cn.treeh.ToNX.util;


import cn.treeh.ToNX.Annotation.DBField;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

import javax.swing.*;

public class DataBaseUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(DataBaseUtil.class);
    private static final String DRIVER;
    private static final String URL;
    private static final String USERNAME;
    private static final String PASSWORD;
    private static final BasicDataSource DATA_SOURCE;
    private static final QueryRunner QUERY_RUNNER = new QueryRunner();
    private static final ThreadLocal<Connection> CONNECTION_HOLDER=new ThreadLocal<Connection>();
    static{
        Properties conf = PropsUtil.loadProps("config.properties");
        DRIVER=conf.getProperty("jdbc.driver");
        URL=conf.getProperty("jdbc.url");
        USERNAME=conf.getProperty("jdbc.username");
        PASSWORD=conf.getProperty("jdbc.password");
        try{
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            LOGGER.error("can not load jdbc driver",e);
        }
        DATA_SOURCE=new BasicDataSource();
        DATA_SOURCE.setDriverClassName(DRIVER);
        DATA_SOURCE.setUrl(URL);
        DATA_SOURCE.setUsername(USERNAME);
        DATA_SOURCE.setPassword(PASSWORD);
    }
    public static void beginTransaction(){
        Connection connection = getConnection();
        if(connection != null){
            try{
                connection.setAutoCommit(false);
            }catch (SQLException e){
                LOGGER.error("begin transaction failure", e);
                throw new RuntimeException(e);
            }finally {
                CONNECTION_HOLDER.set(connection);
            }
        }
    }
    public static void commitTransaciton(){
        Connection connection = getConnection();
        if(connection != null){
            try{
                connection.commit();
                connection.close();
            }catch (SQLException e){
                LOGGER.error("commit transaction failure", e);
                throw new RuntimeException(e);
            }finally {
                CONNECTION_HOLDER.remove();
            }
        }
    }
    public static void rollbackTransaction(){
        Connection connection = getConnection();
        if(connection != null){
            try{
                connection.rollback();
                connection.close();
            }catch (SQLException e){
                LOGGER.error("rollback transaction failure", e);
                throw new RuntimeException(e);
            }finally {
                CONNECTION_HOLDER.remove();
            }
        }
    }
    public static Connection getConnection(){
        Connection connection = CONNECTION_HOLDER.get();
        if(connection==null) {
            try {
                connection = DATA_SOURCE.getConnection();
            } catch (SQLException e) {
                LOGGER.error("get connection failure", e);
                throw  new RuntimeException(e);
            }finally {
                CONNECTION_HOLDER.set(connection);
            }
        }
        return connection;
    }
    public static <T> List<T> queryEntityList(Class<T> entityClass, String sql, Object... params){
        List<T> entityList;
        try{
            Connection connection = getConnection();
            entityList=QUERY_RUNNER.query(connection,sql,new BeanListHandler<T>(entityClass),params);
        }catch (SQLException e){
            LOGGER.error("query entity list failure",e);
            throw new RuntimeException(e);
        }
        return entityList;
    }
    public static <T> T queryEntity(Class<T> entityClass, String sql, Object... params){
        T entity;
        try{
            Connection connection = getConnection();
            entity=QUERY_RUNNER.query(connection,sql,new BeanHandler<T>(entityClass),params);
        }catch (SQLException e){
            LOGGER.error("query entity failure",e);
            throw new RuntimeException(e);
        }
        return entity;
    }
    public static List<Map<String,Object>> executeQuery(String sql, Object... params){
        List<Map<String,Object>> result;
        try{
            Connection connection = getConnection();
            result=QUERY_RUNNER.query(connection,sql,new MapListHandler(),params);
        }catch (SQLException e){
            LOGGER.error("query entity map failure",e);
            throw new RuntimeException(e);
        }
        return result;
    }
    public static int executeUpdate(String sql,Object... params){
        int rows=0;
        try{
            Connection connection = getConnection();
            rows=QUERY_RUNNER.update(connection,sql,params);
        }catch (SQLException e){
            LOGGER.error("execute update failure",e);
            throw new RuntimeException(e);
        }
        return rows;
    }
    public static<T> boolean insertEntity(Class<T> entityClass,Map<String,Object>fieldMap){
        if(CollectionUtil.isEmpty(fieldMap)){
            LOGGER.error("can't insert entity: fieldMap is empty");
            return false;
        }
        String sql="INSERT INTO "+getTableName(entityClass);
        StringBuilder colums = new StringBuilder("(");
        StringBuilder values = new StringBuilder("(");
        for(String fieldName : fieldMap.keySet()){
            colums.append(fieldName).append(", ");
            values.append("?, ");
        }
        colums.replace(colums.lastIndexOf(", "),colums.length(),")");
        values.replace(values.lastIndexOf(", "),values.length(),")");
        sql+=colums+"VALUES"+values;
        Object[] params = fieldMap.values().toArray();
        return executeUpdate(sql,params) == 1;
    }

    public static boolean insertEntity(Object entity) {
        HashMap<String, Object> fieldMap = new HashMap<>();
        Field[] fields = entity.getClass().getFields();
        DBField annotation;
        for (Field field : fields) {
            if (field.isAnnotationPresent(DBField.class)) {
                annotation = field.getAnnotation(DBField.class);
                try {
                    if (annotation.field().equals(""))
                        fieldMap.put(field.getName(), field.get(entity));
                    else
                        fieldMap.put(annotation.field(), field.get(entity));
                } catch (IllegalAccessException e) {
                    JOptionPane.showConfirmDialog(null,
                            "奇怪的事情发生了1",
                            "成功", JOptionPane.DEFAULT_OPTION);
                    throw new RuntimeException("Field should be public");
                }
            }
        }
        return insertEntity(entity.getClass(), fieldMap);
    }
    public static boolean updateEntity(Object entity, long id){
        HashMap<String, Object> fieldMap = new HashMap<>();
        Field[] fields = entity.getClass().getFields();
        DBField annotation;
        for (Field field : fields) {
            if (field.isAnnotationPresent(DBField.class)) {
                annotation = field.getAnnotation(DBField.class);
                try {
                    if (annotation.field().equals(""))
                        fieldMap.put(field.getName(), field.get(entity));
                    else
                        fieldMap.put(annotation.field(), field.get(entity));
                } catch (IllegalAccessException e) {
                    throw new RuntimeException("Field should be public");
                }
            }
        }
        return updateEntity(entity.getClass(), id, fieldMap);
    }
    public static <T> boolean updateEntity(Class<T> entityClass ,long id, Map<String,Object>fieldMap){
        if(CollectionUtil.isEmpty(fieldMap)){
            LOGGER.error("can't update entity: fieldMap is empty");
            return false;
        }
        String sql ="UPDATE "+ getTableName(entityClass)+ " SET ";
        StringBuilder columns = new StringBuilder();
        for(String fieldName : fieldMap.keySet()){
            columns.append(fieldName).append("=?, ");
        }
        sql += columns.substring(0,columns.lastIndexOf(", "))+" WHERE id=?";
        List<Object> paramList = new ArrayList<Object>();
        paramList.addAll(fieldMap.values());
        paramList.add(id);
        Object[] params = paramList.toArray();
        return executeUpdate(sql,params) == 1;
    }
    public static <T> boolean deleteEntity(Class<T> entityClass,long id){
        String sql="DELETE FROM "+getTableName(entityClass)+ " WHERE id=?";
        return executeUpdate(sql,id)==1;
    }
    private static String getTableName(Class<?> entityClass){
        return entityClass.getSimpleName();
    }
    public static void executeSqlFile(String filePath){
        InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(filePath);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String sql;
        try {
            while ((sql = reader.readLine()) != null) {
                executeUpdate(sql);
            }
        }catch (Exception e){
            LOGGER.error("execute sql file failure",e);
            throw  new RuntimeException(e);
        }
    }
}
