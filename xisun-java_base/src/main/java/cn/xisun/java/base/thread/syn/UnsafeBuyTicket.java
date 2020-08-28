package cn.xisun.java.base.thread.syn;

/**
 * @author XiSun
 * @Date 2020/8/18 8:58
 * <p>
 * 不安全的购票：
 * 可能买到重复票，或者负数的票
 */
public class UnsafeBuyTicket {
    public static void main(String[] args) {
        BuyTicket buyTicket = new BuyTicket();

        new Thread(buyTicket, "小米").start();
        new Thread(buyTicket, "小蓝").start();
        new Thread(buyTicket, "小龙").start();
    }
}

class BuyTicket implements Runnable {
    private int ticketNums = 10;
    private boolean flag = true;

    @Override
    public void run() {
        // 买票
        while (flag) {
            buy();
        }
    }

    /**
     * 添加synchronized关键字后，buy方法为同步方法，线程安全
     */
    private /*synchronized*/ void buy() {
        // 判断是否有票
        if (ticketNums <= 0) {
            flag = false;
            return;
        }

        // 模拟延时
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 买票
        System.out.println(Thread.currentThread().getName() + "拿到第" + ticketNums-- + "张票");
    }
}
