package cn.xisun.kafka.producer.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author XiSun
 * @Date 2020/8/21 11:40
 * <p>
 * 专利信息
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PatentMessage {
    /**
     * 专利名
     */
    private String patentName;

    /**
     * 专利内容
     */
    private String patentContent;
}
