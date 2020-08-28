package cn.xisun.java.base.thread.state;

/**
 * @author XiSun
 * @Date 2020/8/17 17:23
 * <p>
 * 停止线程：
 * 1. 不推荐使用JDK提供的stop()、destroy()方法停止线程 → 已废弃
 * 2. 建议线程正常运行完毕停止 → 利用次数，不建议死循环
 * 3. 建议使用一个标识位进行终止变量，当flag = false时，终止线程
 */
public class ThreadStop implements Runnable {
    /**
     * 1> 设置一个标识位，加volatile关键字，保证可见性
     */
    private volatile boolean flag = true;

    @Override
    public void run() {
        int i = 0;
        while (flag) {
        }
        System.out.println("run Thread " + i++);
    }

    /**
     * 2> 设置一个公开的方法停止线程，转换标识位
     */
    public void stop() {
        this.flag = false;
    }


    public static void main(String[] args) {
        ThreadStop threadStop = new ThreadStop();
        new Thread(threadStop, "t1").start();
        for (int i = 0; i < 1000; i++) {
            System.out.println("main " + i);
            if (i == 950) {
                // 调用stop()方法切换标识位，让线程t1停止
                threadStop.stop();
                System.out.println("线程t1该停止了");
            }
        }
    }
}
