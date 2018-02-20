package cn.jrc.util;

import cn.jrc.domain.PageInfo;
import com.google.gson.Gson;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.IOException;
import java.nio.file.Paths;

/**
 * @author Created By Jrc
 * @version v.0.1
 * @date 2018/2/20 18:58
 */
public class IndexUtils {
    public static void index(PageInfo pageInfo,String indexDir) throws IOException {
        Directory directory = FSDirectory.open(Paths.get(indexDir));
        IKAnalyzer analyzer = new IKAnalyzer();
        IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
        iwc.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
        IndexWriter writer = new IndexWriter(directory,iwc);
        Document document = addDocument(pageInfo);
        writer.addDocument(document);
        writer.commit();
    }

    private static Document addDocument(PageInfo pageInfo) {
        Document document  = new Document();
        document.add(new StringField("url",pageInfo.getUrl(), Field.Store.YES));
        document.add(new StringField("title",pageInfo.getTitle(), Field.Store.YES));
        document.add(new StringField("question",pageInfo.getQuestion(), Field.Store.YES));
        String answers = GsonUtils.fromList2Json(pageInfo.getAnswers());
        document.add(new TextField("answers",answers, Field.Store.YES));
        String tags = GsonUtils.fromList2Json(pageInfo.getTags());
        document.add(new StringField("tags",tags, Field.Store.YES));
        document.add(new StringField("date",pageInfo.getDate().toString(), Field.Store.YES));
        document.add(new StringField("description",pageInfo.getDescription(), Field.Store.YES));
        return document;
    }

}
