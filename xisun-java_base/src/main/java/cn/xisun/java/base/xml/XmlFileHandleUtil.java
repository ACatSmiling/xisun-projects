package cn.xisun.java.base.xml;

import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import nu.xom.*;
import org.xml.sax.XMLReader;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @author XiSun
 * @Date 2020/8/26 17:07
 * <p>
 * 加载XML文件
 */
@Slf4j
public class XmlFileHandleUtil {
    public static Document doc;

    /**
     * 加载xml文件
     *
     * @param inputFile 待处理的xml文件
     */
    public static void loadFile(File inputFile) {
        try {
            XMLReader xmlreader = null;
            SAXParserFactory spf = SAXParserFactory.newInstance();
            SAXParser saxParser;
            try {
                saxParser = spf.newSAXParser();
                xmlreader = saxParser.getXMLReader();
                // 忽略xml文件里的dtd验证
                xmlreader.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
            } catch (Exception e) {
                e.printStackTrace();
            }
            doc = new Builder(xmlreader).build(inputFile);
        } catch (ParsingException e) {
            log.error("xml文件解析失败: {}", inputFile);
        } catch (IOException e) {
            log.error("xml文件读取失败: {}", inputFile);
        }
    }

    /**
     * 获得Element对象完整的节点名和属性值
     *
     * @param element 待处理的Element对象
     * @return 节点名和属性值组成的字符串
     */
    public static String getElementAttributeValue(Element element) {
        StringBuilder sb = new StringBuilder();
        // 节点名
        sb.append("<" + element.getLocalName());
        int attributeCount = element.getAttributeCount();
        for (int i = 0; i < attributeCount; i++) {
            // 属性值
            Attribute attribute = element.getAttribute(i);
            sb.append(" " + attribute.getLocalName() + "=" + "\"" + attribute.getValue() + "\"");
        }
        sb.append(">");
        return sb.toString();
    }

    /**
     * 获得待处理的xml文件中Description节点下的heading和p两个节点元素的List集合
     * 1. document.getRootElement()：获得document文件的根节点(被解析xml文件的根节点)
     * 2. rootElement.getFirstChildElement("description")：获得document文件根节点下的description节点
     * 3. XomUtils.getChildElementsWithTagNames()：获得description节点下的heading和p两个节点
     * 4. 最终返回heading和p节点对应的Element元素的集合
     * 5. 如果没有符合条件的Element元素，返回null
     *
     * @return 符合条件的Element对象集合，或者null
     */
    public static List<Element> loadDocumentWithTagNames() {
        return XomUtils.getChildElementsWithTagNames(
                doc.getRootElement().getFirstChildElement(XmlTags.DESCRIPTION),
                new String[]{XmlTags.HEADING, XmlTags.P});
    }

    /**
     * 将element转成JSONObject
     * 当属性与子节点为最小单元时，不再进行递归
     * 注意：并非所有xml文件均能完美转换，使用时要检查
     *
     * @param element
     * @return
     */
    public static JSONObject elementToJSONObject(Element element) {
        JSONObject result = new JSONObject();

        // <1> 将element的每一个元素及其值，转为json
        int attributeCount = element.getAttributeCount();
        // 当属性值不为空时，遍历得到每个属性值
        for (int i = 0; i < attributeCount; i++) {
            Attribute attribute = element.getAttribute(i);
            result.put(attribute.getLocalName(), attribute.getValue());
        }

        // 递归遍历当前节点所有的子节点
        Elements children = element.getChildElements();
        int size = children.size();
        if (size == 0) {
            // <2> 当前节点没有子节点，将节点的值，转为json
            if (!"".equals(element.getValue())) {
                result.put("element-value", element.getValue());
            }
        } else if (size == 1) {
            // <3> 当前节点只有一个子节点时，将子节点的属性和值，转为json
            result.put(children.get(0).getLocalName(), elementToJSONObject(children.get(0)));
        } else {
            // <4> 当一级子节点存在相同标签时，转为数组
            if (hasSameLabel(children)) {
                result.put(element.getLocalName(), elementToJSONArray(element));
            } else {
                // <5> 当一级子节点不存在相同标签时，遍历所有一级子节点
                for (Element child : children) {
                    // <6> 判断一级节点是否有属性和子节点，沒有则将当前节点作为上级节点的属性对待(直接转化为子标签)
                    if (child.getAttributeCount() == 0 && child.getChildElements().size() == 0) {
                        result.put(child.getLocalName(), child.getValue());
                    } else {
                        // 若存在子节点或者属性不为空，要继续向下一级转化
                        if (hasSameLabel(child.getChildElements())) {
                            // <7> 当下一级存在相同标签时，转为数组
                            result.put(child.getLocalName(), elementToJSONArray(child));
                        } else {
                            // <8> 当下一级不存在相同标签，仍为json对象
                            result.put(child.getLocalName(), elementToJSONObject(child));
                        }
                    }
                }
            }
        }

        return result;
    }

    /**
     * 将element转成JSONArray
     *
     * @param element
     * @return
     */
    public static JSONArray elementToJSONArray(Element element) {
        JSONArray jsonArray = new JSONArray();

        // 所有一级子节点
        Elements children = element.getChildElements();
        if (children.size() != 0) {
            // 遍历所有一级子节点
            for (Element child : children) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put(child.getLocalName(), elementToJSONObject(child));
                jsonArray.add(jsonObject);
            }
        }

        return jsonArray;
    }

    /**
     * 判断elements中是否存在相同的element
     *
     * @param elements
     * @return
     */
    public static Boolean hasSameLabel(Elements elements) {
        Boolean flag = false;
        for (int i = 0; i < elements.size() - 1; i++) {
            if (elements.get(i).getLocalName().equals(elements.get(i + 1).getLocalName())) {
                flag = true;
                break;
            }
        }
        return flag;
    }
}
