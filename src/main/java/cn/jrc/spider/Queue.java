package cn.jrc.spider;

import java.util.LinkedList;

/**
 * @author Created By Jrc
 * @version v.0.1
 * @date 2018/1/18 13:34
 */
public class Queue {
    private LinkedList queue = new LinkedList();

    public void enQueue(Object t){
            queue.addLast(t);
    }

    public Object deQueue(){
        return queue.removeFirst();
    }

    public boolean isQueueEmpty(){
        return queue.isEmpty();
    }

    public boolean contains(Object t){
        return queue.contains(t);
    }

    public boolean empty(){
        return queue.isEmpty();
    }


}
