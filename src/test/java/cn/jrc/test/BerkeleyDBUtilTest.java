package cn.jrc.test;

import cn.jrc.db.BerkeleyDBUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.UnsupportedEncodingException;

import static org.junit.Assert.assertEquals;

/**
 * @author Created By Jrc
 * @version v.0.1
 * @date 2018/1/26 13:45
 */
public class BerkeleyDBUtilTest {
    private BerkeleyDBUtil dbUtil = null;

    @Before
    public void setup(){
        dbUtil = new BerkeleyDBUtil("./db");
    }

    @Test
    public void testWriteToDatabase(){
        for (int i = 0; i < 10; i++) {
            dbUtil.writeToDatabase(i+"","student"+i,true);
        }
    }

    @Test
    public void testReadFromDatabase() throws UnsupportedEncodingException {
        String value = dbUtil.readFromDatabase("2");
        assertEquals(value,"student2");
    }

    @Test
    public void testGetEveryItem(){
        int size = dbUtil.getEveryItem().size();
        assertEquals(size,10);
    }

    @Test
    public void  testDeleteFroDatabase(){
        dbUtil.deleteFromDatabase("4");
        assertEquals(dbUtil.getEveryItem().size(),9);
    }

    @After
    public void cleanup(){
        dbUtil.closeDB();
    }
}
