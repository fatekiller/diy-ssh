import com.mchange.v2.c3p0.ComboPooledDataSource;
import net.liuchenfei.diyhibernate.HConnection;
import net.liuchenfei.diyhibernate.TDataSourceTemplate;
import net.liuchenfei.diyhibernate.Table;
import org.dom4j.DocumentException;
import org.junit.Test;

import java.beans.PropertyVetoException;
import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.UUID;

/**
 * Created by liuchenfei on 2017/5/9.
 */
public class TestCase {
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
        User u=new User();
        u.setUserid(UUID.randomUUID().toString());
        u.setUsername("username");
        Field f=u.getClass().getDeclaredField("username");
        f.setAccessible(true);
        String s=f.get(u).toString();
        System.out.println(s);
    }

    @Test
    public void testSave(){
        User u=new User();
        u.setUserid(UUID.randomUUID().toString());
        u.setUsername("username");
        try {
            new MyTemplate().save(u);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

    }

    class MyTemplate extends TDataSourceTemplate{

    }
}
