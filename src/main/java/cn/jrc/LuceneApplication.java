package cn.jrc;

import cn.jrc.db.DBSearch;
import cn.jrc.domain.Book;
import org.apache.lucene.queryparser.classic.ParseException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

/**
 * @author Created By Jrc
 * @version v.0.1
 * @date 2018/1/17 10:26
 */
@SpringBootApplication
@RestController
public class LuceneApplication {

    @RequestMapping("/hello")
    public String index(){
        return "hello Lucene";
    }

    @RequestMapping("/search")
    public List<Book> search(String keyword) throws IOException, ParseException {
        List<Book> books = DBSearch.searchData(keyword);
        return books;
    }

    public static void main(String[] args) {
        SpringApplication.run(LuceneApplication.class,args);
    }
}
