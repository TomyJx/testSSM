package com.jiyx.test.mybatis;

import com.jiyx.test.common.constant.MapperXmlEnum;
import com.jiyx.test.common.util.XmlUtils;
import org.dom4j.Element;

import java.util.ArrayList;
import java.util.List;

/**
 * auther: jiyx
 * date: 2018/12/2.
 */
public class XmlBuilderMapper {

    /**
     * @param clazz
     * @param xmlMapperPath
     * @return
     */
    public List<MapperInfo> buildMapper(Class clazz, String xmlMapperPath) {
        List<MapperInfo> mapperInfoList = new ArrayList<>();

        String xmlPath = getXmlPath(clazz, xmlMapperPath);

        Element rootElement = XmlUtils.getRootElement(xmlPath);

        List<Element> elements = rootElement.elements();
        if (elements != null) {
            // 暂时只处理所有的select请求
            elements.stream().filter(element -> MapperXmlEnum.SELECT.getName().equals(element.getName()))
                    .forEach(element -> {
                        MapperInfo mapperInfo = new MapperInfo();
                        mapperInfo.setInterfaceName(rootElement.attributeValue(MapperXmlEnum.NAMESPACE.getName()));
                        mapperInfo.setMethodName(element.attributeValue(MapperXmlEnum.ELEMENT_ID.getName()));
                        mapperInfo.setResultClassName(element.attributeValue(MapperXmlEnum.ELEMENT_RESULT_TYPE.getName()));
                        mapperInfo.setSqlContent(element.getText());
                        mapperInfoList.add(mapperInfo);
                    });
        }
        return mapperInfoList;
    }

    /**
     * 根据类名获取xml文件名
     * @param clazz
     * @param xmlMapperPath
     * @return
     */
    private String getXmlPath(Class clazz, String xmlMapperPath) {
        return xmlMapperPath + "/" + clazz.getSimpleName() + ".xml";
    }
}
