package cn.jrc.util;

import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * @author Created By Jrc
 * @version v.0.1
 * @date 2018/4/5 14:20
 */
public class CollectorUtils {
    /**
     * convert list to queue
     * @param list
     * @param queue
     */
    public static void convertListToQueue(List list, LinkedBlockingDeque queue) {
        for (Object o : list) {
            try {
                queue.put(o);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
