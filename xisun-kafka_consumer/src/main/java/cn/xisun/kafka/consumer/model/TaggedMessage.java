package cn.xisun.kafka.consumer.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author XiSun
 * @Date 2020/8/24 11:15
 * <p>
 * 打标签信息
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TaggedMessage {
    /**
     * 专利名
     */
    private String patentName;

    /**
     * 打标签内容
     */
    private String taggedContent;
}
