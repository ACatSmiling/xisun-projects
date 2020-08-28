package cn.xisun.java.base.thread.state;

/**
 * @author XiSun
 * @Date 2020/8/17 17:18
 * <p>
 * 模拟网络延时：放大问题的发生性
 */
public class ThreadSleep implements Runnable {
    private int ticketNums = 10;

    @Override
    public void run() {
        while (true) {
            if (ticketNums <= 0) {
                break;
            }

            try {
                // 能更好的看出线程不安全的问题
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println(Thread.currentThread().getName() + "拿到了第" + ticketNums-- + "张票");
        }
    }

    public static void main(String[] args) {
        ThreadSleep threadSleep = new ThreadSleep();

        new Thread(threadSleep, "小米").start();
        new Thread(threadSleep, "小蓝").start();
        new Thread(threadSleep, "小花").start();
    }
}
