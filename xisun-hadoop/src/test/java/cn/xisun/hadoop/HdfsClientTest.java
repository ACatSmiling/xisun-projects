package cn.xisun.hadoop;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author XiSun
 * @Date 2020/8/21 10:21
 */
class HdfsClientTest {
    @Test
    public void mkdirs() throws InterruptedException, IOException, URISyntaxException {
        HdfsClient hdfsClient = new HdfsClient();
        hdfsClient.mkdirs();
    }
}