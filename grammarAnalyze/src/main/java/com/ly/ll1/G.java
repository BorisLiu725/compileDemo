package com.ly.ll1;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class G{
        /**
         * 开始符号
         * */
        private String startChar;
        /**
         * 终结符集合
         * */
        private Set<String> VtSet = new HashSet<>();
        /**
         * 非终结符集合
         * */
        private Set<String> VnSet = new HashSet<>();
        /**
         * 产生式
         * */
        private Production production = new Production();




    /**
         * 产生式内部类
         * */
        public static class Production{
           /**
            * 原产生式
            * */
           public static final Map<String,String> ProductionMap = new HashMap<String, String>();
           /**
            * 消除左递归产生式
            * */
           public static final Map<String,String> NoLeftRecursionProductionMap = new HashMap<String, String>();
        }

    public G(Set<String> vtSet, Set<String> vnSet, Production production) {
        VtSet = vtSet;
        VnSet = vnSet;
        this.production = production;
    }

    public G() {
    }


    public String getStartChar() {
        return startChar;
    }

    public void setStartChar(String startChar) {
        this.startChar = startChar;
    }

    public Set<String> getVtSet() {
        return VtSet;
    }

    public void setVtSet(Set<String> vtSet) {
        VtSet = vtSet;
    }

    public Set<String> getVnSet() {
        return VnSet;
    }

    public void setVnSet(Set<String> vnSet) {
        VnSet = vnSet;
    }

    public Production getProduction() {
        return production;
    }

    public void setProduction(Production production) {
        this.production = production;
    }
}