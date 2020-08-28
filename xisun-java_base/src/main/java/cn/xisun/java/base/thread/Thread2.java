package cn.xisun.java.base.thread;

/**
 * @author XiSun
 * @Date 2020/8/18 9:44
 * <p>
 * 创建线程方式二：
 * 1. 实现Runnable接口，重写run()方法
 * 2. 启动线程时，需要将Runnable接口实现类的实体对象，放入Thread实体对象中，再调用其start()方法
 */
public class Thread2 implements Runnable {
    @Override
    public void run() {
        // 线程体
        System.out.println("当前线程名：" + Thread.currentThread().getName());
    }

    public static void main(String[] args) {
        // 1> 创建Runnable接口实现类的实体对象
        Thread2 thread2 = new Thread2();

        // 2> 将该实体对象放入Thread实体对象中，并调用start()方法启动线程
        new Thread(thread2).start();
    }
}
