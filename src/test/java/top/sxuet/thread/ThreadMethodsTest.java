package top.sxuet.thread;

import org.junit.jupiter.api.Test;

/**
 * 线程常用方法测试
 *
 * @author Sxuet
 * @since 2022.03.24 17:18
 */
public class ThreadMethodsTest {
  /** 测试方法 */
  @Test
  void methodsTest() {
    // currentThread() 返回执行当前代码线程
    Thread thread = new Thread(() -> System.out.println(Thread.currentThread().getName()));
    // 不启动线程，执行线程的run方法
    thread.run();
    // 设置线程优先级
    System.out.println(thread.getPriority());
    // 更换线程名
    thread.setName("sxuet's thread");
    // start启动线程，执行线程的run方法
    thread.start();
    // isAlive()线程是否还存活
    System.out.println(thread.isAlive());
  }
}
