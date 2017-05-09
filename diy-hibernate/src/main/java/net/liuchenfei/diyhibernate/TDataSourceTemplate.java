package net.liuchenfei.diyhibernate;

import javafx.scene.control.Tab;
import org.dom4j.DocumentException;

import java.beans.PropertyVetoException;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;

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
     * @param object
     * @return 更新后的实体
     */
    public Object update(Object object){
        return null;
    }

    /**
     * 删除一个实体
     * @param object
     * @return 被删除的实体
     */
    public Object delete(Object object){
        return null;
    }

    /**
     * 按照id查找一个对象
     * @param id
     * @param <T>
     * @return
     */
    public <T> T query(String id){
        T t=null;
        return t;
    }

    private String getTableName(Object object) throws SQLException {
        Table t=object.getClass().getAnnotation(Table.class);
        if(t!=null){
            return t.tableName();
        }
        else{
            throw new SQLException("这个类注解不正确");
        }
    }

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

}
