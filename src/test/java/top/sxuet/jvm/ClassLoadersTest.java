package top.sxuet.jvm;

import org.junit.jupiter.api.Test;
import org.openjsse.sun.security.util.CurveDB;

import java.net.URL;
import java.security.Provider;

/**
 * 类加载器，获取各种类加载器以及打印类加载器的加载路径测试
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
   * <ul>
   *   <li>引导类加载器 Bootstrap ClassLoader：只加载包名为java、javax、sun开头的类
   *   <li>扩展类加载器 PlatForm ClassLoader (ExtClassLoader)
   *       从java.ext.dirs系统属性所指定的目录中加载类库或从jre/lib/ext子目录下加载库
   *   <li>应用程序加载器 Application ClassLoader(AppClassLoader)：加载环境变量classpath或系统属性java.class.path
   *       指定路径下的类库，应用程序加载器是系统默认的加载器。
   *   <li>用户自定义类加载器:继承ClassLoader
   * </ul>
   *
   * @see ClassLoader#getSystemClassLoader
   * @see ClassLoader#getParent
   */
  @Test
  void getClassLoaders() {
    // 获取系统类加载器 -  sun.misc.Launcher$AppClassLoader@18b4aac2
    ClassLoader systemClassLoader = ClassLoader.getSystemClassLoader();
    System.out.println(systemClassLoader);

    // 获取系统类加载器上层：扩展类加载器 - sun.misc.Launcher$ExtClassLoader@2f92e0f4
    ClassLoader extClassLoader = systemClassLoader.getParent();
    System.out.println(extClassLoader);

    // 获取引导类加载器 - null 获取不到
    ClassLoader bootstrapClassLoader = extClassLoader.getParent();
    System.out.println(bootstrapClassLoader); //

    // 获取用户自定类加载，默认使用系统类进行加载 - sun.misc.Launcher$AppClassLoader@18b4aac2
    ClassLoader classLoader = ClassLoadersTest.class.getClassLoader();
    System.out.println(classLoader);

    // 获取线程像下文的ClassLoader - sun.misc.Launcher$AppClassLoader@18b4aac2
    // 就是app类加载器
    ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
    System.out.println(contextClassLoader);

    // String类是使用引导类加载的 - null 获取不到
    // Java核心类库都是使用引导类加载的
    ClassLoader stringClassLoader = String.class.getClassLoader();
    System.out.println(stringClassLoader);
  }

  /** 获取类加载器的加载路径 */
  @Test
  void printLoaderLoadPaths() {
    // 获取BootstrapClassLoader能够加载的api的路径
    URL[] urLs = sun.misc.Launcher.getBootstrapClassPath().getURLs();
    for (URL element : urLs) {
      System.out.println(element.toExternalForm());
    }
    // 从上面的路径中随意选择一个类,来看看他的类加载器是什么:引导类加载器
    // 结果是无法获取 null
    ClassLoader classLoader = Provider.class.getClassLoader();
    System.out.println("Provider's classLoader is " + classLoader);

    // 获取扩展类加载器的加载路径
    String extDirs = System.getProperty("java.ext.dirs");
    for (String path : extDirs.split(";")) {
      System.out.println(path);
    }
    // 从上面的路径中随意选择一个类,来看看他的类加载器是什么:扩展类加载器
    // 结果 sun.misc.Launcher$ExtClassLoader@2f92e0f4
    ClassLoader classLoader1 = CurveDB.class.getClassLoader();
    System.out.println(classLoader1);
  }
}
