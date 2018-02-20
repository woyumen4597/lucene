package cn.jrc.lucene;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;


import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Paths;

/**
 * @author Created By Jrc
 * @version v.0.1
 * @date 2018/1/11 19:10
 */
public class Indexer {
    private IndexWriter writer;
    public Indexer(String indexDir) throws IOException {
        Directory directory = FSDirectory.open(Paths.get(indexDir));
        StandardAnalyzer analyzer = new StandardAnalyzer();
        IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
        iwc.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
        writer = new IndexWriter(directory,iwc);
    }


    private Document getDocument(File f) throws IOException {
        Document document = new Document();
        document.add(new TextField("文档",new FileReader(f)));
        document.add(new StringField("文件名",f.getName(), Field.Store.YES));
        document.add(new StringField("路径",f.getCanonicalPath(), Field.Store.YES));
        return document;
    }

}
