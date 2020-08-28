package cn.xisun.flink.kafka.connector.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author XiSun
 * @Date 2020/8/24 11:13
 * <p>
 * 反应信息
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReactionMessage {
    /**
     * 专利名
     */
    private String patentName;

    /**
     * 反应数
     */
    private int reactionNum;

    /**
     * 反应内容
     */
    private String reactionContent;
}
