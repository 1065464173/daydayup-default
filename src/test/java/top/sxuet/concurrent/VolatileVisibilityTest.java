package top.sxuet.concurrent;

import org.junit.jupiter.api.Test;

/**
 * @author Sxuet
 * @program daydayup
 * @description volatile在jmm中解决不同线程对共享数据的可见性
 * @since 2021-11-09 13:22
 */
public class VolatileVisibilityTest {
  /*
   * volatile中的缓存一致性协议：当数据发生变化的时候会立即刷新到内存。<br>
   * 每个cpu通过总线嗅探机制可以感知到数据的变化从而将自己缓存里的数据失效
   *
   * 底层实现主要是通过汇编lock前缀指令，它会锁定这块内存区域的缓存（缓存行锁定）并回写到主内存
   * 打印汇编JVM参数 -server -Xcomp -XX:+UnlockDiagnosticVMOptions -XX:+PrintAssembly -XX:CompileCommand=compileonly,*VolatileVisibility.prepareData
   */
  volatile boolean initFlag = false;

  @Test
  public void printData() throws InterruptedException {
    new Thread(this::run).start();
    Thread.sleep(1000);
    new Thread(this::prepareData).start();
  }

  void prepareData() {
    System.out.println("prepare data..");
    initFlag = true;
    // TODO
    System.out.println("prepare data end..");
  }

  private void run() {
    System.out.println("waiting data..");
    while (!initFlag) {}
    System.out.println("success");
  }
}
