package cn.xisun.java.base.thread.state;

/**
 * @author XiSun
 * @Date 2020/8/17 17:08
 * <p>
 * 观察测试线程的状态
 */
public class ThreadState {
    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("////////");
        });

        // 观察new后线程状态
        Thread.State state = thread.getState();
        // state = NEW
        System.out.println("state = " + state);

        // 观察启动后线程状态
        thread.start();
        state = thread.getState();
        // state = RUNNABLE
        System.out.println("state = " + state);

        // 只要线程不停止，就一直输出状态
        while (state != Thread.State.TERMINATED) {
            Thread.sleep(500);
            // 更新线程状态
            state = thread.getState();
            // 输出状态
            System.out.println("state = " + state);
        }
    }
}
