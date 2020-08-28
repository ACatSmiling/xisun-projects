package cn.xisun.redis.lettuce;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author XiSun
 * @Date 2020/8/12 9:55
 */
@Slf4j
class LettuceUtilTest {
    private LettuceUtil lettuceUtil = new LettuceUtil();

    @Test
    void synchronousApi() {
        lettuceUtil.synchronousApi();
    }

    @Test
    void consumingType() {
        lettuceUtil.consumingType();
    }

    @Test
    void reactiveApi() throws InterruptedException {
        lettuceUtil.reactiveApi();
    }

    @Test
    void testCustomPing() throws Exception {
        log.info("testCustomPing begin");
        lettuceUtil.customPing();
    }

    @Test
    void testCustomSet() throws Exception {
        log.info("testCustomSet begin");
        lettuceUtil.customSet();
    }

    @Test
    void testCustomDynamicSet() {
        log.info("testcustomDynamicSet begin");
        lettuceUtil.customDynamicSet();
    }

    @Test
    void testUseConnectionPool() throws Exception {
        log.info("testUseConnectionPool begin");
        lettuceUtil.useConnectionPool();
    }
}