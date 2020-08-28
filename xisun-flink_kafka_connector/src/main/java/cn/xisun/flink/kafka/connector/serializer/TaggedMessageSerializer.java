package cn.xisun.flink.kafka.connector.serializer;

import cn.xisun.flink.kafka.connector.model.TaggedMessage;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.flink.streaming.connectors.kafka.KafkaSerializationSchema;
import org.apache.kafka.clients.producer.ProducerRecord;

import javax.annotation.Nullable;

/**
 * @author XiSun
 * @Date 2020/8/24 15:09
 * <p>
 * 序列化TaggedMessage对象
 */
@AllArgsConstructor
public class TaggedMessageSerializer implements KafkaSerializationSchema<TaggedMessage> {
    private String topic;

    @SneakyThrows
    @Override
    public ProducerRecord<byte[], byte[]> serialize(TaggedMessage taggedMessage, @Nullable Long aLong) {
        ObjectMapper objectMapper = new ObjectMapper();
        return new ProducerRecord<>(topic, objectMapper.writeValueAsBytes(taggedMessage));
    }
}
