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

    public void close() throws IOException {
        writer.close();
    }

    public int index(String dataDir, FileFilter filter) throws IOException {
        File[] files = new File(dataDir).listFiles();
        for(File f:files){
            if(!f.isDirectory() && !f.isHidden()&&f.exists()&&f.canRead()&&(filter==null||filter.accept(f))){
                indexFile(f);
            }
        }
        return writer.numDocs();
    }

    private void indexFile(File f) throws IOException {
        System.out.println("Indexing "+f.getCanonicalPath());
        Document doc = getDocument(f);
        writer.addDocument(doc);
    }

    private Document getDocument(File f) throws IOException {
        Document document = new Document();
        document.add(new TextField("文档",new FileReader(f)));
        document.add(new StringField("文件名",f.getName(), Field.Store.YES));
        document.add(new StringField("路径",f.getCanonicalPath(), Field.Store.YES));
        return document;
    }

    public static void main(String[] args) throws Exception {
        String indexDir = "D://Lucene//test1";
        String dataDir = "D://Lucene//test2";
        long start = System.currentTimeMillis();
        Indexer indexer = new Indexer(indexDir);
        int numIndexed;
        try{
            numIndexed = indexer.index(dataDir,new TextFilesFilter());
        }finally {
            indexer.close();
        }
        long end = System.currentTimeMillis();
        System.out.println("搜索到"+numIndexed+" 文件夹共花费 "+(end-start)+"毫秒");
    }
    private static class TextFilesFilter implements FileFilter{

        @Override
        public boolean accept(File pathname) {
            return pathname.getName().toLowerCase().endsWith(".txt");
        }
    }
}
