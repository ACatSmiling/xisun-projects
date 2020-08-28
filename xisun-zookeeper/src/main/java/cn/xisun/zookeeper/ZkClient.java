package cn.xisun.zookeeper;

import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.*;

import java.util.List;

/**
 * @author XiSun
 * @Date 2020/8/19 9:07
 */
@Slf4j
public class ZkClient {
    private static final String CONNECT_STRING = "hadoop100:2181";
    private static final int SESSION_TIMEOUT = 2000;
    private ZooKeeper zkClient = null;

    public void init() throws Exception {
        zkClient = new ZooKeeper(CONNECT_STRING, SESSION_TIMEOUT, new Watcher() {
            @Override
            public void process(WatchedEvent watchedEvent) {
                // 收到事件通知后的回调函数（用户的业务逻辑）
                log.info("watchedEvent type---path: {}---{}", watchedEvent.getType(), watchedEvent.getPath());

                // 再次启动监听
                try {
                    zkClient.getChildren("/", true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void createNode(String path, String context) throws KeeperException, InterruptedException {
        zkClient.create(path, context.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
    }

    public void findNode(String path, Boolean watch) throws KeeperException, InterruptedException {
        List<String> children = zkClient.getChildren(path, watch);
        for (String child : children) {
            byte[] data = zkClient.getData("/" + child, true, null);
            log.info("child---data: {}---{}---{}", child, data, new String(data));
        }

    }
}
