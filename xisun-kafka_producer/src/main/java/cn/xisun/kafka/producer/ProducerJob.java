package cn.xisun.kafka.producer;

import cn.xisun.kafka.producer.model.PatentMessage;
import cn.xisun.kafka.producer.properties.ProducerProperties;
import cn.xisun.kafka.producer.utility.XmlTags;
import cn.xisun.kafka.producer.utility.XomUtils;
import lombok.extern.slf4j.Slf4j;
import nu.xom.*;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.xml.sax.XMLReader;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.List;
import java.util.Properties;

/**
 * @author XiSun
 * @Date 2020/8/24 9:54
 */
@Slf4j
public class ProducerJob {
    private static Document doc;
    public static Integer successCount = 0;
    public static Integer failedCount = 0;

    /**
     * 加载xml文件
     *
     * @param inputFile 待处理的xml文件
     */
    private static void loadFile(File inputFile) {
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
    private static String getElementAttributeValue(Element element) {
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
    private static List<Element> loadDocumentWithTagNames() {
        return XomUtils.getChildElementsWithTagNames(
                doc.getRootElement().getFirstChildElement(XmlTags.DESCRIPTION),
                new String[]{XmlTags.HEADING, XmlTags.P});
    }

    public static void main(String[] args) {
        Properties props = ProducerProperties.getProps();
        // 自定义value序列化工具
        props.put("value.serializer", "cn.xisun.kafka.producer.serialize.PatentMessageSerializer");
        KafkaProducer<String, PatentMessage> producer = new KafkaProducer<>(props);

        // 获取命令行参数
        /*String inputFilePath = args[0];// 文件输入路径
        Integer startFile = Integer.parseInt(args[1]);// 起始文件夹
        Integer endFile = Integer.parseInt(args[2]);// 结束文件夹
        logger.info("文件输入路径 = " + inputFilePath);
        logger.info("起始文件夹 = " + startFile);
        logger.info("结束文件夹 = " + endFile);*/
        String inputFilePath = "D:/test/2017_labeled_rawdata/4/";

        // 对文件输入路径下的每个子文件夹内的.xml和.XML文件进行处理
        for (int i = 1; i <= 1; i++) {
            File file = new File(inputFilePath + i);
            // 子文件夹存在
            if (file.exists()) {
                // 获取子文件夹内所有文件，放到文件数组里
                File[] fileList = file.listFiles();
                for (int j = 0; j < fileList.length; j++) {
                    // 当前文件
                    File currentFile = fileList[j];
                    // 当前文件是普通文件(排除文件夹)，且不是隐藏文件
                    if (currentFile.isFile() && !currentFile.isHidden()) {
                        // 当前文件的完整路径，含文件名
                        String currentFilePath = currentFile.getPath();
                        log.info("当前文件的完整路径: {}", currentFilePath);
                        // 当前文件的文件名，含后缀
                        String fileName = currentFile.getName();
                        if (fileName.endsWith("xml") || fileName.endsWith("XML")) {
                            // 加载xml文件
                            ProducerJob.loadFile(new File(currentFilePath));
                            // 获得xml文件中description节点下的所有heading和p节点元素
                            List<Element> allHPElements = ProducerJob.loadDocumentWithTagNames();
                            // xml文件有heading和p节点时，重新创建完整信息，并发送给Kafka
                            if (allHPElements != null) {
                                log.info("当前文件的heading和p节点元素个数: {}", allHPElements.size());
                                // 创建message
                                StringBuilder message = new StringBuilder();
                                message.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n");
                                message.append(getElementAttributeValue(doc.getRootElement()) + "\r\n");
                                message.append("<description id=\"description\">\r\n");
                                for (Element element : allHPElements) {
                                    message.append(element.toXML() + "\r\n");
                                }
                                message.append("</description>\r\n");
                                message.append("</" + doc.getRootElement().getLocalName() + ">");

                                // 发送到Kafka
                                PatentMessage patentMessage = new PatentMessage(fileName, message.toString());
                                // 不建议直接发送String字符串到kafka，因为字符串太长的话，会影响效率
                                ProducerRecord<String, PatentMessage> record = new ProducerRecord<>("patent_message_log", patentMessage);
                                // 相比同步发送，异步发送需要传入callback，发送结果回来回调callback方法
                                producer.send(record, (recordMetadata, exception) -> {
                                    if (exception != null) {
                                        exception.printStackTrace();
                                        failedCount++;
                                    } else {
                                        log.info("topic: {}", recordMetadata.topic());
                                        log.info("partition: {}", recordMetadata.partition());
                                        log.info("offset: {}", recordMetadata.offset());
                                        log.info("timestamp: {}", recordMetadata.timestamp());
                                        successCount++;
                                    }
                                });
                            }
                        }
                    }
                }
            } else {
                log.info("指定的文件夹不存在: {}", file.getName());
            }
        }
        producer.flush();
        producer.close();
        log.info("发送成功总数: {}", successCount);
        log.info("发送失败总数: {}", failedCount);
    }
}
