package cn.xisun.java.base.lambda;

/**
 * @author XiSun
 * @Date 2020/8/17 14:12
 * <p>
 * 推导过程2：函数式接口有一个参数
 */
public class Lambda2 {
    /**
     * 3.静态内部类（将外部实现类优化到内部）
     */
    static class Love2 implements ILove {
        @Override
        public void love(int a) {
            System.out.println("I love lambda " + a);
        }
    }

    public static void main(String[] args) {
        // 利用接口new一个实现类
        ILove love = new Love();
        love.love(1);

        love = new Love2();
        love.love(2);

        /**
         * 4.局部内部类
         */
        class Love3 implements ILove {
            @Override
            public void love(int a) {
                System.out.println("I love lambda " + a);
            }
        }
        love = new Love3();
        love.love(3);

        /**
         * 5.匿名内部类，省略类的名称，必须借助接口或者父类
         */
        love = new ILove() {
            @Override
            public void love(int a) {
                System.out.println("I love lambda " + a);
            }
        };
        love.love(4);

        /**
         * 6.用lambda简化：只要方法，在()和{}中间添加->
         */
        love = (int a) -> {
            System.out.println("I love lambda " + a);
        };
        love.love(5);

        /**
         * 7.lambda表达式简化1：省略参数类型 → 多参数时，参数类型必须全部省略，或者全部不省略
         */
        love = (a) -> {
            System.out.println("I love lambda " + a);
        };
        love.love(6);

        /**
         * 8.lambda表达式简化2：省略括号 → 多参数时，不能省略括号，无参数时，也不能省略括号
         */
        love = a -> {
            System.out.println("I love lambda " + a);
        };
        love.love(7);

        /**
         * 9.lambda表达式简化3：省略花括号 → 只适用于方法体只有一行的情况，如果有多行，必须用代码块（花括号包裹）
         */
        love = a -> System.out.println("I love lambda " + a);
        love.love(8);
    }
}

/**
 * 1.定义一个函数式接口，一个参数
 */
interface ILove {
    /**
     * 接口中定义了一个方法，用于区别实现类
     *
     * @param a 简单参数
     */
    void love(int a);
}

/**
 * 2.外部实现类
 */
class Love implements ILove {
    @Override
    public void love(int a) {
        System.out.println("I love lambda " + a);
    }
}
