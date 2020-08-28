package cn.xisun.java.base.dataStructure.map;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import java.util.Map;

/**
 * @author XiSun
 * @Date 2020/8/27 16:42
 * <p>
 * MultiMap的使用和遍历：com.google.guava库
 */
public class MultiMapTraverse {
    public static void traverse() {
        Multimap<String, String> myMultimap = ArrayListMultimap.create();

        // 添加key/value，key可以重复，value实际是添加在该key对应的一个集合中
        myMultimap.put("Fruits", "Bannana");
        myMultimap.put("Fruits", "Apple");
        myMultimap.put("Fruits", "Pear");
        myMultimap.put("Vegetables", "Carrot");
        myMultimap.put("Vegetables", "Carrot");

        for (Map.Entry<String, String> e : myMultimap.entries()) {
            System.out.println("key: " + e.getKey() + " - value: " + e.getValue());
        }
    }
}
