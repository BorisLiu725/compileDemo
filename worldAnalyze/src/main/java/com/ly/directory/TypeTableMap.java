package com.ly.directory;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by BorisLiu on 2019/10/8
 */
public class TypeTableMap {

    /**
     * 关键字表
     * */
    public static Map<String,Integer> keyWordsMap = new HashMap<>();
    static {

        // 初始化关键字表
        keyWordsMap.put("main",1);
        keyWordsMap.put("return",2);
        keyWordsMap.put("if",3);
        keyWordsMap.put("else",4);
        keyWordsMap.put("for",5);
        keyWordsMap.put("include",6);
        keyWordsMap.put("printf",7);
        keyWordsMap.put("int",8);
        keyWordsMap.put("char",9);
        keyWordsMap.put("while",10);
    }


    /**
     * 标识符表
     * */
    public static Map<String,Integer> identifierMap = new HashMap<>();
    public static int identifierWordCode = 90;

    /**
     * 数字常量表
     * */
    public static Map<Integer,Integer> DigitalsMap = new HashMap<>();
    public static int digitalCode = 50;

    /**
     * 界符表
     * */
    public static Map<Integer,Integer> limitWordsMap = new HashMap<>();

    /**
     * 操作数表
     * */
    public static Map<Integer,Integer> OperatorWordMap = new HashMap<>();
    public static  int operatorWordCode = 70;

}
