package com.qgyyzs.globalcosmetics.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/4/28.
 */

public class MapUtils {
    public static Map<Integer, Boolean> getUrlParams(String param) {
        Map<Integer, Boolean> map = new HashMap<Integer, Boolean>();
        if ("".equals(param) || null == param) {
            return map;
        }
        String[] params = param.split(", ");
        for (String s : params) {
            String[] ms = s.split("=");

            if (ms[1].equals("true")) {
                map.put(new Integer(Integer.parseInt(ms[0])), true);
            } else {
                map.put(new Integer(Integer.parseInt(ms[0])), false);
            }

        }
        return map;
    }

}
