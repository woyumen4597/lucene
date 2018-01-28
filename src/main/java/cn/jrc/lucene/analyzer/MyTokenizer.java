package cn.jrc.lucene.analyzer;

import org.apache.lucene.analysis.Tokenizer;

import java.io.IOException;
import java.io.StringReader; /**
 * @author Created By Jrc
 * @version v.0.1
 * @date 2018/1/28 16:33
 */
public class MyTokenizer extends Tokenizer{
    private StringReader reader;
    public MyTokenizer(StringReader reader){
        this.reader = reader;
    }

    @Override
    public boolean incrementToken() throws IOException {
        return false;
    }
}
