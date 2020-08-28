package cn.xisun.kafka.producer.serialize;

import cn.xisun.kafka.producer.model.PatentMessage;
import lombok.SneakyThrows;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Serializer;

import java.util.Map;

/**
 * @author XiSun
 * @Date 2020/8/24 9:34
 * <p>
 * 序列化PatentMessage对象
 * 注意：生产者的序列化方式，应该和消费者的反序列化方式，一一对应
 */
public class PatentMessageSerializer implements Serializer<PatentMessage> {
    @Override
    public void configure(Map<String, ?> map, boolean b) {

    }

    @SneakyThrows
    @Override
    public byte[] serialize(String topic, PatentMessage patentMessage) {
        // 方式一
        ObjectMapper objectMapper = new ObjectMapper();
        byte[] bytes = objectMapper.writeValueAsBytes(patentMessage);
        return bytes;
    }

    @Override
    public void close() {

    }
}
