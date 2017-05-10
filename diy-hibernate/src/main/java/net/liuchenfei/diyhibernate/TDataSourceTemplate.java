package net.liuchenfei.diyhibernate;

import javafx.scene.control.Tab;
import org.dom4j.DocumentException;

import java.beans.PropertyVetoException;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * Created by liuchenfei on 2017/5/9.
 */
public class TDataSourceTemplate {

    static {
        try {
            HConnection.initConnection(HConnection.class.getClassLoader().getResource("test.xml").getPath());
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (PropertyVetoException e) {
            e.printStackTrace();
        }
    }

    /**
     * 保存一个实体到数据库
     * @param obj 需要保存的实体
     */
    public void save(Object obj) throws SQLException, NoSuchFieldException, IllegalAccessException {
        Connection c=HConnection.getConnection();
        //获取这个类映射的表名，以及各个字段的名称
        StringBuffer sb1=new StringBuffer();
        StringBuffer sb=new StringBuffer();
        StringBuffer fl=new StringBuffer();
        Map<String,String> fs=getFieldList(obj);
        String tableName=getTableName(obj);
        sb1.append("insert into ");
        sb1.append(tableName);
        sb.append("(");
        fl.append("values(");
        for(String name:fs.keySet()){
            sb.append(name);
            sb.append(",");
            fl.append("?,");
        }
        String sql=sb1.toString()+sb.substring(0,sb.length()-1)+") "+fl.substring(0,fl.length()-1)+")";
        PreparedStatement pst=c.prepareStatement(sql);
        System.out.println("sql"+sql);
        int i=0;
        for(String name:fs.keySet()){
            //// TODO: 2017/5/9 这里应该用别的方法，判断不同的类型的数据来处理
            pst.setString(++i,fs.get(name));
        }
        pst.execute();
        pst.close();
        c.close();
    }

    /**
     * 更新一个实体
     * @param obj
     * @return 更新后的实体
     */
    public Object update(Object obj) throws Exception {
        Connection c=HConnection.getConnection();
        //获取这个类映射的表名，以及各个字段的名称
        StringBuffer sb=new StringBuffer();
        Map<String,String> fs=getFieldList(obj);
        ArrayList<String> id=getId(obj);

        StringBuffer whereClause=new StringBuffer(" where ");
        whereClause.append(id.get(0));
        whereClause.append(" =?");
        String tableName=getTableName(obj);
        sb.append("update ");
        sb.append(tableName);
        sb.append(" set ");
        for(String name:fs.keySet()){
            sb.append(name);
            sb.append("=?,");
        }
        String sql=sb.substring(0,sb.length()-1).concat(whereClause.toString());

        PreparedStatement pst=c.prepareStatement(sql);
        System.out.println("executing sql:"+sql);
        int i=0;
        for(String name:fs.keySet()){
            //// TODO: 2017/5/9 这里应该用别的方法，判断不同的类型的数据来处理
            pst.setString(++i,fs.get(name));
        }
        pst.setString(++i,id.get(1));
        pst.execute();
        pst.close();
        c.close();
        return null;
    }

    /**
     * 删除一个实体
     * @param object
     */
    public void delete(Object object) throws Exception {
        ArrayList<String> id=getId(object);
        if(query(id.get(1),object.getClass())==null){
            throw new Exception("数据库中没有这个对象");
        }
        Connection c=HConnection.getConnection();
        StringBuffer sb=new StringBuffer("DELETE FROM ");
        String tableName = getTableName(object);
        sb.append(tableName);
        sb.append(" WHERE ");
        sb.append(id.get(0));
        sb.append("=?");
        String sql = sb.toString();
        PreparedStatement pst=c.prepareStatement(sql);
        pst.setString(1,id.get(1));
        pst.execute();
        pst.close();
        c.close();
    }

    /**
     * 查找一个对象
     * @param id
     * @param <T>
     * @return
     */
    public <T> T query(String id, Class<T> clazz) throws Exception {
        T t=clazz.newInstance();
        String idfield="";
        Connection c=HConnection.getConnection();
        StringBuffer sb=new StringBuffer("select ");
        Field[] fields=clazz.getDeclaredFields();
        for(Field field:fields){
            if(field.isAnnotationPresent(Table.class)){
                field.setAccessible(true);
                String columnName=field.getAnnotation(Table.class).columnName();
                if(field.getAnnotation(Table.class).isId()){
                    idfield=columnName;
                }
                sb.append(columnName);
                sb.append(",");
            }
        }
        sb.deleteCharAt(sb.length()-1);
        sb.append(" from ");
        sb.append(clazz.getAnnotation(Table.class).tableName());
        sb.append(" where ");
        sb.append(idfield);
        sb.append("=?");
        String sql=sb.toString();
        System.out.println("executing sql:"+sql);
        PreparedStatement pst=c.prepareStatement(sql);
        pst.setString(1,id);
        ResultSet rs=pst.executeQuery();
        if(rs.next()){
            int index=0;
            for(Field field:fields){
                field.set(t,rs.getString(++index));
            }
        }else{
            t=null;
        }
        return t;
    }

    /**
     * 获取类注释的表名
     * @param object
     * @return
     * @throws SQLException
     */
    private String getTableName(Object object) throws SQLException {
        Table t=object.getClass().getAnnotation(Table.class);
        if(t!=null){
            return t.tableName();
        }
        else{
            throw new SQLException("这个类注解不正确");
        }
    }

    /**
     * 提取对象中的字段对应的表字段名和对应的值
     * @param object 被提取的对象
     * @return
     * @throws SQLException
     * @throws IllegalAccessException
     * @throws NoSuchFieldException
     */
    private HashMap<String,String> getFieldList(Object object) throws SQLException, IllegalAccessException, NoSuchFieldException {
        HashMap<String,String> result=new HashMap<String, String>();
        Table t=object.getClass().getAnnotation(Table.class);
        if(t!=null){
            Field[] fields=object.getClass().getDeclaredFields();
            for(Field field:fields){
                if(field.isAnnotationPresent(Table.class)){
                    field.setAccessible(true);
                    result.put(field.getAnnotation(Table.class).columnName(),field.get(object).toString());
                }
            }
            return result;
        }
        else{
            throw new SQLException("这个类注解不正确");
        }
    }

    private ArrayList<String> getId(Object obj) throws Exception {
        ArrayList<String> list=new ArrayList<String>();
        if(obj.getClass().isAnnotationPresent(Table.class)){
            Field[] fields=obj.getClass().getDeclaredFields();
            for(Field f:fields){
                f.setAccessible(true);
                Table t=f.getAnnotation(Table.class);
                if(t.isId()){
                    list.add(t.columnName());
                    list.add(f.get(obj).toString());
                    return list;
                }
            }
        }else{
            throw new SQLException("这个类注解不正确");
        }
        if(list.size()==0){
            throw new Exception("不能更新没有指定id的表");
        }
        return  list;
    }

}
