package top.sxuet.jvm;

import org.junit.jupiter.api.Test;

/**
 * 类加载器
 *
 * @author Sxuet
 * @since 2022.03.07 15:20
 */
public class ClassLoadersTest {
  /**
   * 各种类加载器的获取测试
   *
   * <p>vm参数： -XX:+TraceClassLoading 获取类加载顺序过程 <br>
   *
   * <p>1. 引导类加载器 Bootstrap ClassLoader：只加载包名为java、javax、sun开头的类
   *
   * <p>2. 扩展类加载器 PlatForm ClassLoader (ExtClassLoader)
   * 从java.ext.dirs系统属性所指定的目录中加载类库或从jre/lib/ext子目录下加载库
   *
   * <p>3. 应用程序加载器 Application ClassLoader(AppClassLoader)：加载环境变量classpath或系统属性java.class.path
   * 指定路径下的类库，应用程序加载器是系统默认的加载器。
   *
   * <p>4. 用户自定义类加载器:继承ClassLoader
   */
  @Test
  private void getClassLoaders() {}
}
