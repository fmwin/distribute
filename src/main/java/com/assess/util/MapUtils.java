package com.assess.util;

import java.util.HashMap;
import java.util.Map;

public class MapUtils {

    public static boolean isEmpty(Map map, Object object){
        if (null == map.get(object) || "".equals(map.get(object))){
            return true;
        }
        return false;
    }

    public static void main(String[] args) {
        Map map = new HashMap();
        map.put("a", "");

        Map map1 = new HashMap();

        map.put("b", map1);

        System.out.println(isEmpty(map, "b"));
    }
}
