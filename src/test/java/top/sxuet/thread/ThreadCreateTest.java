package top.sxuet.thread;

import com.sun.istack.internal.NotNull;
import com.sun.tools.javac.util.Assert;
import org.junit.jupiter.api.Test;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 创建线程的几种方式
 *
 * @author Sxuet
 * @since 2022.03.22 14:24
 */
public class ThreadCreateTest {
  /**
   * 方式一，继承Thread方法，调用start
   *
   * @see java.lang.Thread
   */
  @Test
  void createByThread() {
    // 创建Thread匿名子类
    Thread thread =
        new Thread() {
          @Override
          public void run() {
            System.out.println(Thread.currentThread().getName());
          }
        };
    // start启动线程，直接调用run方法不会启动线程
    thread.run();
    thread.start();
  }

  /**
   * 方式二，继承Runnable方法<br>
   * 相比与继承Thread类，更推荐实现Runnable，因为Java是单继承多实现的。减少了局限性，并钱实现的方式更适合多个线程共享数据的情况
   *
   * @see java.lang.Runnable
   */
  @Test
  void createByRunnable() {
    RunnableClass runnableClass = new RunnableClass();
    new Thread(runnableClass).start();
  }
  /**
   * 方式三，继承Callable方法<br>
   * 和Runnable相比，更推荐Callable，可以抛出异常，以及可以有返回值
   *
   * @see java.util.concurrent.FutureTask
   * @see java.util.concurrent.Callable
   */
  @Test
  void createByCallable() throws ExecutionException, InterruptedException {
    CallableClass callableClass = new CallableClass();
    // 需要借助FutureTask来完成callableClass的多线程创建
    // FutureTask本质还是实现了Runnable，格外还实现了Future
    FutureTask<String> futureTask = new FutureTask<>(callableClass);
    // 启动线程
    new Thread(futureTask).start();
    // 获取返回值
    System.out.println(futureTask.get());
  }

  /**
   * 方式四，线程池创建线程 - 最推荐也是最常用的，减少了线程创建的内存消耗 <hr>
   *
   * <p>【自动创建线程池-不推荐】，原因是创建时参数默认是Integer.MAX_VALUE，线程过多时会OOM。Java通过Executors提供四种线程池分别为：<br>
   *
   * <ul>
   *   <li>【newCachedThreadPool-可缓存线程池】，如果线程池长度超过处理需要，可灵活回收空闲线程，若无可回收，则新建线程。（线程最大并发数不可控制）<br>
   *   <li>【newFixedThreadPool-定长线程池】，可控制线程最大并发数，超出的线程会在队列中等待。<br>
   *   <li>【newScheduledThreadPool-定长线程池】，支持定时及周期性任务执行。<br>
   *   <li>【newSingleThreadExecutor-单线程化线程池】，它只会用唯一的工作线程来执行任务，保证所有任务按照指定顺序(FIFO, LIFO, 优先级)执行。
   * </ul>
   *
   * <hr>
   *
   * <p>【手动创建线程池 - 推荐】通过ThreadPoolExecutor创建，步骤如下：
   *
   * <ul>
   *   <li>【指定命名的线程池工厂】：主要功能是为线程指定统一的名称前缀，这样方便出错时检索出是哪个模块的线程
   *   <li>【手动创建线程池】：指定并发数，最大池容量，活跃时间，排队队列结构等等
   * </ul>
   *
   * @see java.util.concurrent.Executors
   * @see java.util.concurrent.ThreadFactory
   */
  @Test
  void createByThreadPool() {

    // 注意：阿里开发手册不建议使用Executors创建线程，原因是参数默认是Integer.MAX_VALUE，线程过多时会OOM
    // ThreadPoolExecutor pool = (ThreadPoolExecutor) Executors.newFixedThreadPool(10);

    // 指定线程池工厂
    ThreadFactory threadFactory = new NamedThreadFactory("ThreadCreateTest-");
    // 手动创建线程池
    ThreadPoolExecutor pool =
        new ThreadPoolExecutor(
            10, 200, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(1024), threadFactory);

    /* 设置线程池属性 */
    // 核心池大小
    pool.setCorePoolSize(15);
    // 重新设置最大并发数
    pool.setMaximumPoolSize(15);
    // 适用于Runnable
    pool.execute(new RunnableClass());
    // 适用Callable
    pool.submit(new CallableClass()); // 适用Callable
    pool.submit(new CallableClass()); // 适用Callable
    pool.submit(new CallableClass());
    // 关闭线程池
    pool.shutdown();
  }

  /** 创建定时线程演示 */
  @Test
  public void createByScheduledPool() throws InterruptedException {
    // 创建拥有固定线程数量的定时线程任务的线程池
    ScheduledExecutorService pool = Executors.newScheduledThreadPool(5);
    System.out.println("时间：" + System.currentTimeMillis());
    // 延时xx时间执行
    // pool.schedule(new RunnableClass(), 2, TimeUnit.SECONDS);
    // 在一次调用完成和下一次调用开始之间有长度为delay的延迟，单位为TimeUnit枚举时间单位
    pool.scheduleWithFixedDelay(new RunnableClass(), 0, 1, TimeUnit.SECONDS);
    if (pool.awaitTermination(7, TimeUnit.SECONDS)) {
      pool.shutdown();
    }
    System.out.println("结束时间：" + System.currentTimeMillis());
  }
}

/** 一个实现Runnable的类，测试线程创建 */
class RunnableClass implements Runnable {
  @Override
  public void run() {
    System.out.println(Thread.currentThread().getName());
    System.out.println("This is a runnable thread");
  }
}

/**
 * 一个实现Callable的类，测试线程创建<br>
 * 和Runnable相比，可以抛出异常，以及可以有返回值
 */
class CallableClass implements Callable<String> {

  @Override
  public String call() {
    System.out.println(Thread.currentThread().getName());
    return "This is a  callable thread";
  }
}

/** 自我实现的ThreadFactory，用于手动创建线程池，并且为线程池的线程命名，因此可以区分是哪个模块的线程 */
class NamedThreadFactory implements ThreadFactory {
  private final AtomicInteger id = new AtomicInteger(0);

  private final String name;

  public NamedThreadFactory(String name) {
    this.name = name;
  }

  @Override
  public Thread newThread(@NotNull Runnable r) {
    Assert.checkNonNull(r);
    String threadName = name + id.getAndIncrement();
    Thread thread = new Thread(r, threadName);
    thread.setDaemon(true);
    return thread;
  }
}
