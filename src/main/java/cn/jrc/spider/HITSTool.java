package cn.jrc.spider;

import java.io.*;
import java.util.ArrayList;

/**
 * @author Created By Jrc
 * @version v.0.1
 * @date 2018/1/26 18:27
 */
public class HITSTool {
    private String filePath;
    private int pageNum;
    //网页权威值
    private double[] authority;
    //网页hub中心值
    private double[] hub;

    private int[][] linkMatrix;
    //网页种类
    private ArrayList<String> pageClass;

    public HITSTool(String filePath) throws IOException {
        this.filePath = filePath;
        readDataFile();
    }

    private void readDataFile() throws IOException {
        File file = new File(filePath);
        ArrayList<String[]> dataArray = new ArrayList<>();
        BufferedReader in = new BufferedReader(new FileReader(file));
        String str;
        String[] tempArray;
        while ((str = in.readLine()) != null) {
            tempArray = str.split(" ");
            dataArray.add(tempArray);
        }
        in.close();
        pageClass = new ArrayList<>();
        //统计网页类型种数
        for (String[] array : dataArray) {
            for (String s : array) {
                if (!pageClass.contains(s)) {
                    pageClass.add(s);
                }
            }

        }
        int i = 0;
        int j = 0;
        pageNum = pageClass.size();
        linkMatrix = new int[pageNum][pageNum];
        authority = new double[pageNum];
        hub = new double[pageNum];

        for (int k = 0; k < pageNum; k++) {
            authority[k] = 1;
            hub[k] = 1;
        }

        for (String[] array : dataArray) {
            i = Integer.parseInt(array[0]);
            j = Integer.parseInt(array[1]);
            linkMatrix[i - 1][j - 1] = 1;
        }
    }

    public void printResultPage(){
        double maxHub = 0;
        double maxAuthority = 0;
        int maxAuthorityIndex = 0;
        double error = Integer.MAX_VALUE;
        double[] newHub = new double[pageNum];
        double[] newAuthority = new double[pageNum];

        while(error>0.01*pageNum){
            for (int k = 0; k < pageNum; k++) {
                newHub[k] = 0;
                newAuthority[k] = 0;
            }
            //hub和authority的更新计算
            for (int i = 0; i < pageNum; i++) {
                for (int j = 0; j < pageNum; j++) {
                    if (linkMatrix[i][j] == 1) {
                        newHub[i] += authority[j];
                        newAuthority[j] += hub[i];
                    }
                }

            }
            maxHub = 0;
            maxAuthority = 0;
            for (int k = 0; k < pageNum; k++) {
                if (newHub[k] > maxHub) {
                    maxHub = newHub[k];
                }

                if (newAuthority[k] > maxAuthority) {
                    maxAuthority = newAuthority[k];
                    maxAuthorityIndex = k;
                }
            }
            error = 0;
            //归一化处理
            for (int k = 0; k < pageNum; k++) {
                newHub[k] /= maxHub;
                newAuthority[k] /= maxAuthority;
                error += Math.abs(newHub[k]-hub[k]);
                System.out.println(newAuthority[k]+":"+newHub[k]);
                hub[k] = newHub[k];
                authority[k] = newAuthority[k];
            }
            System.out.println("-----------");
        }
        System.out.println("----最终收敛的网页的权威值和中心值");
        for (int k = 0; k < pageNum; k++) {
            System.out.println("网页" + pageClass.get(k) + ":" + authority[k] + ":" + hub[k]);
        }
        System.out.println("权威值最高的网页为：网页" + pageClass.get(maxAuthorityIndex));

    }

    public static void main(String[] args) throws IOException {
        String path = "./files/hits.txt";
        HITSTool tool = new HITSTool(path);
        tool.printResultPage();
    }
}
