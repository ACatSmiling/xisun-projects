package cn.xisun.kryo.pool;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.util.Pool;
import org.objenesis.strategy.StdInstantiatorStrategy;

/**
 * @author XiSun
 * @Date 2020/8/12 9:11
 */
public enum KryoPool {
    //实例
    Instance;
    private Pool<Kryo> kryoPool;

    KryoPool() {
        kryoPool = new Pool<Kryo>(true, false, 8) {
            @Override
            protected Kryo create() {
                Kryo kryo = new Kryo();
                // Configure the Kryo instance.
                kryo.setReferences(true);
                kryo.setRegistrationRequired(false);
                kryo.setInstantiatorStrategy(new StdInstantiatorStrategy());
                return kryo;
            }
        };
    }

    public Pool<Kryo> getKryoPool() {
        return kryoPool;
    }

    public static void main(String[] args) {
        Pool<Kryo> kryoPool = KryoPool.Instance.getKryoPool();
        Kryo kryo = kryoPool.obtain();
        // Use the Kryo instance here.
        System.out.println("kryoPool.getFree() = " + kryoPool.getFree());
        System.out.println("kryoPool.getPeak() = " + kryoPool.getPeak());
        kryoPool.free(kryo);// 释放kryo
    }
}
