package cn.jrc.test;

import cn.jrc.util.IPUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * @author Created By Jrc
 * @version v.0.1
 * @date 2018/2/25 19:09
 */
public class IPTest {
    @Test
    public void testIP() throws IOException {
        Document document = Jsoup.parse(new File("./files/ip.html"), "gbk");
        String text = document.body().text();
        String[] strings = text.split(" ");
        HashMap<String,Integer> map = new HashMap<>();
        for (String string : strings) {
            if(string.matches(".*:[0-9]+")) {
                String[] split = string.split(":");
                map.put(split[0],Integer.parseInt(split[1])); //ip:port
            }
        }
        System.out.println(map);
    }

    @Test
    public void testIpUtils(){
        ArrayList<String> ips = IPUtils.getIps();
        for (String ip : ips) {
            System.out.println(ip);
        }

    }



}
