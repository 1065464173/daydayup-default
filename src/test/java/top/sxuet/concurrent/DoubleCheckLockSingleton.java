package top.sxuet.concurrent;

import org.junit.jupiter.api.Test;

/**
 * @author Sxuet
 * @program daydayup
 * @description **阿里面试题（高频）**：双重检测锁DCL对象半初始化问题
 * @since 2021-11-09 15:03
 */
public class DoubleCheckLockSingleton {
  private static DoubleCheckLockSingleton instance = null;

  private DoubleCheckLockSingleton() {}

  public static DoubleCheckLockSingleton getInstance() {
    /*
     * 双层检测锁 避免多次实例化 但是有所隐患
     * 静态方法的调用会使类进行初始化——执行<init>方法
     */
    if (instance == null) {
      /*
       * 字节码文件对于DoubleCheckLockSingleton类构造方法初始化、成员变量的赋值——<init>方法
       * 21 invokespecial #5 <top/sxuet/concurrent/DoubleCheckLockSingleton. <init>
       *
       * 对static静态变量的赋值
       * 24 putstatic #3 <top/sxuet/concurrent/DoubleCheckLockSingleton. instance>
       *
       * 这两个指令没有依赖关系（不违反happen-before和as-if-serial原则），可能会产生重排，导致类还处于半初始化。
       * （可能性非常低，需要非常高的并发量）
       * 将instance指定为volatile，则不会产生重排
       */
      synchronized (DoubleCheckLockSingleton.class) {
        if (instance == null) {
          instance = new DoubleCheckLockSingleton();
        }
      }
    }
    return instance;
  }

  @Test
  void testMain() {
    DoubleCheckLockSingleton instance = DoubleCheckLockSingleton.getInstance();
  }
}
