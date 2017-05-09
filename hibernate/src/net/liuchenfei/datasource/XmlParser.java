package net.liuchenfei.datasource;

import com.sun.xml.internal.messaging.saaj.util.ByteInputStream;
import org.xml.sax.Attributes;
import org.xml.sax.HandlerBase;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.ws.handler.Handler;
import java.beans.XMLDecoder;
import java.io.*;
import java.util.HashMap;

/**
 * Created by liuchenfei on 2017/4/12.
 */
public class XmlParser {

    /**
     * 解析一个xmlfile返回一个对象,输入文件的名称
     *
     * @return
     */
    private HashMap<String,Object> result=new HashMap<>();
    public static Object parse(String file) throws IOException, ParserConfigurationException, SAXException {
        SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
        parser.parse(new File(file), new DataSourceHandler());
        return null;
    }
    static class DataSourceHandler extends DefaultHandler {
        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes)
                throws SAXException {
            System.out.println(uri);
            System.out.println(localName);
            System.out.println(qName);
            System.out.println(attributes);
        }

        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            super.characters(ch, start, length);
        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            super.endElement(uri, localName, qName);
        }
    }
}
