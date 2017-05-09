package net.liuchenfei.diyhibernate;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.beans.PropertyVetoException;
import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Iterator;

/**
 * Created by liuchenfei on 2017/5/9.
 */
public class HConnection {
    private static ComboPooledDataSource dataSource=new ComboPooledDataSource();

    public static void initConnection(String file) throws DocumentException, PropertyVetoException {

        SAXReader reader=new SAXReader();
        Document document=reader.read(new File(file));
        Element root=document.getRootElement();

        for(Iterator<Element> i=root.elementIterator();i.hasNext();){
            Element e=i.next();
            if(e.getQName().getName().equals("url")){
                dataSource.setJdbcUrl(e.getText());
            }else if(e.getQName().getName().equals("driver")){
                dataSource.setDriverClass(e.getText());
            }else if(e.getQName().getName().equals("username")){
                dataSource.setUser(e.getText());
            }else if(e.getQName().getName().equals("password")){
                dataSource.setPassword(e.getText());
            }else if(e.getQName().getName().equals("maxConnSize")){
                dataSource.setMaxPoolSize(Integer.parseInt(e.getText()));
            }
        }
    }

    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public static void closeDatasource(){
        dataSource.close();
    }
}
