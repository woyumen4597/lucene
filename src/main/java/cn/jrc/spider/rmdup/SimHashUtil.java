package cn.jrc.spider.rmdup;


/**
 * @author Created By Jrc
 * @version v.0.1
 * @date 2018/1/24 14:53
 */
public class SimHashUtil {
    public static int hammingDistance(String str1,String str2){
        SimHash hash1 = new SimHash(str1);
        SimHash hash2 = new SimHash(str2);
        return hash1.hammingDistance(hash2);
    }
}
