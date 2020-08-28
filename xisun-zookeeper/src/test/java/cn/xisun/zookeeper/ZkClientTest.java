package cn.xisun.zookeeper;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author XiSun
 * @Date 2020/8/19 9:16
 */
class ZkClientTest {
    @Test
    public void create() throws Exception {
        ZkClient zkClient = new ZkClient();
        zkClient.init();
//        zkClient.createNode("/xisun", "xisun");
        zkClient.findNode("/", true);
    }
}