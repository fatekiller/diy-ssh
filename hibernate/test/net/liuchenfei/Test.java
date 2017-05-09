package net.liuchenfei;

import net.liuchenfei.datasource.XmlParser;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by liuchenfei on 2017/5/9.
 */
public class Test {
    public static void main(String[] args) {
        try {
            XmlParser.parse("test.xml");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
