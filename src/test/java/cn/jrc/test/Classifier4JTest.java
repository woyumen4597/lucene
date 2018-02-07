package cn.jrc.test;

import net.sf.classifier4J.summariser.SimpleSummariser;

/**
 * @author Created By Jrc
 * @version v.0.1
 * @date 2018/2/7 20:07
 */
public class Classifier4JTest {
    public static void main(String[] args) {
        String input = "Classifier4J is a Java package for working with text.  Classifier4J includes a summariser";
        SimpleSummariser simpleSummariser = new SimpleSummariser();
        String result = simpleSummariser.summarise(input, 1);
        System.out.println(result);
    }
}
