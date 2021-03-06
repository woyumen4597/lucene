package cn.jrc.crawler;

import cn.jrc.domain.PageInfo;
import cn.jrc.util.IndexUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;


/**
 * @author Created By Jrc
 * @version v.0.1
 * @date 2018/2/18 22:48
 */
public abstract class Crawler {
    public static final Logger LOG = LoggerFactory.getLogger(Crawler.class);
    private static final HttpClientBuilder HTTP_CLIENT_BUILDER;
    private static final CloseableHttpClient CLIENT;
    private static String INDEXDIR = "./indexDir";
    private static final String USER_AGENT = "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; QQDownload 1.7; .NET CLR 1.1.4322; CIBA; .NET CLR 2.0.50727)";

    static {
        System.setProperty("https.protocols", "TLSv1,TLSv1.1,TLSv1.2");
        HTTP_CLIENT_BUILDER = HttpClientBuilder.create();
        HTTP_CLIENT_BUILDER.setConnectionTimeToLive(5, TimeUnit.SECONDS);
        try {
            HTTP_CLIENT_BUILDER.setSSLContext(SSLContexts.custom().build());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
        CLIENT = HTTP_CLIENT_BUILDER.build();
    }

    protected String url;
    protected Document document = null;

    public Crawler(String url) throws IOException {
        this.url = url;
        HttpGet get = new HttpGet(url);
        get.addHeader("User-Agent", USER_AGENT);
        CloseableHttpResponse response = null;
        try {
            response = CLIENT.execute(get);
            int code = response.getStatusLine().getStatusCode();
            if (code == 200) {
                HttpEntity entity = response.getEntity();
                String html = EntityUtils.toString(entity);
                document = Jsoup.parse(html, url);
            } else {
                LOG.error(url + " return code: " + code);
                throw new IOException("Net Error,code = " + code);
            }
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void visit(boolean update) throws IOException {
        if (document != null) {
            if (match(url)) {
                PageInfo pageInfo = handle(document, url);
                index(pageInfo,update);
            }
        } else {
            throw new IOException("Crawl Failed!Document is null!");
        }
    }

    public Set<String> deriveLinks() {
        Set<String> set = new HashSet<>();
        Elements links = document.getElementsByTag("a");
        if (links != null) {
            for (Element link : links) {
                String url = link.absUrl("href");
                if (match(url)) {
                    set.add(url);
                }
            }
        }
        return set;
    }

    /**
     * whether link is match
     * @return true:match,false:not match
     */
    public abstract boolean match(String link);

    /**
     * handle page to return PageInfo object
     *
     * @param document
     * @param url
     * @return pageInfo
     */
    public abstract PageInfo handle(Document document, String url) throws NullPointerException;

    private void index(PageInfo pageInfo, boolean update) {
        try {
            LOG.info("Index Start: " + pageInfo.toString());
            if (update) {
                IndexUtils.update(pageInfo, INDEXDIR);
            } else {
                IndexUtils.index(pageInfo, INDEXDIR);
            }
            LOG.info("Index End: " + pageInfo.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
