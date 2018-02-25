package cn.jrc.util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * @author Created By Jrc
 * @version v.0.1
 * @date 2018/2/25 19:37
 */
public class IPUtils {
    public static String apiUrl = "http://www.89ip.cn/apijk/?&tqsl=30&sxa=&sxb=&tta=&ports=&ktip=&cf=1";

    public static ArrayList<String> getIps() {
        try {
            URL url = new URL(apiUrl);
            Document document = Jsoup.parse(url, 1000);
            String text = document.body().text();
            String[] strings = text.split(" ");
            ArrayList<String> list = new ArrayList<>();
            for (String string : strings) {
                if(string.matches(".*:[0-9]+")) {
                    list.add(string);
                }
            }
            return list;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
