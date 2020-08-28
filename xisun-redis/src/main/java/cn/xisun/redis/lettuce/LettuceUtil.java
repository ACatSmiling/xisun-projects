package cn.xisun.redis.lettuce;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisFuture;
import io.lettuce.core.RedisURI;
import io.lettuce.core.SetArgs;
import io.lettuce.core.api.StatefulConnection;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.async.RedisAsyncCommands;
import io.lettuce.core.api.reactive.RedisReactiveCommands;
import io.lettuce.core.api.sync.RedisCommands;
import io.lettuce.core.codec.RedisCodec;
import io.lettuce.core.codec.StringCodec;
import io.lettuce.core.dynamic.Commands;
import io.lettuce.core.dynamic.RedisCommandFactory;
import io.lettuce.core.dynamic.annotation.Command;
import io.lettuce.core.dynamic.annotation.CommandNaming;
import io.lettuce.core.dynamic.annotation.Param;
import io.lettuce.core.output.StatusOutput;
import io.lettuce.core.protocol.CommandArgs;
import io.lettuce.core.protocol.CommandType;
import io.lettuce.core.support.ConnectionPoolSupport;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import reactor.core.CoreSubscriber;
import reactor.core.publisher.Flux;
import reactor.core.publisher.GroupedFlux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * @author XiSun
 * @Date 2020/8/12 9:50
 * @Reference https://lettuce.io/core/release/reference/index.html
 * https://juejin.im/post/5d8eb73ff265da5ba5329c66#heading-22     https://my.oschina.net/u/3343218/blog/2989564
 * <p>
 * Lettuce特点：
 * 1.线程安全，可扩展；2.基于netty(非阻塞IO)和Reactor(反应式编程)；3.5.x融合了JDK1.8的异步编程特性；4.提供了同步，异步和反应式api ... ...
 */
@Slf4j
public class LettuceUtil {
    /**
     * 创建RedisURI
     *
     * @return
     */
    private RedisURI createRedisURI() {
        RedisURI redisUri = RedisURI.builder()
                .withHost("192.168.1.100")
                .withPort(6379)
                .withTimeout(Duration.of(10, ChronoUnit.SECONDS))
                .build();
        return redisUri;
    }

    /**
     * Basic usage
     *
     * @throws Exception
     */
    public void BasicUsage() throws Exception {
        // 1.创建连接信息
        RedisURI redisUri = RedisURI.builder()
                .withHost("192.168.1.100")
                .withPort(6379)
                .withTimeout(Duration.of(10, ChronoUnit.SECONDS))
                .build();
        // 2.创建Redis客户端，集群连接有一个定制的RedisClusterClient
        RedisClient redisClient = RedisClient.create(redisUri);
        // 3.创建线程安全的Redis连接，主要是StatefulConnection或者StatefulRedisConnection的子类，连接的类型主要由连接的具体方式(单机、哨兵、集群、订阅发布等)选定
        StatefulRedisConnection<String, String> connection = redisClient.connect();
        // 4.创建同步命令，其他还有异步(async)命令和反应式(reactive)命令
        RedisCommands<String, String> redisCommands = connection.sync();
        RedisAsyncCommands<String, String> async = connection.async();
        RedisReactiveCommands<String, String> reactive = connection.reactive();
        // 5.查询
        SetArgs setArgs = SetArgs.Builder.nx().ex(5);
        redisCommands.set("name", "throwable", setArgs);
        redisCommands.get("name");
        // 6.关闭连接
        connection.close();
        // 7.关闭客户端
        redisClient.shutdown();
    }

    /**
     * 同步api
     * <1> 用法类似Jedis
     */
    public void synchronousApi() {
        StatefulRedisConnection<String, String> connection = LettuceSingleClient.getStringConnection();
        RedisCommands<String, String> syncCommands = connection.sync();

        String pong = syncCommands.ping();
        log.info("pong: {}\n", pong);
    }

    /**
     * 异步api
     * <1> RedisFuture可以无缝使用Future或者JDK1.8中引入的CompletableFuture提供的方法
     *
     * <2> Interface CompletionStage<T>: https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/CompletionStage.html
     *
     * @throws ExecutionException
     * @throws InterruptedException
     * @throws TimeoutException
     */
    public void asynchronousApi() throws ExecutionException, InterruptedException, TimeoutException {
        StatefulRedisConnection<String, String> connection = LettuceSingleClient.getStringConnection();
        RedisAsyncCommands<String, String> asyncCommands = connection.async();

        RedisFuture<String> stringRedisFuture = asyncCommands.get("key");

        // -------------------------------------------------------------------------------------------------------------------
        // pull style:  block the calling thread at least until the value is computed but in the worst case indefinitely
        // -------------------------------------------------------------------------------------------------------------------
        // 超时等待1min，防止无限等待，如果超时，抛出TimeoutException
        String value = stringRedisFuture.get(1, TimeUnit.MINUTES);
        log.info("value: {}", value);

        // -------------------------------------------------------------------------------------------------------------------
        // push style: when the RedisFuture<T> is completed, a follow-up action is triggered
        // -------------------------------------------------------------------------------------------------------------------
        stringRedisFuture.thenAccept(new Consumer<String>() {
            @Override
            public void accept(String value) {
                log.info("value: {}", value);
            }
        });

        // 使用thenApply()方法对值进行简单的转换 ---> CompletionStage<T> API
        stringRedisFuture.thenApply(new Function<String, Integer>() {
            @Override
            public Integer apply(String value) {
                return value.length();
            }
        }).thenAccept(new Consumer<Integer>() {
            @Override
            public void accept(Integer length) {
                log.info("value length: {}", length);
            }
        });

        // 异常处理：使用handle()方法返回异常后的默认值
        stringRedisFuture.handle(new BiFunction<String, Throwable, String>() {
            @Override
            public String apply(String value, Throwable throwable) {
                if (throwable != null) {
                    return "default value";
                }
                return value;
            }
        }).thenAccept(new Consumer<String>() {
            @Override
            public void accept(String value) {
                log.info("value: {}", value);
            }
        });

        // 关于Retrying futures and recovery using futures  --->   查看Reactive API
    }


    /**
     * 反应式api
     * <1> All commands return a Flux<T>, Mono<T> or Mono<Void> to which a Subscriber can subscribe to;
     *
     * <2> Mono and Flux that are both publishers, a Mono can emit 0 to 1 events while a Flux can emit 0 to N events;
     *
     * <3> The last key point of a Publisher<T> is that the underlying processing is not started at the time the Publisher<T>
     * is obtained, rather its started at the moment an observer subscribes or signals demand to the Publisher<T>.
     * This is a crucial difference to a java.util.concurrent.Future, which is started somewhere at the time it is
     * created/obtained. So if no observer ever subscribes to the Publisher<T>, nothing ever will happen;
     *
     * <4> A Publisher<T> emits items until either an exception is raised or the Publisher<T> finishes the emission calling
     * onCompleted. No further elements are emitted after that time.
     */

    /**
     * <1> Consuming Publisher<T>
     */
    public void consumingType() {
        // 1. bottom is an example that subscribes and prints out all the items emitted   --->   use Subscriber<T>
        Flux.just("Ben", "Michael", "Mark")
                /**
                 * 限制发出的项的数量为N，N表示the first N elements
                 * 如果发出了预期的元素计数，订阅会被隐式地取消，即N超过发出的项的数量时，take操作无效(原文应该是这么理解)
                 */
                .take(2)
                // 将数据转化为一个List，返回一个List<String>
                .collectList()
                /**
                 * 说明：new Subscriber的参数类型，与前一步返回值类型相同，内部也可以添加属性，如start
                 */
                .subscribe(new Subscriber<List<String>>() {
                    // 时间管理
                    private Instant start;

                    @Override
                    // 说明：订阅处理开始
                    public void onSubscribe(Subscription subscription) {
                        subscription.request(Long.MAX_VALUE);
                        start = Instant.now();
                        log.info("start process ...");
                    }

                    @Override
                    // 说明：onNext的参数类型，与前一步返回值类型相同
                    public void onNext(List<String> s) {
                        // 订阅过程中，对每一次处理过程，发布者返回的值进行最终处理
                        log.info("value: {}", s);
                    }

                    @Override
                    // 说明：订阅过程中发生异常时，抛出异常
                    public void onError(Throwable throwable) {
                        throwable.printStackTrace();
                        log.error(throwable.getMessage());
                    }

                    @Override
                    // 说明：订阅处理完成
                    public void onComplete() {
                        log.info("end process\n");
                    }
                });

        // 2. rewrite the above code to a simpler form   --->   use Consumer<T>
        // 没有异常处理，建议使用上面1的方法
        Flux.just("Ben", "Michael", "Mark")
                .take(2)
                .doOnNext(new Consumer<String>() {
                    @Override
                    public void accept(String s) {
                        log.info("value: {}", s);
                    }
                })
                .doOnComplete(new Runnable() {
                    @Override
                    public void run() {
                        log.info("Completed\n");
                    }
                })
                .subscribe();
    }

    /**
     * <2> reactive api
     */
    public void reactiveApi() throws InterruptedException {
        StatefulRedisConnection<String, String> connection = LettuceSingleClient.getStringConnection();
        RedisReactiveCommands<String, String> reactiveCommands = connection.reactive();

        // 一个key
        reactiveCommands.get("Ben1")
                .subscribe(new Subscriber<String>() {
                    // 时间管理
                    private Instant start;

                    @Override
                    public void onSubscribe(Subscription subscription) {
                        subscription.request(Long.MAX_VALUE);
                        start = Instant.now();
                        log.info("start process 1 ...");
                    }

                    @Override
                    public void onNext(String value) {
                        log.info("value: {}", value);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        throwable.printStackTrace();
                        log.error(throwable.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        Instant end = Instant.now();
                        log.info("end process 1\n");
                    }
                });
        Thread.sleep(100);

        // 异步地加载多个key
        Flux.just("Ben", "Michael", "Mark")
                .flatMap(new Function<String, Publisher<String>>() {
                    @Override
                    public Publisher<String> apply(String key) {
                        return reactiveCommands.get(key);
                    }
                })
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onSubscribe(Subscription subscription) {
                        subscription.request(Long.MAX_VALUE);
                        log.info("start process 2 ...");
                    }

                    @Override
                    public void onNext(String value) {
                        log.info("value: {}", value);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        throwable.printStackTrace();
                        log.error(throwable.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        Instant end = Instant.now();
                        log.info("end process 2\n");
                    }
                });
        Thread.sleep(100);

        /**
         * map和flatmap的区别：
         * The difference between map() and flatMap() is that flatMap() allows you to do those transformations with Publisher<T> calls
         * 即：map和flatmap都可以对传入的参数处理(转换)，但flatmap能够返回Publisher<T>而map不行，而反应式api各命令，返回的都是Publisher<T>的实现类对象
         * 注意查看map和flatmap中Function函数的入参和出参
         */
        Flux.just("Ben", "Michael", "Mark")
                .map(new Function<String, String>() {
                    @Override
                    public String apply(String key) {
                        return key;
                    }
                })
                .flatMap(new Function<String, Publisher<String>>() {
                    @Override
                    public Publisher<String> apply(String key) {
                        return reactiveCommands.get(key);
                    }
                })
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onSubscribe(Subscription subscription) {
                        subscription.request(Long.MAX_VALUE);
                        log.info("start process 3 ...");
                    }

                    @Override
                    public void onNext(String value) {
                        log.info("value: {}", value);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        throwable.printStackTrace();
                        log.error(throwable.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        Instant end = Instant.now();
                        log.info("end process 3\n");
                    }
                });
        Thread.sleep(100);

        // reduce()聚合
        Flux.just("Ben", "Michael", "Mark")
                .flatMap(new Function<String, Publisher<String>>() {
                    @Override
                    public Publisher<String> apply(String key) {
                        return reactiveCommands.get(key);
                    }
                })
                /**
                 * reduce()方法，BiFunction三个参数，第一个和第二个是入参，第三个是出参，如果多次操作，后续的出参结果赋值给入参1(猜测是这样分配)
                 * the aggregation function of reduce() is applied on each emitted value, so three times in the example bellow
                 */
                .reduce(new BiFunction<String, String, String>() {
                    @Override
                    public String apply(String result1, String result2) {
                        log.info("result1: {}", result1);
                        log.info("result2: {}", result2);
                        return result1 + " /// " + result2;
                    }
                })
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onSubscribe(Subscription subscription) {
                        subscription.request(Long.MAX_VALUE);
                        log.info("start process 4 ...");
                    }

                    @Override
                    public void onNext(String value) {
                        log.info("value: {}", value);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        throwable.printStackTrace();
                        log.error(throwable.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        Instant end = Instant.now();
                        log.info("end process 4\n");
                    }
                });
        Thread.sleep(100);

        // groupby()分组 --->   计数
        Flux.just("Ben", "Michael", "Mark")
                .groupBy(new Function<String, String>() {
                    @Override
                    public String apply(String key) {
                        // 以key的首字母，作为分组依据
                        return key.substring(0, 1);
                    }
                })
                .subscribe(new Consumer<GroupedFlux<String, String>>() {
                    @Override
                    public void accept(GroupedFlux<String, String> groupedFlux) {
                        groupedFlux
                                .collectList()
                                .subscribe(new Subscriber<List<String>>() {
                                    @Override
                                    public void onSubscribe(Subscription subscription) {
                                        subscription.request(Long.MAX_VALUE);
                                        log.info("start process 5 ...");
                                    }

                                    @Override
                                    public void onNext(List<String> list) {
                                        // list是所有首字母相同的Key组成的集合
                                        log.info("First character: {}, elements: {}", groupedFlux.key(), list);
                                    }

                                    @Override
                                    public void onError(Throwable throwable) {
                                        throwable.printStackTrace();
                                        log.error(throwable.getMessage());
                                    }

                                    @Override
                                    public void onComplete() {
                                        log.info("end process 5\n");
                                    }
                                });
                    }
                });
        Thread.sleep(100);

        // 空值处理：defaultIfEmpty   switchIfEmpty   Flux.hasElements/Flux.hasElement   next/last/elementAt

        // filters()过滤器：过滤器不改变值，只发出符合条件的值
        Flux.just("Ben", "Michael", "Mark")
                .filter(new Predicate<String>() {
                    @Override
                    public boolean test(String key) {
                        // 返回以M开头的key
                        return key.startsWith("M");
                    }
                })
                .flatMap(new Function<String, Publisher<String>>() {
                    @Override
                    public Publisher<String> apply(String key) {
                        return reactiveCommands.get(key);
                    }
                })
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onSubscribe(Subscription subscription) {
                        subscription.request(Long.MAX_VALUE);
                        log.info("start process 6 ...");
                    }

                    @Override
                    public void onNext(String value) {
                        log.info("value: {}", value);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        throwable.printStackTrace();
                        log.error(throwable.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        log.info("end process 5\n");
                    }
                });
        Thread.sleep(100);

        // takelast(N)：获取后N个value，即Michael和Mark
        Flux.just("Ben", "Michael", "Mark")
                .takeLast(2)
                .subscribe(value -> log.info("value 7: {}", value));

        // next()：取第一个value？？？
        Flux.just("Ben", "Michael", "Mark")
                .next()
                .subscribe(value -> log.info("value 8: {}", value));

        // Error handling：几种错误处理机制见官网

        Flux.just("Ben", "Michael", "Mark").flatMap(key -> {
                    System.out.println("Map 1: " + key + " (" + Thread.currentThread().getName() + ")");
                    return Flux.just(key);
                }
        ).flatMap(value -> {
                    System.out.println("Map 2: " + value + " (" + Thread.currentThread().getName() + ")");
                    return Flux.just(value);
                }
        ).subscribe();

        Flux.just("Ben", "Michael", "Mark").flatMap(key -> {
                    System.out.println("Map 1: " + key + " (" + Thread.currentThread().getName() + ")");
                    return Flux.just(key);
                }
        ).flatMap(value -> {
                    System.out.println("Map 2: " + value + " (" + Thread.currentThread().getName() + ")");
                    return Flux.just(value);
                }
        ).subscribeOn(Schedulers.parallel()).subscribe();

    }

    public void testSyncSetAndGet() throws Exception {
        RedisURI redisURI = this.createRedisURI();
        RedisClient redisClient = RedisClient.create(redisURI);
        StatefulRedisConnection<String, String> connect = redisClient.connect();

        // 同步API，在所有命令调用之后会立即返回结果，与Jedis类似
        RedisCommands<String, String> sync = connect.sync();
        String pong = sync.ping();
        log.info("PING:{}", pong);
        SetArgs setArgs = SetArgs.Builder.nx().ex(5);
        sync.set("name", "throwable", setArgs);
        String value = sync.get("name");
        log.info("Get value: {}", value);

        // 异步API，所有方法执行返回结果都是RedisFuture实例，可以无缝使用Future或者JDK1.8中引入的CompletableFuture提供的方法
        CompletableFuture<String> future2 = new CompletableFuture<>();
        System.out.println("Current state: " + future2.isDone());
        future2.complete("my value");
        System.out.println("Current state: " + future2.isDone());
        System.out.println("Got value: " + future2.get());

        RedisAsyncCommands<String, String> async = connect.async();
        RedisFuture<String> redisFuture = async.ping();
        log.info("Ping result:{}", redisFuture.get());

        RedisFuture<String> future = async.set("name", "throwable", setArgs);
        // CompletableFuture#thenAccept()
        future.thenAccept(value2 -> log.info("Set命令返回:{}", value2));// Set命令返回:OK
        // Future#get()
        future.get();

        CompletableFuture<Void> result =
                (CompletableFuture<Void>) async.set("name", "throwable", setArgs)
                        .thenAcceptBoth(async.get("name"),
                                (s, g) -> {
                                    log.info("Set命令返回:{}", s);// Set命令返回:OK
                                    log.info("Get命令返回:{}", g);// Get命令返回:throwable
                                });
        result.get();

        // 反应式API，RedisReactiveCommands的方法如果返回的结果只包含0或1个元素，那么返回值类型是Mono，如果返回的结果包含0到N（N大于0）个元素，那么返回值是Flux
        RedisReactiveCommands<String, String> reactive = connect.reactive();
        Mono<String> ping = reactive.ping();
        // Ping result: PONG
        ping.subscribe(v -> log.info("Ping result:{}", v));
        Thread.sleep(1000);

        reactive.set("name", "throwable", setArgs).block();
        // Get命令返回: throwable
        reactive.get("name").subscribe(value3 -> log.info("Get命令返回:{}", value3));
        Thread.sleep(1000);

        // block: 阻塞
        reactive.sadd("food", "bread", "meat", "fish").block();
        Flux<String> flux = reactive.smembers("food");
        flux.subscribe(log::info);
        reactive.srem("food", "bread", "meat", "fish").block();
        Thread.sleep(1000);

        // 开启一个事务，先把counter设置为1，再将counter自增1
        reactive.multi().doOnSuccess(r -> {
            reactive.set("counter", "1").doOnNext(log::info).subscribe();
            reactive.incr("counter").doOnNext(c -> log.info(String.valueOf(c))).subscribe();
        }).flatMap(s -> reactive.exec())
                .doOnNext(transactionResult -> log.info("Discarded:{}", transactionResult.wasDiscarded()))
                .subscribe();
        Thread.sleep(1000);

        connect.close();
        redisClient.shutdown();
    }

    /**
     * 自定义命令是Redis命令有限集，不过可以更细粒度指定KEY、ARGV、命令类型、编码解码器和返回值类型，依赖于dispatch()方法
     * 自定义实现ping方法
     *
     * @throws Exception
     */
    public void customPing() {
        RedisURI redisURI = this.createRedisURI();
        RedisClient redisClient = RedisClient.create(redisURI);
        StatefulRedisConnection<String, String> connect = redisClient.connect();
        RedisCommands<String, String> sync = connect.sync();
        RedisCodec<String, String> codec = StringCodec.UTF8;
        String result = sync.dispatch(CommandType.PING, new StatusOutput<>(codec));
        log.info("PING:{}", result);
        connect.close();
        redisClient.shutdown();
    }

    /**
     * 自定义命令是Redis命令有限集，不过可以更细粒度指定KEY、ARGV、命令类型、编码解码器和返回值类型，依赖于dispatch()方法
     * 自定义实现set方法
     *
     * @throws Exception
     */
    public void customSet() throws Exception {
        RedisURI redisURI = this.createRedisURI();
        RedisClient redisClient = RedisClient.create(redisURI);
        StatefulRedisConnection<String, String> connect = redisClient.connect();
        RedisCommands<String, String> sync = connect.sync();
        RedisCodec<String, String> codec = StringCodec.UTF8;
        sync.dispatch(CommandType.SETEX, new StatusOutput<>(codec),
                new CommandArgs<>(codec).addKey("name").add(5).addValue("throwable"));
        String result = sync.get("name");
        log.info("Get value:{}", result);
        connect.close();
        redisClient.shutdown();
    }

    /**
     * 动态命令是基于Redis命令有限集，并且通过注解和动态代理完成一些复杂命令组合的实现。主要注解在io.lettuce.core.dynamic.annotation包路径下
     */
    public interface CustomCommand extends Commands {
        // SET [key] [value]
        @Command("SET ?0 ?1")
        String setKey(String key, String value);

        // SET [key] [value]
        @Command("SET :key :value")
        String setKeyNamed(@Param("key") String key, @Param("value") String value);

        // MGET [key1] [key2]
        @Command("MGET ?0 ?1")
        List<String> mGet(String key1, String key2);

        // 方法名作为命令
        @CommandNaming(strategy = CommandNaming.Strategy.METHOD_NAME)
        String mSet(String key1, String value1, String key2, String value2);
    }

    public void customDynamicSet() {
        RedisURI redisUri = this.createRedisURI();
        RedisClient redisClient = RedisClient.create(redisUri);
        StatefulRedisConnection<String, String> connect = redisClient.connect();
        RedisCommandFactory commandFactory = new RedisCommandFactory(connect);
        CustomCommand commands = commandFactory.getCommands(CustomCommand.class);
        commands.setKey("name", "throwable");
        commands.setKeyNamed("throwable", "doge");
        log.info("MGET ===> " + commands.mGet("name", "throwable"));
        commands.mSet("key1", "value1", "key2", "value2");
        log.info("MGET ===> " + commands.mGet("key1", "key2"));
        connect.close();
        redisClient.shutdown();
    }

    /**
     * 使用连接池，不太建议使用连接池，因为Redis目前处理命令的模块是单线程的
     *
     * @throws Exception
     */
    public void useConnectionPool() throws Exception {
        RedisURI redisUri = this.createRedisURI();
        RedisClient redisClient = RedisClient.create(redisUri);
        GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
        // 同步连接的池化支持需要用ConnectionPoolSupport，异步连接的池化支持需要用AsyncConnectionPoolSupport（Lettuce5.1之后才支持）
        GenericObjectPool<StatefulRedisConnection<String, String>> pool
                = ConnectionPoolSupport.createGenericObjectPool(redisClient::connect, poolConfig);
        try (StatefulRedisConnection<String, String> connection = pool.borrowObject()) {
            RedisCommands<String, String> command = connection.sync();
            SetArgs setArgs = SetArgs.Builder.nx().ex(5);
            command.set("name2", "throwable2", setArgs);
            String n = command.get("name2");
            log.info("Get value:{}", n);
        }
        pool.close();
        redisClient.shutdown();
    }
}
