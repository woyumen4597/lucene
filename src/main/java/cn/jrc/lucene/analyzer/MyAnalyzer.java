package cn.jrc.lucene.analyzer;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.core.WhitespaceTokenizer;
import org.apache.lucene.util.IOUtils;

import java.io.Closeable;
import java.io.StringReader;

/**
 * @author Created By Jrc
 * @version v.0.1
 * @date 2018/1/28 16:29
 */
public class MyAnalyzer extends Analyzer {
    @Override
    protected TokenStreamComponents createComponents(String fieldName) {
        StringReader reader = null;

        TokenStreamComponents tsc = null;
        try {
            reader = new StringReader(fieldName);
            MyTokenizer it = new MyTokenizer(reader);
            tsc = new TokenStreamComponents(it);
        } finally {
            IOUtils.closeWhileHandlingException(new Closeable[]{reader});
        }
        return tsc;
    }
}
