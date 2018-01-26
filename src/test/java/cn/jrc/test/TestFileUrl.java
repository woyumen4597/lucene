package cn.jrc.test;

import java.io.File;
import java.io.FileFilter;

/**
 * @author Created By Jrc
 * @version v.0.1
 * @date 2018/1/26 17:27
 */
public class TestFileUrl {
    public static void main(String[] args) {
        File file = new File("./files");
        File[] files = file.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                if (pathname.getPath().endsWith(".html")) {
                    return true;
                }
                return false;
            }
        });
        for (File file1 : files) {
            System.out.println(file1.getName());
           // System.out.println(file1.getPath());
        }
    }
}
