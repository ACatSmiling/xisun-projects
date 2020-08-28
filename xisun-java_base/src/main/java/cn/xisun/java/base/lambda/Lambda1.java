package cn.xisun.java.base.lambda;

/**
 * @author XiSun
 * @Date 2020/8/17 11:58
 * <p>
 * Lambda表达式 Since java 8
 * <p>
 * 优点：
 * 避免匿名内部类定义过多；简化代码；去除无意义代码，只留下核心逻辑。
 * <p>
 * 适用条件：
 * 函数式接口：只包含唯一一个抽象方法的接口，如：public interface Runnable(){public abstract void run();}
 * <p>
 * 推导过程1：函数式接口无参
 */
public class Lambda1 {
    /**
     * 3.静态内部类（将外部实现类优化到内部）
     */
    static class Like2 implements ILike {
        @Override
        public void lambda() {
            System.out.println("I like lambda 2");
        }
    }

    public static void main(String[] args) {
        // 利用接口new一个实现类
        ILike like = new Like();
        like.lambda();

        like = new Like2();
        like.lambda();

        /**
         * 4.局部内部类
         */
        class Like3 implements ILike {
            @Override
            public void lambda() {
                System.out.println("I like lambda 3");
            }
        }
        like = new Like3();
        like.lambda();

        /**
         * 5.匿名内部类，省略类的名称，必须借助接口或者父类
         */
        like = new ILike() {
            @Override
            public void lambda() {
                System.out.println("I like lambda 4");
            }
        };
        like.lambda();

        /**
         * 6.用lambda简化：只要方法，在()和{}中间添加->
         */
        like = () -> {
            System.out.println("I like lambda 5");
        };
        like.lambda();
    }
}

/**
 * 1.定义一个函数式接口，无参
 */
interface ILike {
    /**
     * 接口中定义了一个方法，用于区别实现类
     */
    void lambda();
}

/**
 * 2.外部实现类
 */
class Like implements ILike {
    @Override
    public void lambda() {
        System.out.println("I like lambda");
    }
}