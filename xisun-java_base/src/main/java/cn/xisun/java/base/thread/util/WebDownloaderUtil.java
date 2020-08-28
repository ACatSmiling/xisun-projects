package cn.xisun.java.base.thread.util;

/**
 * @author XiSun
 * @Date 2020/8/18 14:26
 */
public class WebDownloaderUtil extends Thread {
    private String url;
    private String name;

    public WebDownloaderUtil(String url, String name) {
        this.url = url;
        this.name = name;
    }

    @Override
    public void run() {
        WebDownloader webDownloader = new WebDownloader();
        webDownloader.downloader(url, name);
        System.out.println("下载了文件名为：" + name);
    }

    public static void main(String[] args) {
        WebDownloaderUtil createdThread1Test = new WebDownloaderUtil("https://images2018.cnblogs.com/blog/832799/201808/832799-20180808135659046-1369102890.png", "2.jpg");
        createdThread1Test.start();
    }
}
