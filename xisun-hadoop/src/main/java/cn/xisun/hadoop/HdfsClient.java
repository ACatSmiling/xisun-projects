package cn.xisun.hadoop;

import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.FsStatus;
import org.apache.hadoop.fs.Path;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * @author XiSun
 * @Date 2020/8/21 10:21
 */
@Slf4j
public class HdfsClient {
    public void mkdirs() throws IOException, InterruptedException, URISyntaxException {
        // 1.创建一个配置对象，用于设置集群上的块大小，副本数量等
        Configuration configuration = new Configuration();
        // 配置在集群上运行
        // configuration.set("fs.defaultFS", "hdfs://hadoop102:9000");
        // FileSystem fs = FileSystem.get(configuration);

        URI uri = URI.create("hdfs://hadoop100:9000");// HDFS系统的URI
        FileSystem fs = FileSystem.get(uri, configuration, "xisun");
        FsStatus status = fs.getStatus();
        log.info("status.toString() = {}", status.toString());

        // 2 创建目录
//        fs.mkdirs(new Path("/user/xisun"));
        fs.rename(new Path("/user/xisun_3"), new Path("/user/xisun"));

        /*// 2 上传文件
        fs.copyFromLocalFile(new Path("e:/banzhang.txt"), new Path("/banzhang.txt"));
        fs.createNewFile(new Path("/u"));*/

        fs.getStatus();

        // 3 关闭资源
        fs.close();
    }
}
