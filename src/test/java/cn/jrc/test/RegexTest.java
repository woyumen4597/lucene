package cn.jrc.test;

import org.junit.Assert;
import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Created By Jrc
 * @version v.0.1
 * @date 2018/1/23 14:31
 */
public class RegexTest {
    @Test
    public void test1(){
        Pattern pattern = Pattern.compile("^Java.*");
        Matcher matcher = pattern.matcher("Java是一门编程语言");
        boolean b = matcher.matches();
        assert b==true;
    }

    @Test
    public void test2(){
        Pattern pattern = Pattern.compile("[, |]+");
        String[] strs = pattern.split("Java Hello World  Java Hello,,World|Sun");
        for (int i = 0; i < strs.length; i++) {
            System.out.println(strs[i]);
        }
    }

    @Test
    public void test3(){
        Pattern pattern = Pattern.compile("正则表达式");
        Matcher matcher = pattern.matcher("正则表达式 Hello world 正则表达式 Hello world");
        System.out.println(matcher.replaceFirst("Java"));
        System.out.println(matcher.replaceAll("Java"));
    }

    @Test
    public void test4(){
        Pattern pattern = Pattern.compile("正则表达式");
        Matcher matcher = pattern.matcher("正则表达式 Hello world 正则表达式 Hello world");
        StringBuffer sb = new StringBuffer();
        while(matcher.find()){
            matcher.appendReplacement(sb,"Java");
        }
        matcher.appendTail(sb);
        System.out.println(sb.toString());
    }

    /**
     * validate if is email address
     */
    @Test
    public void test5(){
        String str = "w-oyumen4597@gmai-l.com";
        Pattern pattern = Pattern.compile("[\\w\\.\\-]+@([\\w\\-]+\\.)+[\\w\\-]+",Pattern.CASE_INSENSITIVE);
        Matcher matcher =  pattern.matcher(str);
        Assert.assertTrue(matcher.matches());
    }

    /**
     * remove html tag
     */
    @Test
    public void test6(){
        Pattern pattern = Pattern.compile("<.+?>", Pattern.DOTALL);
        Matcher matcher = pattern.matcher("<a href=\"index.html\">主页</a>");
        String string = matcher.replaceAll("");
        System.out.println(string);
    }

    /**
     * cut out http:// address
     */
    @Test
    public  void test7(){
        Pattern pattern = Pattern.compile("href=\"(.+?)\"");
        Matcher matcher = pattern.matcher("<a href=\"index.html\">主页</a>");
        if(matcher.find()){
            System.out.println(matcher.group(1));
        }
    }










}
