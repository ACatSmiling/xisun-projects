package cn.xisun.java.base.singleton;

/**
 * @author XiSun
 * @Date 2020/8/17 14:52
 * <p>
 * 线程安全的单例模式
 * 改进Singleton2，"双重锁"机制
 */
public class Singleton3 {
    private static Singleton3 instance;

    private Singleton3() {

    }

    /**
     * 对获取实例的方法进行同步
     *
     * @return
     */
    public static Singleton3 getInstance() {
        if (instance == null) {
            synchronized (Singleton3.class) {
                if (instance == null) {
                    instance = new Singleton3();
                }
            }
        }
        return instance;
    }
}
