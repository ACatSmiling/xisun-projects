package cn.xisun.java.base.singleton;

/**
 * @author XiSun
 * @Date 2020/8/17 14:37
 * <p>
 * 线程安全的单例模式
 * 缺点是类加载的时候就会直接new一个静态对象出来，当系统中这样的类较多时，会使得启动速度变慢
 * 没有实现懒加载
 */
public class Singleton1 {
    /**
     * 类加载时，直接初始化一个静态实例对象
     */
    private static final Singleton1 SINGLETON1 = new Singleton1();

    private Singleton1() {
        // private类型的构造函数，保证其他类对象不能直接new一个该对象的实例
    }

    /**
     * 该类唯一的一个public方法
     *
     * @return 返回类的静态实例
     */
    public static Singleton1 getInstance() {
        return SINGLETON1;
    }
}
