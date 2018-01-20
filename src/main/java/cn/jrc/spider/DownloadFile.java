package cn.jrc.spider;

import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.*;
import java.util.concurrent.TimeUnit;

/**
 * @author Created By Jrc
 * @version v.0.1
 * @date 2018/1/18 14:22
 */
public class DownloadFile {
    public String getFileNameByUrl(String url,String contentType){
        //remove HTTP
        url = url.substring(7);
        // text/html
        if(contentType.indexOf("html")!=-1){
            url = url.replaceAll("[\\?/:*|<>\"]","_")+".html";
            return url;
        }
        //application/pdf
        else{
            return url.replaceAll("[\\?/:*|<>\"]","_")+"."+contentType.substring(contentType.lastIndexOf("/")+1);
        }
    }


    public void saveToLocal(InputStream inputStream,String filePath){
        try {
            DataOutputStream out = new DataOutputStream(new FileOutputStream(new File(filePath)));
            int len =0;
            byte[] buffer = new byte[1024];
            while((len=inputStream.read(buffer))!=-1){
                out.write(buffer,0,len);
            }
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String downloadFile(String url){
        String filePath = null;
        HttpClientBuilder builder = HttpClientBuilder.create();
        builder.setConnectionTimeToLive(5, TimeUnit.SECONDS);
        CloseableHttpClient client = builder.build();
        HttpGet get = new HttpGet(url);
        try {
            CloseableHttpResponse response = client.execute(get);
            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if(statusCode!= HttpStatus.SC_OK){
                System.err.println("Method failed: "+statusLine);
                filePath = null;
            }

            // handle response
            InputStream content = response.getEntity().getContent();
            filePath = "./files/"+getFileNameByUrl(url,response.getFirstHeader("Content-type").getValue());
            saveToLocal(content,filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return filePath;
    }

    public static void main(String[] args) {
        String s = new DownloadFile().downloadFile("http://www.baidu.com");
        System.out.println(s);
    }
}
