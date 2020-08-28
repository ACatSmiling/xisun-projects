package cn.xisun.java.base.dataStructure.list;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author XiSun
 * @Date 2020/8/27 15:51
 * <p>
 * ArrayList和LinkedList循环遍历方式
 */
public class ArrayListTraverse {
    /**
     * 遍历方式1：for each循环
     * 注意：无论ArrayList还是LinkedList，遍历均建议使用此方式
     *
     * @param list 待处理的list
     */
    public void traverseForEach(List<Integer> list) {
        for (Integer value : list) {
            System.out.println(value);
        }
    }

    /**
     * 遍历方式2：调用集合迭代器
     * 注意：此方式与方式1等效
     *
     * @param list 待处理的list
     */
    public void traverseIterator(List<Integer> list) {
        Iterator<Integer> iterator = list.iterator();
        while (iterator.hasNext()) {
            Integer value = iterator.next();
            System.out.println(value);
        }
    }

    /**
     * 遍历方式3：下标递增循环
     * 注意：数据量较大时LinkedList避免使用此get遍历
     *
     * @param list 待处理的list
     */
    public void traverseSubscriptUp(List<Integer> list) {
        // 不要把list.size()放到循环条件内
        int size = list.size();
        for (int i = 0; i < size; i++) {
            Integer value = list.get(i);
            System.out.println(value);
        }
    }

    /**
     * 遍历方式4：下标递减循环
     * 注意：数据量较大时LinkedList避免使用此get遍历
     *
     * @param list 待处理的list
     */
    public void traverseSubscriptDown(List<Integer> list) {
        for (int i = list.size() - 1; i >= 0; i--) {
            Integer value = list.get(i);
            System.out.println(value);
        }
    }
}
