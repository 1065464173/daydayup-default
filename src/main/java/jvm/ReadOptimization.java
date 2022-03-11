package jvm;

/**
 * 内存编译器优化的读优化可以优化访问速度，但在多线程的模式下可能会造成一些问题
 *
 * @author Sxuet
 * @since 2022.03.11 10:54
 */
public class ReadOptimization {

  /**
   * 由于访问内存和访问寄存器的开销完全不一样的，所以一般单线程情况下static变量a会被优化成局部变量，这样就可以直接寄存器分配，不需要访问内存 <br>
   * 这里如果不加sync或者fence，编译器会把a优化成局部变量，减少内存读取，达到高性能的目的，凡事在多线程模式下<br>
   * 有时候需要禁止编译器的【读优化】以防止全局变量变成局部变量而造成的的死循环
   *
   * <p>volatile 和 loadFence 都可以有效防止编译器读优化，那么问题来了，哪种方式开销更大？
   */

  // volatile static int a = 1;
  static int a = 1;

  public static void main(String[] args) throws InterruptedException {
    new Thread(
            () -> {
              while (a == 1) {
                // synchronized (ReadOptimization.class) {}
                // Unsafe.getUnsafe().loadFence();
              }
              System.out.println("end");
            })
        .start();
    Thread.sleep(1000);
    a = 2;
  }
}
