package cn.xisun.java.base.lambda;

/**
 * @author XiSun
 * @Date 2020/8/17 14:29
 * <p>
 * 推导过程3：函数式接口不止一个参数
 */
public class Lambda3 {
    /**
     * 3.静态内部类（将外部实现类优化到内部）
     */
    static class Enjoy2 implements IEnjoy {
        @Override
        public void enjoy(int a, int b) {
            System.out.println("I enjoy lambda " + a + " " + b);
        }
    }

    public static void main(String[] args) {
        // 利用接口new一个实现类
        IEnjoy enjoy = new Enjoy();
        enjoy.enjoy(1, 2);

        enjoy = new Enjoy2();
        enjoy.enjoy(3, 4);

        /**
         * 4.局部内部类
         */
        class Enjoy3 implements IEnjoy {
            @Override
            public void enjoy(int a, int b) {
                System.out.println("I enjoy lambda " + a + " " + b);
            }
        }
        enjoy = new Enjoy3();
        enjoy.enjoy(5, 6);

        /**
         * 5.匿名内部类，省略类的名称，必须借助接口或者父类
         */
        enjoy = new IEnjoy() {
            @Override
            public void enjoy(int a, int b) {
                System.out.println("I enjoy lambda " + a + " " + b);
            }
        };
        enjoy.enjoy(7, 8);

        /**
         * 6.用lambda简化：只要方法，在()和{}中间添加->
         */
        enjoy = (int a, int b) -> {
            System.out.println("I enjoy lambda " + a + " " + b);
        };
        enjoy.enjoy(9, 10);

        /**
         * 7.lambda表达式简化1：省略参数类型 → 多参数时，参数类型必须全部省略，或者全部不省略
         */
        enjoy = (a, b) -> {
            System.out.println("I enjoy lambda " + a + " " + b);
        };
        enjoy.enjoy(11, 12);

        /**
         * 8.lambda表达式简化2：省略括号 → 多参数时，不能省略括号
         */
        /*enjoy = a, b -> {
            System.out.println("I enjoy lambda " + a + " " + b);
        };
        enjoy.enjoy(13, 14);*/

        /**
         * 9.lambda表达式简化3：省略花括号 → 只适用于方法体只有一行的情况，如果有多行，必须用代码块（花括号包裹）
         */
        enjoy = (a, b) -> System.out.println("I enjoy lambda " + a + " " + b);
        enjoy.enjoy(15, 16);
    }
}

/**
 * 1.定义一个函数式接口，两个参数
 */
interface IEnjoy {
    /**
     * 接口中定义了一个方法，用于区别实现类
     *
     * @param a 简单参数
     * @param b 简单参数
     */
    void enjoy(int a, int b);
}

/**
 * 2.外部实现类
 */
class Enjoy implements IEnjoy {
    @Override
    public void enjoy(int a, int b) {
        System.out.println("I enjoy lambda " + a + " " + b);
    }
}
