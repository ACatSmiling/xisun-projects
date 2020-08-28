package cn.xisun.flink.kafka.connector;

import cn.xisun.flink.kafka.connector.deserializer.PatentMessageDeserializer;
import cn.xisun.flink.kafka.connector.model.PatentMessage;
import cn.xisun.flink.kafka.connector.model.ReactionMessage;
import cn.xisun.flink.kafka.connector.serializer.ReactionMessageSerializer;
import org.apache.commons.collections.map.HashedMap;
import org.apache.flink.api.common.functions.RichFlatMapFunction;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer;
import org.apache.flink.streaming.connectors.kafka.internals.KafkaTopicPartition;
import org.apache.flink.util.Collector;

import java.util.Map;
import java.util.Properties;

/**
 * @author XiSun
 * @Date 2020/8/24 14:58
 * <p>
 * flink读取kafka中的消息，经转化后，再储存到kafka中
 */
public class StreamJob {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);
        env.setParallelism(1);

        // 定义kafka消费者
        Properties consumerProp = new Properties();
        consumerProp.setProperty("bootstrap.servers", "192.168.1.71:9092");
        FlinkKafkaConsumer<PatentMessage> consumer = new FlinkKafkaConsumer<>("patent_message_log", new PatentMessageDeserializer(), consumerProp);
//        consumer.setStartFromLatest();
        Map<KafkaTopicPartition, Long> offsets = new HashedMap();
        offsets.put(new KafkaTopicPartition("patent_message_log", 0), 18206L);
        consumer.setStartFromSpecificOffsets(offsets);

        // 定义kafka生产者
        Properties producerProp = new Properties();
        producerProp.setProperty("bootstrap.servers", "192.168.1.71:9092");
        FlinkKafkaProducer<ReactionMessage> producer = new FlinkKafkaProducer<>(
                "reaction_message_log",
                new ReactionMessageSerializer("reaction_message_log"),
                producerProp,
                FlinkKafkaProducer.Semantic.EXACTLY_ONCE);
        producer.setWriteTimestampToKafka(true);

        /**
         * 1> 消费Kafka中的消息
         * 2> 转换消息
         * 3> 重新添加到Kafka中
         */
        env.addSource(consumer)
                .flatMap(new Extractor())
                .addSink(producer);
        env.execute("patent message log loader");
    }

    /**
     * 抽取反应
     */
    private static class Extractor extends RichFlatMapFunction<PatentMessage, ReactionMessage> {

        @Override
        public void open(Configuration parameters) throws Exception {
            super.open(parameters);
        }

        @Override
        public void flatMap(PatentMessage patentMessage, Collector<ReactionMessage> collector) throws Exception {
            // 抽取反应的逻辑
        }

        @Override
        public void close() throws Exception {
            super.close();
        }
    }
}
