package cn.xisun.java.base.thread;

import java.util.concurrent.*;

/**
 * @author XiSun
 * @Date 2020/8/18 14:20
 * <p>
 * 创建线程方式三
 * 1. 实现Callable接口，重写call()方法
 */
public class Thread3 implements Callable<Integer> {
    @Override
    public Integer call() throws Exception {
        // 线程体
        System.out.println("当前线程名：" + Thread.currentThread().getName());

        // 返回Integer
        return 100;
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        // 1> 创建执行服务
        ExecutorService executorService = Executors.newFixedThreadPool(1);

        // 2> 执行线程
        Thread3 thread3 = new Thread3();
        Future<Integer> result = executorService.submit(thread3);

        // 3> 获取结果
        Integer integer = result.get();
        System.out.println("Integer = " + integer);

        // 4> 关闭服务
        executorService.shutdown();
    }
}
