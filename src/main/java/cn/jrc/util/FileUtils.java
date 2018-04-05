package cn.jrc.util;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.*;

/**
 * Created by Lucas.Jin on 2018/3/15.
 */
public class FileUtils {
    public static Logger LOGGER = LoggerFactory.getLogger(FileUtils.class);


    /**
     * 读取文件的每一行到集合中
     *
     * @param filePath
     * @return
     */
    public static List<String> readFromFile(String filePath) {
        List<String> list = new ArrayList<String>();
        try {
            FileReader reader = new FileReader(filePath);
            BufferedReader br = new BufferedReader(reader);
            String str = null;
            while ((str = br.readLine()) != null) {
                if (!str.startsWith("#"))
                    list.add(str);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 获得文件中的url列表(去重)
     *
     * @param filePath
     * @return
     */
    public static Set<String> getUrlSetFromFile(String filePath) {
        Set<String> set = new HashSet<String>();
        try {
            FileReader reader = new FileReader(filePath);
            BufferedReader br = new BufferedReader(reader);
            String str = null;
            while ((str = br.readLine()) != null) {
                set.add(str);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return set;
    }


    public static void convertListToQueue(Collection collection, Queue queue) {
        for (Object o : collection) {
            queue.add(o);
        }
    }
}
