package com.xuecheng.manage_cms.dao;

import java.util.HashMap;
import java.util.Map;

public class Demo {

    public static void main(String[] args) {

        Map<String,String> map = new HashMap();
        map.put(null,"11");
        System.out.println(map.get(null));
        show();

    }

    private static void show() {
        new Demo().show1();
    }
    public void show1(){
        System.out.println(1);
    }

}

