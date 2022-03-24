package io;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;

/**
 * 获取指定文件的绝对路径
 *
 * @author Sxuet
 * @since 2022.03.18 16:54
 */
public class FileAbsolutePath {

  ArrayList<String> getFileAbsolutePathThroughFilter(String pathName) {
    File file = new File(pathName);
    FileFilter filter = pathname -> pathname.getName().contains(".pem");
    File[] files = file.listFiles(filter);
    ArrayList<String> res = new ArrayList<>();
    assert files != null;
    for (File f : files) {
      res.add(f.getAbsolutePath());
    }
    return res;
  }

  public static void main(String[] args) {
    String dirPath = "/Users/sxuet/Documents/workplace";
    FileAbsolutePath fileAbsolutePath = new FileAbsolutePath();
    ArrayList<String> res = fileAbsolutePath.getFileAbsolutePathThroughFilter(dirPath);
    System.out.println(res);
  }
}
