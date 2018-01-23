package cn.jrc.spider.rmdup;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Created By Jrc
 * @version v.0.1
 * @date 2018/1/23 19:28
 */
public class PageDistance<E>  {
    private static int num[][] = null;
    public static <E> List<E> LCSDisplay(E[] s1,E[] s2){
        LCS(s1,s2);
        int s1position = s1.length,s2position = s2.length;
        List<E> result = new LinkedList<>();
        while(s1position!=0&&s2position!=0){
            if(s1[s1position-1].equals(s2[s2position-1])){
                result.add(s1[s1position-1]);
                s1position--;
                s2position--;
            }else if(num[s1position][s2position-1]>=num[s1position-1][s2position]){
                s2position--;
            }else{
                s1position--;
            }
        }
        Collections.reverse(result);
        return result;
    }

    public static <E> int LCS(E[] s1,E[] s2){
        num = new int[s1.length+1][s2.length+1];
        //dynamic programming
        for (int i = 1; i <= s1.length; i++) {
            for (int j = 1; j <= s2.length ; j++) {
                if(s1[i-1].equals(s2[j-1])){
                    num[i][j] = 1+num[i-1][j-1];
                }else{
                    num[i][j] = Math.max(num[i-1][j],num[i][j-1]);
                }
            }
        }
        return num[s1.length][s2.length];
    }


    public static void main(String[] args) {
        Character[] s1 =new Character[]{'a','b','c','b','d','a','b'};
        Character[] s2 = new Character[]{'b','d','c','a','b','a'};
        List<Character> lcs = LCSDisplay(s1,s2);
        for (Character lc : lcs) {
            System.out.println(lc);
        }
    }
}
