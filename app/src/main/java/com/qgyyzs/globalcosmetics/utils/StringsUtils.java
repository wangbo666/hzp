package com.qgyyzs.globalcosmetics.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/6/13.
 */

public class StringsUtils {

    public static String[] ToOnlyStrs(String[] str) {
        List<String> list = new ArrayList<String>();
        for (int i = 0; i < str.length; i++) {
            if (!list.contains(str[i])) {
                list.add(str[i]);
            }
        }
        String[] newStr = list.toArray(new String[1]); //返回一个包含所有对象的指定类型的数组
        return newStr;
    }


    //去掉数组中重复的值
    public static void testA() {
        String[] str = {"Java", "C++", "Php", "C#", "Python", "C++", "Java"};
        for (String elementA : str) {
            System.out.print(elementA + " ");
        }
        List<String> list = new ArrayList<String>();
        for (int i = 0; i < str.length; i++) {
            if (!list.contains(str[i])) {
                list.add(str[i]);
            }
        }
        /*
        Set<String> set = new HashSet<String>();
        for (int i=0; i<str.length; i++) {
            set.add(str[i]);
        }
        String[] newStr =  set.toArray(new String[1]);
        */
        System.out.println();
        String[] newStr = list.toArray(new String[1]); //返回一个包含所有对象的指定类型的数组
        for (String elementB : newStr) {
            System.out.print(elementB + " ");
        }
        System.out.println();
    }
}
