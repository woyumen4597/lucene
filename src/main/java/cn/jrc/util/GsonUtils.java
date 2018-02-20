package cn.jrc.util;

import com.google.gson.Gson;


/**
 * @author Created By Jrc
 * @version v.0.1
 * @date 2018/2/20 19:20
 */
public class GsonUtils {
    public static Gson gson = new Gson();

    public static String fromList2Json(Iterable list) {
        return gson.toJson(list);
    }

    public static Iterable fromJson2List(String json){
        return gson.fromJson(json,Iterable.class);
    }
}
