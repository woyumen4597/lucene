package cn.jrc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    public static void main(String[] args) {
        SpringApplication.run(LuceneApplication.class,args);
    }
}
