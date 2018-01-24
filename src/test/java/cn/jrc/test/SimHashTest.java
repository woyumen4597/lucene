package cn.jrc.test;

import cn.jrc.spider.rmdup.SimHashUtil;

/**
 * @author Created By Jrc
 * @version v.0.1
 * @date 2018/1/24 15:03
 */
public class SimHashTest {
    public static void main(String[] args) {
        String s1 = "this is a text";
        String s2 = "this is a test";
        int distance = SimHashUtil.hammingDistance(s1, s2);
        System.out.println(distance);
    }
}
