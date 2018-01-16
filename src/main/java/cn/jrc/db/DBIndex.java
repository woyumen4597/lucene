package cn.jrc.db;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.SimpleFSDirectory;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;

/**
 * @author Created By Jrc
 * @version v.0.1
 * @date 2018/1/16 18:44
 */
public class DBIndex {
    public static Connection conn;
    static {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/jrc","root","woyumen4597");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static final String INDEXPATH = "./indexDir";



    public static void createIndex() throws IOException, SQLException {
        System.out.println("create index loading...");
        Path path = Paths.get(INDEXPATH);
        Directory directory = new SimpleFSDirectory(path);
        Analyzer analyzer = new IKAnalyzer();
        IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
        iwc.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
        IndexWriter writer = new IndexWriter(directory, iwc);

        String sql = "select id,name,author,price,`image`,sales,stock from book";
        //db query
        PreparedStatement statement = conn.prepareStatement(sql);
        System.out.println("");
        ResultSet resultSet = statement.executeQuery();
        while(resultSet.next()){
            Document doc = new Document();
            String id = resultSet.getString("id");
            String name = resultSet.getString("name");
            String author = resultSet.getString("author");
            double price = resultSet.getDouble("price");
            String image = resultSet.getString("image");
            int sales = resultSet.getInt("sales");
            int stock = resultSet.getInt("stock");
            doc.add(new StringField("id",id, Field.Store.YES));
            doc.add(new StringField("name",name, Field.Store.YES));
            doc.add(new StringField("author",author, Field.Store.YES));
            doc.add(new DoublePoint("price",price));
            doc.add(new StringField("image",image, Field.Store.YES));
            doc.add(new IntPoint("sales",sales));
            doc.add(new IntPoint("stock",stock));
            System.out.println(name);
            writer.addDocument(doc);
        }
        resultSet.close();
        writer.close();
    }

    public static void main(String[] args) throws IOException, SQLException {
        createIndex();
    }


}
