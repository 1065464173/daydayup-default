package jvm;

import java.io.FileNotFoundException;

/**
 * 自定义类加载器
 *
 * @author Sxuet
 * @since 2022.03.26 15:44
 */
public class CustomClassLoader extends ClassLoader {
  @Override
  protected Class<?> findClass(String name) throws ClassNotFoundException {

    // 双亲委派机制
    try {
      byte[] result = getClassFromCustomPath(name);
      if (result == null) {
        throw new FileNotFoundException();
      } else {
        return defineClass(name, result, 0, result.length);
      }
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }

    throw new ClassNotFoundException(name);
  }

  private byte[] getClassFromCustomPath(String name) {
    // 从自定义路径中加载指定类:细节略
    // 如果指定路径的字节码文件进行了加密，则需要在此方法中进行解密操作。
    return null;
  }
}
