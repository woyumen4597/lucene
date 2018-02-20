package cn.jrc.test;

import cn.jrc.util.GsonUtils;
import com.google.gson.Gson;
import org.junit.Test;

import java.util.ArrayList;

/**
 * @author Created By Jrc
 * @version v.0.1
 * @date 2018/2/20 19:16
 */
public class GsonTest {
    @Test
    public void toJsonTest(){
        Gson gson =  new Gson();
        ArrayList<String> list = new ArrayList<>();
        list.add("jrc");
        list.add("jra");
        list.add("jrb");
        String json = gson.toJson(list);
        System.out.println(json);
    }

    @Test
    public void fromJsonTest(){
        String json = "[\"jrc\",\"jra\",\"jrb\"]";
        Gson gson = new Gson();
        ArrayList<String> list = gson.fromJson(json, ArrayList.class);
        for (String s : list) {
            System.out.println(s);
        }
    }

    @Test
    public void testGsonUtils(){
        ArrayList<String> list = new ArrayList<>();
        list.add("jrc");
        list.add("jra");
        list.add("jrb");
        String json = GsonUtils.fromList2Json(list);
        System.out.println(json);
    }
}
