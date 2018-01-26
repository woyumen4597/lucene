package cn.jrc.spider;

import org.htmlparser.Parser;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;
import org.htmlparser.visitors.HtmlPage;

import java.io.*;
import java.util.*;

/**
 * @author Created By Jrc
 * @version v.0.1
 */
public class PageRank {
    /* 阈值 */
    public static double MAX = 0.0000001;
    //阻尼系数
    public static double decayFactor = 0.85;

    public static String htmldoc = "./files";
    public static Map<String,HtmlEntity> map = new HashMap<>();
    public static List<HtmlEntity> list = new ArrayList<>();

    public static double[] init;
    public static double[] pr;

    public static void main(String[] args) throws IOException, ParserException {
        loadhtml();
        pr = doPageRank();
        while(!(checkMax())){
            System.arraycopy(pr,0,init,0,init.length);
            pr = doPageRank();
        }
        for (int i = 0; i < pr.length; i++) {
            HtmlEntity he = list.get(i);
            he.setPr(pr[i]);
        }
        List<HtmlEntity> finalList = new ArrayList<>();
        Collections.sort(list, new Comparator<HtmlEntity>() {
            @Override
            public int compare(HtmlEntity o1, HtmlEntity o2) {
                int em = 0;
                if(o1.getPr()>o2.getPr()){
                    em = -1;
                }else{
                    em = 1;
                }
                return em;
            }
        });
        for (HtmlEntity entity : list) {
            System.out.println(entity.getPath()+" : "+entity.getPr());

        }

    }

    /**
     * validate if reach the max
     * @return
     */
    private static boolean checkMax() {
        boolean flag = true;
        for (int i = 0; i < pr.length; i++) {
            if(Math.abs(pr[i]-init[i])>MAX){
                flag = false;
                break;
            }
        }
        return flag;
    }

    /**
     * calculate pagerank
     * @return
     */
    private static double[] doPageRank() {
        double pr[] = new double[init.length];
        for (int i = 0; i < init.length; i++) {
            double temp = 0;
            HtmlEntity entity = list.get(i);
            for (int j = 0; j < init.length; j++) {
                HtmlEntity entity1 = list.get(j);
                if(i!=j&&entity1.getOutLinks().size()!=0&&entity1.getOutLinks().contains(entity.getPath())){
                    temp = temp + init[j]/entity1.getOutLinks().size();
                }

            }
            pr[i] = (1-decayFactor)+decayFactor*temp;
        }
        return pr;
    }

    private static void loadhtml() throws IOException, ParserException {
        File file = new File(htmldoc);
        File[] htmlfiles = file.listFiles(new FileFilter() {

            public boolean accept(File pathname) {
                if (pathname.getPath().endsWith(".html")) {
                    return true;
                }
                return false;
            }

        });
        init = new double[htmlfiles.length];
        for (int i = 0; i < htmlfiles.length; i++) {
            File f = htmlfiles[i];
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    new FileInputStream(f)));
            String line = br.readLine();
            StringBuffer html = new StringBuffer();
            while (line != null) {
                line = br.readLine();
                html.append(line);
            }
            HtmlEntity he = new HtmlEntity();
            he.setPath(f.getName());
            he.setContent(html.toString());
            Parser parser = Parser.createParser(html.toString(), "UTF-8");
            HtmlPage page = new HtmlPage(parser);
            parser.visitAllNodesWith(page);
            NodeList nodelist = page.getBody();
            nodelist = nodelist.extractAllNodesThatMatch(
                    new TagNameFilter("A"), true);
            for (int j = 0; j < nodelist.size(); j++) {
                LinkTag outlink = (LinkTag) nodelist.elementAt(j);
                he.getOutLinks().add(outlink.getAttribute("href"));
            }

            map.put(he.getPath(), he);
            list.add(he);
            init[i] = 1.0;

        }
        for (int i = 0; i < list.size(); i++) {
            HtmlEntity he = list.get(i);
            List<String> outlink = he.getOutLinks();
            for (String ol : outlink) {
                HtmlEntity he0 = map.get(ol);
                he0.getInLinks().add(he.getPath());
            }
        }

    }


}
