package cn.xisun.java.base.xml;

import net.sf.json.JSONObject;
import nu.xom.Element;
import nu.xom.Elements;
import org.junit.jupiter.api.Test;

import javax.sound.midi.Soundbank;
import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author XiSun
 * @Date 2020/8/27 14:29
 */
class XmlFileHandleUtilTest {
    @Test
    void xmlToJSONObject() {
        File file = new File("E:/t/US09532974-20170103.XML");
        XmlFileHandleUtil.loadFile(file);
        Element rootElement = XmlFileHandleUtil.doc.getRootElement();
        JSONObject jsonObject = new JSONObject();
        // 文件名，含后缀
        String fileName = file.getName();
        // 文件名，不含后缀
        String name = fileName.substring(0, fileName.lastIndexOf("."));
        jsonObject.put(name, XmlFileHandleUtil.elementToJSONObject(rootElement));
        System.out.println(jsonObject.toString());
    }
}