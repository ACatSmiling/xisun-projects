package cn.xisun.java.base.thread.syn;

import java.util.ArrayList;
import java.util.List;

/**
 * @author XiSun
 * @Date 2020/8/18 8:52
 * <p>
 * 线程不安全的集合
 */
public class UnsafeList {
    public static void main(String[] args) {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 10000; i++) {
            new Thread(() -> list.add(Thread.currentThread().getName())).start();
        }
        // list的size一般不会是10000
        System.out.println(list.size());
    }
}
