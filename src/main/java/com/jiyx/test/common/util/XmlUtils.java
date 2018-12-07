package com.jiyx.test.common.util;

import com.jiyx.test.common.constant.FileNameConstant;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.util.List;

/**
 * auther: jiyx
 * date: 2018/12/2.
 */
public class XmlUtils {
    /**
     * 解析
     * @param contextConfigLocation
     * @return
     */
    public static List<Element> getElements(String contextConfigLocation) {
        return getRootElement(contextConfigLocation).elements();
    }

    public static Element getRootElement(String contextConfigLocation) {
        SAXReader reader = new SAXReader();
        Document document = null;
        String path = FileNameConstant.PATH + contextConfigLocation;
        try {
            document = reader.read(new File(path));
        } catch (DocumentException e) {
            e.printStackTrace();
        }

        return document.getRootElement();
    }

    public static void main(String[] args) throws ClassNotFoundException {
        Class.forName("com.mysql.jdbc.Driver");
    }
}
