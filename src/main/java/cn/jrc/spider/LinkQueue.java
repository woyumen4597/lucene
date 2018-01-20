package cn.jrc.spider;

import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Set;

/**
 * @author Created By Jrc
 * @version v.0.1
 * @date 2018/1/18 13:38
 */
public class LinkQueue {
    private static Set visitedUrl = new HashSet();
    private static PriorityQueue unVisitedUrl = new PriorityQueue<>();

    public static PriorityQueue getUnVisitedUrl() {
        return unVisitedUrl;
    }

    public static void addVisitedUrl(String url){
        visitedUrl.add(url);
    }

    public static void removeVisitedUrl(String url){
        visitedUrl.remove(url);
    }

    public static Object unVisitedUrlDequeue(){
        return unVisitedUrl.poll();
    }

    public static void addUnvisitedUrl(String url){
        if(url!=null&&!url.trim().equals("")
                &&!visitedUrl.contains(url)
                &&!unVisitedUrl.contains(url)){
            unVisitedUrl.add(url);
        }
    }

    public static int getVisitedUrlNum(){
        return visitedUrl.size();
    }

    public static boolean unVisitedUrlsEmpty(){
        return unVisitedUrl.isEmpty();
    }






}
