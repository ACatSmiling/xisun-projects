package cn.xisun.java.base.thread.state;

/**
 * @author XiSun
 * @Date 2020/8/17 17:14
 * <p>
 * 线程优先级
 */
public class ThreadPriority {
    public static void main(String[] args) {
        // main线程的优先级
        System.out.println(Thread.currentThread().getName() + "的优先级 = " + Thread.currentThread().getPriority());

        MyPriority myPriority = new MyPriority();

        // 新线程t1的优先级
        Thread thread1 = new Thread(myPriority, "t1");
        Thread thread2 = new Thread(myPriority, "t2");
        Thread thread3 = new Thread(myPriority, "t3");
        Thread thread4 = new Thread(myPriority, "t4");
        Thread thread5 = new Thread(myPriority, "t5");
        Thread thread6 = new Thread(myPriority, "t6");

        // 先设置优先级，再启动
        thread1.start();

        thread2.setPriority(2);
        thread2.start();

        thread3.setPriority(Thread.MAX_PRIORITY);
        thread3.start();

        thread4.setPriority(Thread.MIN_PRIORITY);
        thread4.start();

        thread5.setPriority(8);
        thread5.start();

        /*// <0或>10，会出异常：java.lang.IllegalArgumentException
        thread6.setPriority(-1);
        thread6.start();*/
    }
}

class MyPriority implements Runnable {
    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + "的优先级 = " + Thread.currentThread().getPriority());
    }
}