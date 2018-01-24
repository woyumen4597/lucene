package cn.jrc.test;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author Created By Jrc
 * @version v.0.1
 * @date 2018/1/24 16:02
 */
public class TestURL {
    @Test
    public void test1() throws MalformedURLException {
        URL url  = new URL("http://www.baidu.com/index.html");
        System.out.println(url.getHost());
        System.out.println(url.getPath());
        System.out.println(url.getProtocol());
    }

    @Test
    public void test2() throws IOException {
        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet get = new HttpGet("http://www.baidu.com");
        CloseableHttpResponse response = client.execute(get);
        HttpEntity entity = response.getEntity();
        if (entity != null) {
            System.out.println(EntityUtils.toString(entity,"UTF-8"));
            EntityUtils.consume(entity); //close
        }
        client.close();
    }

}
