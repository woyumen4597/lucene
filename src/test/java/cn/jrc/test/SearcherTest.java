package cn.jrc.test;

import cn.jrc.domain.Page;
import cn.jrc.domain.Result;
import cn.jrc.service.Searcher;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author Created By Jrc
 * @version v.0.1
 * @date 2018/5/6 10:58
 */
public class SearcherTest {
    @Test
    public void getPage(){
        Searcher searcher = new Searcher();
        Page<Result> page = searcher.getPage("java c++", 1,4);
        assertEquals(page.getList().size(),4);
        assertEquals(page.getPageNum(),1);
        assertEquals(page.getLimit(),4);
    }

}
