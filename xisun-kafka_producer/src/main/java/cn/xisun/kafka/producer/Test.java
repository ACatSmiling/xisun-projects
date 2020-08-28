package cn.xisun.kafka.producer;

import java.io.File;

/**
 * @author XiSun
 * @Date 2020/8/24 10:16
 */
public class Test {
    public static void main(String[] args) {
        File file = new File("E:/ttt1");
        System.out.println(file.getName());
        System.out.println(file.exists());
    }
}
