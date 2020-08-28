package cn.xisun.flink.kafka.connector.serializer;

import cn.xisun.flink.kafka.connector.model.ReactionMessage;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.flink.streaming.connectors.kafka.KafkaSerializationSchema;
import org.apache.kafka.clients.producer.ProducerRecord;

import javax.annotation.Nullable;

/**
 * @author XiSun
 * @Date 2020/8/24 15:06
 * <p>
 * 序列化ReactionMessage对象
 */
@AllArgsConstructor
public class ReactionMessageSerializer implements KafkaSerializationSchema<ReactionMessage> {
    private String topic;

    @SneakyThrows
    @Override
    public ProducerRecord<byte[], byte[]> serialize(ReactionMessage reactionMessage, @Nullable Long aLong) {
        ObjectMapper objectMapper = new ObjectMapper();
        return new ProducerRecord<>(topic, objectMapper.writeValueAsBytes(reactionMessage));
    }
}
