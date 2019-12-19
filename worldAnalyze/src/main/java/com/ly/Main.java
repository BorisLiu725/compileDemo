package com.ly;

import com.ly.directory.TypeTableMap;

/**
 * Hello world!
 *
 */
public class Main {
    public static void main( String[] args ) {
        String source =
                "int main()\n" +
                "{\n" +
                "int i = 0;\n" +
                "int a1 = 2;\n" +
                "int sum = 0;\n" +
                "sum = i + a1;\n" +
                "return 0;\n" +
                "}";

        TypeTableMap.keyWordsMap.forEach((k,v)->{
            System.out.println("(key,value)==>"+"("+v+","+k+")");
        });

        System.out.println( "Hello World!" );


    }
}
