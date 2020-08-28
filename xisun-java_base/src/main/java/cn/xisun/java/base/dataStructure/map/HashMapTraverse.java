package cn.xisun.java.base.dataStructure.map;

import java.util.Map;

/**
 * @author XiSun
 * @Date 2020/8/27 15:24
 * <p>
 * HashMap循环遍历方式
 */
public class HashMapTraverse {
    /**
     * 遍历方式1：for each map.entrySet()
     * 注意：适用于既需要key，也需要value的场景
     *
     * @param map 待处理的map
     */
    public void traverseKeyAndValue(Map<String, String> map) {
        for (Map.Entry<String, String> entry : map.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            System.out.println(key + ": " + value);
        }
    }

    /**
     * 遍历方式2：for each map.keySet()，再调用get获取
     * 注意：适用于只需要key的场景
     *
     * @param map 待处理的map
     */
    public void traverseKey(Map<String, String> map) {
        for (String key : map.keySet()) {
            String keyValue = map.get(key);
            System.out.println(keyValue);
        }
    }
}
