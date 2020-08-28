package cn.xisun.flink.kafka.connector.deserializer;

import cn.xisun.flink.kafka.connector.model.PatentMessage;
import org.apache.flink.api.common.serialization.DeserializationSchema;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

/**
 * @author XiSun
 * @Date 2020/8/24 15:19
 * <p>
 * 反序列化PatentMessage对象
 */
public class PatentMessageDeserializer implements DeserializationSchema<PatentMessage> {
    @Override
    public PatentMessage deserialize(byte[] bytes) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(bytes, PatentMessage.class);
    }

    @Override
    public boolean isEndOfStream(PatentMessage patentMessage) {
        return false;
    }

    @Override
    public TypeInformation<PatentMessage> getProducedType() {
        return TypeInformation.of(PatentMessage.class);
    }
}
