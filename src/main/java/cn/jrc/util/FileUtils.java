package cn.jrc.util;



import java.io.*;
import java.util.*;

/**
 * Created by Lucas.Jin on 2018/3/15.
 */
public class FileUtils {
    /**
     * read every line in file to list
     *
     * @param filePath
     * @return
     */
    public static List<String> readFromFile(String filePath) {
        List<String> list = new ArrayList<String>();
        try {
            FileReader reader = new FileReader(filePath);
            BufferedReader br = new BufferedReader(reader);
            String str;
            while ((str = br.readLine()) != null) {
                if (!str.startsWith("#"))
                    list.add(str);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

}
