import com.mchange.v2.c3p0.ComboPooledDataSource;
import net.liuchenfei.diyhibernate.HConnection;
import net.liuchenfei.diyhibernate.TDataSourceTemplate;
import net.liuchenfei.diyhibernate.Table;
import org.dom4j.DocumentException;
import org.junit.Test;

import java.beans.PropertyVetoException;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.UUID;

/**
 * Created by liuchenfei on 2017/5/9.
 */
public class TestCase {
    private User u;

    public TestCase(){
        super();
        this.u=new User();
        u.setUserid(UUID.randomUUID().toString());
        u.setUsername("username");
    }


    @Test
    public void testXmlParse() {
        try {
            String s=HConnection.class.getClassLoader().getResource("test.xml").getPath();
            HConnection.initConnection(s);
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (PropertyVetoException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testConnPool(){
        ComboPooledDataSource ds=new ComboPooledDataSource();
        try {
            ds.setDriverClass("com.mysql.jdbc.Driver");
            ds.setJdbcUrl("jdbc:mysql://localhost:3306/diy?characterEncoding=UTF-8&serverTimezone=UTC");
            ds.setUser("root");
            ds.setPassword("liuchenfei");
            ds.setMaxPoolSize(15);
            Object o=ds.getConnection();
            System.out.println(o);;
        } catch (PropertyVetoException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testAnno(){
        User u=new User();
        u.setUserid(UUID.randomUUID().toString());
        u.setUsername("username");
        String s=getTableName(u);
        System.out.println(s);
    }

    private String getTableName(Object object){
        Table t=object.getClass().getAnnotation(Table.class);
        t.tableName();
        return t.tableName();
    }

    @Test
    public void generalTest() throws NoSuchFieldException, IllegalAccessException {
        StringBuffer sb=new StringBuffer("this is a test");
        System.out.println(sb.substring(0,sb.length()));
    }

    @Test
    public void testSave(){
        User u=new User();
        u.setUserid(UUID.randomUUID().toString());
        u.setUsername("username");
        try {
            new MyTemplate().save(u);
            u.setUsername("test");
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void AccessTest(){
        User u=new User();
        u.setUserid(UUID.randomUUID().toString());
        u.setUsername("username");
        try {
            setAcc(u);
            u.getClass().getField("username");
            System.out.println(u.getClass().getField("username"));
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void updateTest(){
        try {
            MyTemplate t=new MyTemplate();
            t.save(u);
            u.setUsername("test");
            t.update(u);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGeneric(){
        StringBuffer sb=new StringBuffer("as");
        sb.deleteCharAt(sb.length()-1);
        System.out.println(sb.toString());
    }

    @Test
    public void testQuery()
    {
        MyTemplate template=new MyTemplate();
        try {
            template.save(u);
            User nu=template.query(u.getUserid(),User.class);
            System.out.println(u.equals(nu));
            template.delete(nu);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Test
    public void testRs(){
        try {
            ResultSet rs=HConnection.getConnection().prepareStatement("select * from USER").executeQuery();
            System.out.println(rs.getString(0));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private <T> T getT() throws IllegalAccessException, InstantiationException {
        Class<T> entityClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        System.out.println(entityClass);
        return entityClass.newInstance();
    }


    private void setAcc(Object o) throws NoSuchFieldException {
        o.getClass().getDeclaredField("username").setAccessible(true);
    }



    class MyTemplate extends TDataSourceTemplate{

    }
}
