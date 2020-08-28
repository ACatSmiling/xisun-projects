package cn.xisun.java.base.thread.state;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author XiSun
 * @Date 2020/8/17 17:20
 * <p>
 * 模拟倒计时
 */
public class ThreadSleep2 {
    public static void tenDown() throws InterruptedException {
        int num = 10;
        while (true) {
            Thread.sleep(1000);
            // 每隔1s，输出一下num值
            System.out.println(num--);
            if (num <= 0) {
                break;
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        // 模拟倒计时
        tenDown();

        // 打印当前系统时间
        Date currentTime = new Date(System.currentTimeMillis());
        while (true) {
            Thread.sleep(1000);
            System.out.println(new SimpleDateFormat("HH:mm:ss").format(currentTime));
            // 更新当前系统时间
            currentTime = new Date(System.currentTimeMillis());
        }
    }
}
