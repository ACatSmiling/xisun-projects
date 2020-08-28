package cn.xisun.java.base.thread.lock;

import java.util.concurrent.locks.ReentrantLock;

/**
 * @author XiSun
 * @Date 2020/8/21 10:33
 */
public class LockBuyTicket {
    public static void main(String[] args) {
        BuyTicket buyTicket = new BuyTicket();

        new Thread(buyTicket, "小米").start();
        new Thread(buyTicket, "小蓝").start();
        new Thread(buyTicket, "小龙").start();
    }
}

/**
 * 使用ReentrantLock
 */
class BuyTicket implements Runnable {
    private int ticketNums = 10;

    /**
     * 定义lock锁
     */
    private final ReentrantLock lock = new ReentrantLock();

    @Override
    public void run() {
        while (true) {
            // 加锁
            lock.lock();
            try {
                if (ticketNums > 0) {
                    System.out.println(Thread.currentThread().getName() + "拿到第" + ticketNums-- + "张票");
                    // 模拟延时
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    break;
                }
            } finally {
                // 解锁
                lock.unlock();
            }
        }
    }
}
