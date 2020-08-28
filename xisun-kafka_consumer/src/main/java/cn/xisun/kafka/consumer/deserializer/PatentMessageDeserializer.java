package cn.xisun.kafka.consumer.deserializer;

import cn.xisun.kafka.consumer.model.PatentMessage;
import lombok.SneakyThrows;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Deserializer;

import java.util.Map;

/**
 * @author XiSun
 * @Date 2020/8/24 11:29
 * <p>
 * 反序列化PatentMessage对象
 * 注意：消费者的反序列化方式，应该和生产者的序列化方式，一一对应
 */
public class PatentMessageDeserializer implements Deserializer<PatentMessage> {
    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {

    }

    @SneakyThrows
    @Override
    public PatentMessage deserialize(String topic, byte[] data) {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(data, PatentMessage.class);
    }

    @Override
    public void close() {

    }
}
