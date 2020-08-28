package cn.xisun.kafka.consumer;

import cn.xisun.kafka.consumer.model.ReactionMessage;
import cn.xisun.kafka.consumer.properties.ConsumerProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.*;

/**
 * @author XiSun
 * @Date 2020/8/24 11:12
 */
@Slf4j
public class ReactionConsumerJob {
    public static void main(String[] args) {
        Properties props = ConsumerProperties.getProps();
        // 自定义value反序列化工具
        props.put("value.deserializer", "cn.xisun.kafka.consumer.serialize.ReactionMessageDeserializer");
        KafkaConsumer<String, ReactionMessage> consumer = new KafkaConsumer<>(props);

        List<String> topics = new ArrayList<>();
        topics.add("reaction_message_log");
        // 可以订阅多个topic
        consumer.subscribe(topics);

        // 提取的反应总数
        Integer reactionNumsAll = 0;

        // 拉取任务超时时间
        while (true) {
            ConsumerRecords<String, ReactionMessage> records = consumer.poll(1000);

            // 指定特定的partition和偏移量
            Map<TopicPartition, OffsetAndMetadata> offsets = new HashMap<>();
            // 指定topic 和partition
            TopicPartition topicPartition = new TopicPartition("reaction_message_log", 0);
            // 指定offset
            OffsetAndMetadata offsetAndMetadata = new OffsetAndMetadata(414L);
            // 可以提交多个topic
            offsets.put(topicPartition, offsetAndMetadata);
            // 提交offset
            consumer.commitSync(offsets);

            // 展示消费结果
            for (ConsumerRecord consumerRecord : records) {
                log.info("partition: {}", consumerRecord.partition());
                log.info("offset: {}", consumerRecord.offset());
                log.info("key: {}", consumerRecord.key());
                log.info("value: {}", consumerRecord.value());

                ReactionMessage reactionMessage = (ReactionMessage) consumerRecord.value();
                reactionNumsAll += reactionMessage.getReactionNum();

                log.info("patentName: {}", reactionMessage.getPatentName());
                log.info("reactionNum: {}", reactionMessage.getReactionNum());
                log.info("reactionNumsAll: {}", reactionNumsAll);

                // 抽取的反应写入到本地
                try {
                    String patentName = reactionMessage.getPatentName();
                    Writer out = new FileWriter("D:/test/2017_labeled_reaction/" + patentName.substring(0, patentName.lastIndexOf(".")) + "-" + System.currentTimeMillis() + ".XML");
                    out.write(reactionMessage.getReactionContent());
                    out.close();
                } catch (IOException e) {
                    log.error("xml文件写入失败: {}", reactionMessage.getPatentName());
                }
            }
        }
    }
}
