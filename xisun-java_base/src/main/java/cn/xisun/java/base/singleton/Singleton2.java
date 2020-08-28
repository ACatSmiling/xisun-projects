package cn.xisun.java.base.singleton;

/**
 * @author XiSun
 * @Date 2020/8/17 14:50
 * <p>
 * 线程安全的单例模式
 * 锁住了一个方法，粒度有点大，改进就是只锁住其中的new语句就OK。就是所谓的"双重锁"机制
 */
public class Singleton2 {

    private static Singleton2 instance;

    private Singleton2() {

    }

    /**
     * 对获取实例的方法进行同步
     *
     * @return
     */
    public static synchronized Singleton2 getInstance() {
        if (instance == null) {
            instance = new Singleton2();
        }
        return instance;
    }
}
