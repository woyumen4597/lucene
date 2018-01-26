package cn.jrc.spider;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author Created By Jrc
 * @version v.0.1
 * @date 2018/1/26 14:31
 */
public class NIODownloader {
    public static Selector selector = null;
    public static Map<SocketChannel,String> sc2Path = new HashMap<>();
    public static void setConnect(String ip,String path,int port){
        try {
            SocketChannel client = SocketChannel.open();
            client.configureBlocking(false);
            client.connect(new InetSocketAddress(ip,port));
            client.register(selector, SelectionKey.OP_CONNECT|SelectionKey.OP_READ|SelectionKey.OP_WRITE);
            sc2Path.put(client,path);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(0);
        }

    }

    public static void main(String[] args) {
        try {
            selector = Selector.open();
            setConnect("www.baidu.com","/index.html",80);
            //setConnect("www.bilibili.com","/av18710335",443);
            while(!selector.keys().isEmpty()){
                if(selector.select(100)>0){
                    Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                    while(iterator.hasNext()){
                        SelectionKey key = iterator.next();
                        iterator.remove();
                        try {
                            processSelectionKey(key);
                        }catch (IOException e){
                            key.cancel();
                        }

                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public  static void processSelectionKey(SelectionKey key) throws IOException{
        SocketChannel socketChannel = (SocketChannel) key.channel();
        if(key.isValid()&&key.isConnectable()){
            boolean success = socketChannel.finishConnect();
            if(!success){
                key.cancel();
            }
            sendMessage(socketChannel,"GET "+sc2Path.get(socketChannel)+" HTTP/1.0\r\nAccept: */*\r\n\r\n");
        }else if(key.isReadable()){
            String ret = readMessage(socketChannel);
            if(ret!=null&&ret.length()>0){
                System.out.println(ret);
            }else{
                key.cancel();
            }
        }
    }
    //download page
    public static String readMessage(SocketChannel client) {
        String result = null;
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        try {
            int i = client.read(buffer);
            buffer.flip();
            if(i!=-1){
                result = new String(buffer.array(),0,i);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static boolean sendMessage(SocketChannel client, String msg) {
        try {
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            buffer = ByteBuffer.wrap(msg.getBytes());
            client.write(buffer);
        } catch (IOException e) {
            return true;
        }
        return false;

    }


}
