package cn.xisun.java.base.thread.syn;

/**
 * @author XiSun
 * @Date 2020/8/18 9:06
 * <p>
 * 不安全的取钱
 */
public class UnsafeBank {
    public static void main(String[] args) {
        Account marry = new Account("结婚备用金", 100);

        new Drawing(marry, 50, "you").start();
        new Drawing(marry, 100, "girlFriend").start();
    }
}

/**
 * 个人账户
 */
class Account {
    /**
     * 账户名
     */
    private String name;

    /**
     * 账户余额
     */
    private int money;

    public Account(String name, int money) {
        this.name = name;
        this.money = money;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }
}

/**
 * 银行：模拟取钱
 */
class Drawing extends Thread {
    /**
     * 账户
     */
    private Account account;

    /**
     * 取了多少钱
     */
    private int drawingMoney;

    /**
     * 现在手里有多少钱
     */
    private int nowMoney;

    public Drawing(Account account, int drawingMoney, String name) {
        super(name);
        this.account = account;
        this.drawingMoney = drawingMoney;
    }

    /**
     * 取钱
     * 注意：synchronized不能锁定run方法，因为run方法的this对象是不同的线程实例
     */
    @Override
    public void run() {
        // 先判断有没有钱
        if (drawingMoney > account.getMoney()) {
            System.out.println(Thread.currentThread().getName() + "，账户余额不足！");
            return;
        }

        // 放大问题
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 重新设置账户余额
        account.setMoney(account.getMoney() - drawingMoney);
        // 现在手里余额
        nowMoney = nowMoney + drawingMoney;

        System.out.println(account.getName() + "账户余额：" + account.getMoney());
        System.out.println(Thread.currentThread().getName() + "取钱：" + drawingMoney);
        // this = Thread.currentThread()
        System.out.println(this.getName() + "手里的钱：" + nowMoney);
    }
}
