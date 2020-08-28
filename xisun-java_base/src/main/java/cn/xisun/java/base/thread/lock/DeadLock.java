package cn.xisun.java.base.thread.lock;

/**
 * @author XiSun
 * @Date 2020/8/21 10:35
 * <p>
 * 死锁：多个线程互相抱着对方需要的资源，然后形成僵持
 */
public class DeadLock {
    public static void main(String[] args) {
        Makeup girl1 = new Makeup(0, "girl1");
        Makeup girl2 = new Makeup(1, "girl2");
        girl1.start();
        girl2.start();
    }
}

/**
 * 口红
 */
class Lipstick {

}

/**
 * 镜子
 */
class Mirror {

}

class Makeup extends Thread {
    // 用static保证需要的资源只有一份

    static Lipstick lipstick = new Lipstick();
    static Mirror mirror = new Mirror();

    /**
     * 选择
     */
    int choice;
    /**
     * 用化妆品的人
     */
    String girlName;

    public Makeup(int choice, String girlName) {
        this.choice = choice;
        this.girlName = girlName;
    }

    @Override
    public void run() {
        makeup();
    }

    /**
     * 化妆，互相持有对方的锁，就是需要拿到对方的资源
     */
    private void makeup() {
        if (choice == 0) {
            // 获得口红的锁
            synchronized (lipstick) {
                System.out.println(this.girlName + "获得口红的锁");

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                // 一秒钟之后想获得镜子
                synchronized (mirror) {
                    System.out.println(this.girlName + "获得镜子的锁");
                }
            }
        } else {
            // 获得镜子的锁
            synchronized (mirror) {
                System.out.println(this.girlName + "获得镜子的锁");

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                // 一秒钟之后想获得口红
                synchronized (lipstick) {
                    System.out.println(this.girlName + "获得口红的锁");
                }
            }
        }
    }

    /**
     * 用完锁后，释放，即不会发生死锁现象
     */
    private void makeup2() {
        if (choice == 0) {
            // 获得口红的锁
            synchronized (lipstick) {
                System.out.println(this.girlName + "获得口红的锁");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            // 一秒钟之后想获得镜子
            synchronized (mirror) {
                System.out.println(this.girlName + "获得镜子的锁");
            }
        } else {
            // 获得镜子的锁
            synchronized (mirror) {
                System.out.println(this.girlName + "获得镜子的锁");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            // 一秒钟之后想获得口红
            synchronized (lipstick) {
                System.out.println(this.girlName + "获得口红的锁");
            }
        }
    }
}