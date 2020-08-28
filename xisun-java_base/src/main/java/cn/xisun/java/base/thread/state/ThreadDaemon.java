package cn.xisun.java.base.thread.state;

/**
 * @author XiSun
 * @Date 2020/8/17 15:29
 * <p>
 * 守护线程
 */
public class ThreadDaemon {
    public static void main(String[] args) {
        Earth earth = new Earth();
        Person person = new Person();

        Thread t1 = new Thread(earth);
        // 设置线程t1为守护线程，默认是false，即默认线程为用户线程
        t1.setDaemon(true);
        // 守护线程启动，当用户线程停止时，守护线程自动停止
        t1.start();

        Thread t2 = new Thread(person);
        // 用户线程启动
        t2.start();
    }
}

class Earth implements Runnable {
    @Override
    public void run() {
        while (true) {
            System.out.println("地球一直是人美好的家园");
        }
    }
}

class Person implements Runnable {
    @Override
    public void run() {
        for (int i = 0; i < 36500; i++) {
            System.out.println("开心的活着第" + i + "天");
        }
        System.out.println("=====GoodBye, World!=====");
    }
}
