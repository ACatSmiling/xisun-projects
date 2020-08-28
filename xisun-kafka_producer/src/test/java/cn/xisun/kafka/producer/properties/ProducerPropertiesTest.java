package cn.xisun.kafka.producer.properties;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.junit.jupiter.api.Test;

import java.util.Properties;

/**
 * @author XiSun
 * @Date 2020/8/21 11:48
 */
@Slf4j
class ProducerPropertiesTest {

    @Test
    void getProps() {
        Properties props = ProducerProperties.getProps();
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        KafkaProducer<String, String> producer = new KafkaProducer<>(props);

        for (int i = 0; i < 10; i++) {
            ProducerRecord<String, String> record = new ProducerRecord<>("patent_message_log", "topic_" + i, "test-asyn-" + i);

            // 相比同步发送，异步发送需要传入Callback，发送结果回来回调callback方法
            producer.send(record, new Callback() {
                @Override
                public void onCompletion(RecordMetadata recordMetadata, Exception e) {
                    if (e != null) {
                        e.printStackTrace();
                    } else {
                        log.info("topic: {}", recordMetadata.topic());
                        log.info("partition: {}", recordMetadata.partition());
                        log.info("offset: {}", recordMetadata.offset());
                    }
                }
            });

            // lambda表达式
            /*producer.send(record, (recordMetadata, e) -> {
                if (e != null) {
                    failedCount++;
                    e.printStackTrace();
                } else {
                    successCount++;
                    log.info("topic: {}", recordMetadata.topic());
                    log.info("partition: {}", recordMetadata.partition());
                    log.info("offset: {}", recordMetadata.offset());
                }
            });*/
        }

        producer.flush();
        producer.close();
    }
}