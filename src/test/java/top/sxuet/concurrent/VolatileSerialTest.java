package top.sxuet.concurrent;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Sxuet
 * @program daydayup
 * @description volatile 有序性
 * @since 2021-11-09 14:08
 */
public class VolatileSerialTest {
  int x = 0, y = 0, a = 0, b = 0;

  @Test
  void volatileSerial() throws InterruptedException {
    Set<String> resultSet = new HashSet<>();
    for (int i = 0; i < 100000; i++) {
      // 恢复初值
      x = y = a = b = 0;
      // java标准规定单线程内会重排序，无法跨线程重排
      /*
       * 正常情况 [a=1,b=0, a=0,b=1]
       * 极端情况 [a=1,b=0, a=0,b=1, a=0,b=0 ,a=1,b=1] 单线程内的执行顺序发生重排
       *  两行代码没有任何依赖关系，可能产生指令重排以优化性能（两个代码的执行顺序可能会变化）
       */
      Thread one =
          new Thread(
              () -> {
                a = y;
                x = 1;
              });
      Thread other =
          new Thread(
              () -> {
                b = x;
                y = 1;
              });
      one.start();
      other.start();
      one.join();
      other.join();
      resultSet.add("a=" + a + "," + "b=" + b);
      System.out.println(resultSet);
    }
  }
}
