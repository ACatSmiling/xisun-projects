package cn.xisun.java.base.singleton;

import java.util.Arrays;

/**
 * @author XiSun
 * @Date 2020/8/17 14:53
 * <p>
 * 线程安全的单例模式
 * 既不用加锁，也能实现懒加载
 */
public class Singleton4 {
    private Singleton4() {
        System.out.println("single");
    }

    private static class Inner {
        // 因为是静态的，所以只会在第一次被直接使用的时候初始化，既保证了唯一性，也实现了懒加载
        private static final Singleton4 INSTANCE = new Singleton4();
    }

    public static Singleton4 getInstance() {
        return Inner.INSTANCE;
    }

    public static void main(String[] args) {
        Thread[] ths = new Thread[200];
        // 200个线程，调用的是同一个Singleton4实例
        for (int i = 0; i < ths.length; i++) {
            ths[i] = new Thread(() -> {
                System.out.println(Singleton4.getInstance());
            });
        }

        Arrays.asList(ths).forEach(o -> o.start());
    }
}
