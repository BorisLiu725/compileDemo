package com.ly;

import com.ly.directory.TypeTableMap;
import com.ly.enumerate.WordType;
import com.ly.model.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by BorisLiu on 2019/10/8
 */
public class WordAnalyse {

    private String fileName;
    /**
     * 存放结果
     * */
    private List<TypeCode> results = new ArrayList<>();

    public WordAnalyse(String fileName){
       this.fileName = fileName;
    }

    /**
     * 去掉多余的空格和换行
     */
    public  String replaceAllBlank(String source){
        Pattern pattern = Pattern.compile("\\s*|\t|\n|\r");
        Matcher matcher = pattern.matcher(source);
        String result = matcher.replaceAll("");
        return result;
    }

    public  List<TypeCode> analyse(String source){
        int identifierWordCode = 1;
        int DigitalCode = 1;

        for (int i = 0; i < source.length(); i++) {
            char ch = source.charAt(i);
            //空格
            if (ch==' '){
                System.out.println("空格==>"+ch + "i="+i);
            } else if (isLetter(ch)){ //第一个字是字母
                String temp = "";
                while (isLetter(ch)|| isDigital(ch)){
                    temp+=ch;
                    i++;
                    ch = source.charAt(i);
                }
                i--;
                //非字母或者数字
                //判断是关键字还是标识符

                System.out.println("关键字为："+temp +"ch=="+ch);
                switch (judgeIdentifierWordTyp(temp)){
                    case KEY_WORD:
                        KeyWord keyWord = new KeyWord();
                        keyWord.setValue(temp);
                        keyWord.setTypeCode(TypeTableMap.keyWordsMap.get(temp));
                        //将关键字加入到结果集中
                        TypeCode typeCode = isConstains(keyWord);
                        if (typeCode==null){
                            results.add(keyWord);
                        }else {
                            results.add(typeCode);
                        }
                        break;
                    case IDENTIFYING_WORD:
                        //保存标识符到标识符表中
                        TypeTableMap.identifierMap.put(temp,++TypeTableMap.identifierWordCode);
                        //将标识符加入到结果集中
                        IdentifierWord identifierWord = new IdentifierWord();
                        identifierWord.setValue(temp);
                        identifierWord.setTypeCode(TypeTableMap.identifierWordCode);
                        typeCode = isConstains(identifierWord);
                        if (typeCode==null){
                            results.add(identifierWord);
                        }else {
                            --TypeTableMap.identifierWordCode;
                            results.add(typeCode);
                        }
                        break;
                    default:break;
                }
            }else if(isDigital(ch)){ //如果第一个是数字
                int temp = 0;
                while(isDigital(ch)){
                    i++;
                    temp = temp*10 + Integer.parseInt(ch+"");
                    ch = source.charAt(i);
                }
                //非数字，判断是不是空格
                i--;
                Digital digital = null;
                if (isBlank(ch) || ch == ';' || ch==')'){
                    digital = new Digital();
                    digital.setTypeCode(++TypeTableMap.digitalCode);
                    digital.setValue(temp);

                    //加入到结果集
//                    results.add(digital);
                }else{
                    System.out.println("数字语法错误...");
                }
                TypeCode typeCode = isConstains(digital);
                if (typeCode==null){
                    results.add(digital);
                }else {
                   --TypeTableMap.digitalCode;
                    results.add(typeCode);
                }

            }else if (ch == '='){
                char nextChar = source.charAt(i+1);
                OperatorWord operatorWord = new OperatorWord();
                if (nextChar == '='){
                    i++;
                    operatorWord.setTypeCode(++TypeTableMap.operatorWordCode);
                    operatorWord.setValue("==");
                }else {
                    operatorWord.setTypeCode(++TypeTableMap.operatorWordCode);
                    operatorWord.setValue("=");
                }
                TypeCode typeCode = isConstains(operatorWord);
                if (typeCode==null){
                    results.add(operatorWord);
                }else {
                    --TypeTableMap.operatorWordCode;
                    results.add(typeCode);
                }
            }else if (ch == '+'){
                char nextChar = source.charAt(i+1);
                OperatorWord operatorWord = new OperatorWord();
                if (nextChar == '+'){
                    i++;
                    operatorWord.setTypeCode(++TypeTableMap.operatorWordCode);
                    operatorWord.setValue("++");
                }else {
                    operatorWord.setTypeCode(++TypeTableMap.operatorWordCode);
                    operatorWord.setValue("+");
                }
                TypeCode typeCode = isConstains(operatorWord);
                if (typeCode==null){
                    results.add(operatorWord);
                }else {
                    --TypeTableMap.operatorWordCode;
                    results.add(typeCode);
                }
            }else if (ch == '-'){
                char nextChar = source.charAt(i+1);
                OperatorWord operatorWord = new OperatorWord();
                if (nextChar == '-'){
                    i++;
                    operatorWord.setTypeCode(++TypeTableMap.operatorWordCode);
                    operatorWord.setValue("--");
                }else {
                    operatorWord.setTypeCode(++TypeTableMap.operatorWordCode);
                    operatorWord.setValue("-");
                }
                TypeCode typeCode = isConstains(operatorWord);
                if (typeCode==null){
                    results.add(operatorWord);
                }else {
                    --TypeTableMap.operatorWordCode;
                    results.add(typeCode);
                }
            }else if (ch == '/'){
                OperatorWord operatorWord = new OperatorWord();
                operatorWord.setTypeCode(++TypeTableMap.operatorWordCode);
                operatorWord.setValue("/");
                results.add(operatorWord);
                TypeCode typeCode = isConstains(operatorWord);
                if (typeCode==null){
                    results.add(operatorWord);
                }else {
                    --TypeTableMap.operatorWordCode;
                    results.add(typeCode);
                }
            }else if (ch == '*'){
                //判断是乘还是 ** 二次方
                char nextChar = source.charAt(i+1);
                OperatorWord operatorWord = new OperatorWord();
                if (nextChar == '*'){
                    i++;
                    operatorWord.setTypeCode(++TypeTableMap.operatorWordCode);
                    operatorWord.setValue("**");
                }else {
                    operatorWord.setTypeCode(++TypeTableMap.operatorWordCode);
                    operatorWord.setValue("*");
                }
                TypeCode typeCode = isConstains(operatorWord);
                if (typeCode==null){
                    results.add(operatorWord);
                }else {
                    --TypeTableMap.operatorWordCode;
                    results.add(typeCode);
                }
            }else if (ch == '<'){
                char nextChar = source.charAt(i+1);
                OperatorWord operatorWord = new OperatorWord();
                if (nextChar == '='){
                    i++;
                    operatorWord.setTypeCode(++TypeTableMap.operatorWordCode);
                    operatorWord.setValue("<=");
                }else {
                    operatorWord.setTypeCode(++TypeTableMap.operatorWordCode);
                    operatorWord.setValue("<");
                }
                TypeCode typeCode = isConstains(operatorWord);
                if (typeCode==null){
                    results.add(operatorWord);
                }else {
                    --TypeTableMap.operatorWordCode;
                    results.add(typeCode);
                }
            }else if (ch == '>'){
                char nextChar = source.charAt(i+1);
                OperatorWord operatorWord = new OperatorWord();
                if (nextChar == '='){
                    i++;
                    operatorWord.setTypeCode(++TypeTableMap.operatorWordCode);
                    operatorWord.setValue(">=");
                }else {
                    operatorWord.setTypeCode(++TypeTableMap.operatorWordCode);
                    operatorWord.setValue(">");
                }
                TypeCode typeCode = isConstains(operatorWord);
                if (typeCode==null){
                    results.add(operatorWord);
                }else {
                    --TypeTableMap.operatorWordCode;
                    results.add(typeCode);
                }
            }else if (ch == '!'){
                char nextChar = source.charAt(i+1);
                OperatorWord operatorWord = new OperatorWord();
                if (nextChar == '='){
                    i++;
                    operatorWord.setTypeCode(++TypeTableMap.operatorWordCode);
                    operatorWord.setValue("!=");
                }else {
                    operatorWord.setTypeCode(++TypeTableMap.operatorWordCode);
                    operatorWord.setValue("!");
                }
                TypeCode typeCode = isConstains(operatorWord);
                if (typeCode==null){
                    results.add(operatorWord);
                }else {
                    --TypeTableMap.operatorWordCode;
                    results.add(typeCode);
                }
            }
            else if (ch == ','){
                limitWord limitWord = new limitWord();
                limitWord.setTypeCode(++TypeTableMap.operatorWordCode);
                limitWord.setValue(",");
                results.add(limitWord);
                TypeCode typeCode = isConstains(limitWord);
                if (typeCode==null){
                    results.add(limitWord);
                }else {
                    --TypeTableMap.operatorWordCode;
                    results.add(typeCode);
                }
            }else if (ch == ';'){
                limitWord limitWord = new limitWord();
                limitWord.setTypeCode(++TypeTableMap.operatorWordCode);
                limitWord.setValue(";");
                TypeCode typeCode = isConstains(limitWord);
                if (typeCode==null){
                    results.add(limitWord);
                }else {
                    --TypeTableMap.operatorWordCode;
                    results.add(typeCode);
                }
            }else if (ch == '{'){
                limitWord limitWord = new limitWord();
                limitWord.setTypeCode(++TypeTableMap.operatorWordCode);
                limitWord.setValue("{");
                TypeCode typeCode = isConstains(limitWord);
                if (typeCode==null){
                    results.add(limitWord);
                }else {
                    --TypeTableMap.operatorWordCode;
                    results.add(typeCode);
                }
            }else if (ch == '}'){
                limitWord limitWord = new limitWord();
                limitWord.setTypeCode(++TypeTableMap.operatorWordCode);
                limitWord.setValue("}");
                TypeCode typeCode = isConstains(limitWord);
                if (typeCode==null){
                    results.add(limitWord);
                }else {
                    --TypeTableMap.operatorWordCode;
                    results.add(typeCode);
                }
            }else if (ch == '('){
                limitWord limitWord = new limitWord();
                limitWord.setTypeCode(++TypeTableMap.operatorWordCode);
                limitWord.setValue("(");
                TypeCode typeCode = isConstains(limitWord);
                if (typeCode==null){
                    results.add(limitWord);
                }else {
                    --TypeTableMap.operatorWordCode;
                    results.add(typeCode);
                }
            }else if (ch == ')'){
                limitWord limitWord = new limitWord();
                limitWord.setTypeCode(++TypeTableMap.operatorWordCode);
                limitWord.setValue(")");
                TypeCode typeCode = isConstains(limitWord);
                if (typeCode==null){
                    results.add(limitWord);
                }else {
                    --TypeTableMap.operatorWordCode;
                    results.add(typeCode);
                }
            } else{
                System.out.println("3.回车或者一些换行的字符..."+ch);
            }


        }
        return results;
    }

    /**
     * 判断是不是空格
     * */
    public boolean isBlank(char ch){
        if (ch == ' ' || ch=='\t' || ch=='\n' || ch=='\r'){
            return true;
        }
        return false;
    }


    /**
     * 判断是不是字母
     * */
    public boolean isLetter(char ch){
        if ((ch >= 'a' && ch <= 'z' )|| (ch >='A' && ch<='Z')){
            return true;
        }
        return false;
    }

    /**
     * 判断是不是数字
     * */
    public boolean isDigital(char ch){
        if ((ch >= '0' && ch <= '9' )){
            return true;
        }
        return false;
    }

    /**
     * 判断是关键字还是标识符
     * */
    public WordType judgeIdentifierWordTyp(String word){
        if (TypeTableMap.keyWordsMap.containsKey(word)){
            return WordType.KEY_WORD;
        }
        return WordType.IDENTIFYING_WORD;
    }

    /**
     *  判断关键字有没有在集合中
     * */
    public TypeCode isConstains(TypeCode typeCode){
//        System.out.println(typeCode);
        for (int i = 0; i < results.size(); i++) {
            if (results.get(i).getValue().equals(typeCode.getValue())){
                typeCode = null;
                return results.get(i);
            }
        }
        return null;
    }


    public StringBuilder readFile(String fileName){
        File file = new File(fileName);
        try {
            InputStreamReader  reader = new InputStreamReader(new FileInputStream(file));
            BufferedReader bufferedReader = new BufferedReader(reader);
            String line = "";
            StringBuilder result = new StringBuilder();
            while ((line=bufferedReader.readLine())!=null){
                result.append(line);
//                System.out.print(line);
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<TypeCode> printfResult(){
        StringBuilder stringBuilder = readFile(this.fileName);
        List<TypeCode> analyse = analyse(stringBuilder.toString());
        return analyse;
    }


    public static void main(String[] args) {
        String fileName = "C:\\Users\\asus\\Desktop\\demo.c";
        WordAnalyse analyse = new WordAnalyse(fileName);
        List<TypeCode> result = analyse.printfResult();
        result.forEach((E)-> System.out.println(E));
        System.out.println("*********************打印标识符表====");
        TypeTableMap.identifierMap.forEach((K,V)->{
            System.out.println("("+K+","+V+")");
        });

    }

}
