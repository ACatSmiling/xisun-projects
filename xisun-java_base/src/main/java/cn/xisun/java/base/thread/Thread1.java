package cn.xisun.java.base.thread;

/**
 * @author XiSun
 * @Date 2020/8/18 9:41
 * <p>
 * 创建线程方法一：
 * 1. 继承Thread类，重写run()方法
 * 2. 启动线程时，调用该继承类实体对象的start()方法
 * <p>
 * 注意：线程由CPU调度执行
 */
public class Thread1 extends Thread {
    @Override
    public void run() {
        // 线程体
        System.out.println("当前线程名：" + Thread.currentThread().getName());
    }

    public static void main(String[] args) {
        // 1> 主线程
        System.out.println("当前线程名：" + Thread.currentThread().getName());

        // 2> 创建Thread继承类的实体对象
        Thread1 thread1 = new Thread1();

        // 3> 调用start()方法启动线程，在主线程main函数内，启动创建的线程thread1
        thread1.start();
    }
}
