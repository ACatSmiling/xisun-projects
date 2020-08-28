package cn.xisun.java.base.thread.state;

/**
 * @author XiSun
 * @Date 2020/8/17 17:28
 * <p>
 * 礼让不一定成功，由CPU调度决定
 * <p>
 * 礼让成功如：
 * a线程开始执行
 * b线程开始执行
 * a线程停止执行
 * b线程停止执行
 * <p>
 * 礼让不成功如：
 * a线程开始执行
 * a线程停止执行
 * b线程开始执行
 * b线程停止执行
 */
public class ThreadYield {
    public static void main(String[] args) {
        MyYield myYield = new MyYield();

        new Thread(myYield, "a").start();
        new Thread(myYield, "b").start();
    }
}

class MyYield implements Runnable {
    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + "线程开始执行");
        Thread.yield();// 线程礼让
        System.out.println(Thread.currentThread().getName() + "线程停止执行");
    }
}
