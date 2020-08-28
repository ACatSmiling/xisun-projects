package cn.xisun.java.base.thread.state;

/**
 * @author XiSun
 * @Date 2020/8/17 17:10
 */
public class ThreadJoin implements Runnable {
    @Override
    public void run() {
        for (int i = 0; i < 100; i++) {
            System.out.println("线程VIP来了" + i);
        }
    }

    public static void main(String[] args) throws InterruptedException {
        ThreadJoin threadJoin = new ThreadJoin();
        Thread thread = new Thread(threadJoin);
        thread.start();

        for (int i = 0; i < 1000; i++) {
            System.out.println("main " + i);

            // 主线程执行到这个地方时，必须等到thread线程执行完毕之后，才会重新执行，此时主线程阻塞。在此代码之前，thread和main两个线程由CPU调度执行
            thread.join();
        }
    }
}
