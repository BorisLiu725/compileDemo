package com.ly.ll1;

import java.io.PrintStream;
import java.util.*;

/**
 * Created by BorisLiu on 2019/11/19
 */
public class GrammarMain {

    /**
     * 定义一个文法G
     * */
    private G srcG;


    public void initG(){
        srcG = new G();

        Scanner in = new Scanner(System.in);
        System.out.println("请输入文法的开始符号:");
        String s = in.nextLine();
        srcG.setStartChar(s);
        System.out.println("请输入所有的终结符号:");
        s = in.nextLine().replaceAll("[ ]*", "");
        System.out.println(s);
        for (int i = 0; i < s.length(); i++) {
            srcG.getVtSet().add(s.charAt(i)+"");
        }
        srcG.getVtSet().forEach((v)-> System.out.print(v+" "));
        System.out.println();

        System.out.println("请输入所有的非终结符号:");
        s = in.nextLine().replaceAll("[ ]*", "");
        for (int i = 0; i < s.length(); i++) {
            srcG.getVnSet().add(s.charAt(i)+"");
        }
        srcG.getVnSet().forEach((v)-> System.out.print(v+" "));
        System.out.println();
        System.out.println("输入产生式的集合。。。");

        while (!(s = in.nextLine()).equals("quit")){
            String[] split = s.split("->");
            srcG.getProduction().ProductionMap.put(split[0],split[1]);
        }
        srcG.getProduction().ProductionMap.forEach((k,v)-> System.out.println(k+"->"+v));
    }

    /**
     * 获取first集
     * ch：求ch的first集
     * */
    private void getFirst(Set<String> vnSet,Set<String> vtSet,Map<String,String> productionMap,String ch,Map<String,Set<String>> firstMap){

        if (Objects.nonNull(firstMap.get(ch))){
            return;
        }
        String chProduct = productionMap.get(ch);
        //遍历这个产生式
        int charGrep = -1;
        for (int i = 0; i < chProduct.length(); i++) {
            //如果遍历到第一个是终结符的话
            if (vtSet.contains(chProduct.charAt(i)+"")){

                Set<String> s = firstMap.get(ch);
                if (Objects.nonNull(s)){
                    s.add(chProduct.charAt(i)+"");
                    firstMap.put(ch,s);
                }else {
                    s = new HashSet<>();
                    s.add(chProduct.charAt(i)+"");
                    firstMap.put(ch,s);
                }

                charGrep = chProduct.indexOf("|",i);
                if (charGrep <=0 ){
                    break;
                }
                i = charGrep;
            }else if (vnSet.contains(chProduct.charAt(i)+"")){

                //如果是非终结符
                //继续求它的first集
                //如果first集没求的话，就直接先去求first集
                if (!firstMap.containsKey(chProduct.charAt(i)+"")){
                    getFirst(vnSet,vtSet,productionMap,chProduct.charAt(i)+"",firstMap);
                }
                //将运算结果添加到ch的first集当中
                Set<String> s = firstMap.get(chProduct.charAt(i) + "");
                firstMap.put(ch,s);

                charGrep = chProduct.indexOf("|",i);
                if (charGrep <=0 ){
                    break;
                }
                i = charGrep;
            }else if ((chProduct.charAt(i)+"").equals("|")){
                System.out.println("遇到一个|========>");

            }else if ((chProduct.charAt(i)+"").equals("@")){
                Set<String> s = firstMap.get(ch);
                if (Objects.nonNull(s)){
                    s.add("@");
                    firstMap.put(ch,s);
                }else {
                    s = new HashSet<>();
                    s.add("@");
                    firstMap.put(ch,s);
                }

            }else {
                System.out.println(chProduct.charAt(i)+"<=========>");
            }
        }

    }

    /**
     * 求first集
     * */
    public Map<String,Set<String>> getFirst(){
        Map<String,Set<String>> firstMap = new HashMap<>();
        for (String ch : srcG.getVnSet()) {
           // System.out.println(ch+"这个字符=======");
            getFirst(srcG.getVnSet(),srcG.getVtSet(),srcG.getProduction().ProductionMap,ch,firstMap);
        }
        //System.out.println("=====<>"+srcG.getVnSet());
        return firstMap;
    }



    /**
     * 求follow集
     * */
    private void getFollow(Set<String> vnSet,Set<String> vtSet,Map<String,String> productionMap,String ch,Map<String,Set<String>> firstMap,Map<String,Set<String>> followMap){
        if (Objects.nonNull(followMap.get(ch))){
            return;
        }

        if (ch.equals(srcG.getStartChar())&&Objects.isNull(followMap.get(ch))){
            HashSet<String> set = new HashSet<>();
            set.add("#");
            followMap.put(ch,set);
        }
        //扫描所有的产生式右部
        productionMap.forEach((k,v)->{
            if (v.contains(ch)){
                int index = 0;
                for (int i=0;i<v.length()&&index>=0;i++){
                    index = v.indexOf(ch,i);
                    index++;
                   // System.out.println("=-=-=-=-=-=-=-=-=-=-");
                    //最后一个符号
                    if ((index<v.length()&&(v.charAt(index)+"").equals("|")&&(!k.equals(v.charAt(index-1)+""))) || (index >= v.length()&&(!k.equals(ch)))){
                        Set<String> s = followMap.get(k);

                        Set<String> old = followMap.get(ch);
                        if (s==null){
                            getFollow(vnSet,vtSet,productionMap,k,firstMap,followMap);
                            s = followMap.get(k);
                        }
                        if (old!=null){
                            old.addAll(s);
                            followMap.put(ch,old);
                        }else {
                           old = new HashSet<>();
                           old.addAll(s);
                           followMap.put(ch,old);
                        }
                    }//如果是终结符
                   else if (index<v.length()&&vtSet.contains(v.charAt(index)+"")){
                        Set<String> s = followMap.get(ch);
                        if (s==null){
                            s = new HashSet<>();
                        }
                        s.add(v.charAt(index)+"");
                        followMap.put(ch,s);
                        break;
                    }else if (index<v.length()&&vnSet.contains(v.charAt(index)+"")){
                        //如果是非终结符
                        Set<String> s = firstMap.get(v.charAt(index)+"");
                        boolean flag = false;
                        if (s.contains("@")){
                            flag = true;
                            s.remove("@");
                        }
                        Set<String> old = followMap.get(ch);
                        if (old!=null){
                            old.addAll(s);
                            followMap.put(ch,old);
                        }else {
                            old = new HashSet<>();
                            old.addAll(s);
                            followMap.put(ch,old);
                        }

                        if (flag){
                            int j = index + 1;
                            for (j = index+1; j < v.length()&&flag; j++) {
                                if (vnSet.contains(v.charAt(j)+"")){
                                    s = firstMap.get(v.charAt(j)+"");
                                    flag = false;
                                    if (s.contains("@")){
                                        flag = true;
                                        s.remove("@");
                                    }
                                    old = followMap.get(ch);
                                    if (old == null){
                                        old = new HashSet<>();
                                    }
                                    old.addAll(s);
                                    followMap.put(ch,old);

                                }else if (vtSet.contains(v.charAt(j)+"")){
                                    old = followMap.get(ch);
                                    if (old==null){
                                        old = new HashSet<>();
                                    }
                                    old.add(v.charAt(j)+"");
                                    followMap.put(ch,old);
                                    break;
                                }
                            }
                            if (j >= v.length()&&(!k.equals(ch))){
                                s = followMap.get(v.charAt(index)+"");
                                old = followMap.get(ch);
                                if (old==null){
                                    old = new HashSet<>();
                                }
                                old.addAll(s);
                                followMap.put(ch,old);
                            }


                        }
                        break;
                    }else if (index<v.length()&&(v.charAt(index)+"").equals("|")){
                        index = v.indexOf(ch,index);
                        if (index<=0){
                            break;
                        }
                        index++;
                        break;
                    }
                }
            }

        });

    }

    /**
     * 求follow集合
     * */
    public Map<String,Set<String>> getFollow(){
        Map<String,Set<String>> firstMap = getFirst();
        Map<String,Set<String>> followMap = new HashMap<>();
        for (String ch : srcG.getVnSet()) {
            System.out.println(ch+"这个字符=======");
            getFollow(srcG.getVnSet(),srcG.getVtSet(),srcG.getProduction().ProductionMap,ch,firstMap,followMap);
        }
        System.out.println("=====<>"+srcG.getVnSet());
        return followMap;
    }



    /**
     * 打印预测分析表
     * */
    public List<Node> printPredictionForm(Map<String,Set<String>> firstSet,Map<String,Set<String>> followSet,G g){
        
//        String[][] tables = new String[srcG.getVtSet().size()+1][srcG.getVtSet().size()+1];

        List<Node> tables = new ArrayList<>();
        firstSet.forEach((k,v)->{
            v.forEach((e)->{
                if (!e.equals("@")){
                    String production =  getProductionNoneNull(g.getProduction().ProductionMap.get(k));
                    if (production.contains("|") && production.contains(e)){
                        String[] split = production.split("[|]");
                        for (int i = 0; i < split.length; i++) {
                            if (split[i].contains(e)){
                                Node node = new Node(k,e,split[i]);
                                tables.add(node);
                                break;
                            }
                        }
                    }else {
                        Node node = new Node(k,e,production);
                        tables.add(node);
                    }

                }else {
                    followSet.forEach((k1,v1)->{
                        if (k1.equals(k)){
                            v1.forEach((k2)->{
                                Node node = new Node(k1,k2,"@");
                                tables.add(node);
                            });
                        }
                    });

                }
            });
        });

        tables.forEach((k)->{
            System.out.println(k);
        });
        return tables;
   }


   /**
    * 去除文法0中的null
    * */
   public String getProductionNoneNull(String produce){
       return produce.replaceAll("[@][|]", "").replaceAll("[|][@]","");
   }

   /**
    * 判断是否为ll1
    * */
   public boolean judge(Map<String,Set<String>> firstSet,Map<String,Set<String>> followSet,G g){
        List list = new ArrayList();
        firstSet.forEach((k,v)->{
            if (v.contains("@")){
                followSet.forEach((k1,v1)->{
                    if (k.equals(k1)){
                        if (v1.contains(v)){
                            list.add(new Object());
                        }
                    }
                });
            }
        });
        return list.size()>0?false:true;

   }

   /**
    * 预测分析过程
    * */
   public void prediction(G g,List<Node> tables){
       String startChar = g.getStartChar();
       System.out.println("请输入预测分析串(#*****#)：");
       Scanner in = new Scanner(System.in);
       String content = in.nextLine();
       Stack<String> stack  = new Stack<>();
       stack.push("#");
       stack.push(startChar);
       int i = 1;
       while (i < content.length()&&(!stack.isEmpty())){
           String tempChar = stack.peek();
           //System.out.println("tempChar = =="+tempChar);
           if (tempChar.equals("#")){
               System.out.println("分析成功!");
               return;
           }else if (tempChar.equals(content.charAt(i)+"")&&!tempChar.equals("#")){
               String pop = stack.pop();
               System.out.println("弹出栈："+pop);
               i++;
           }else if (g.getVnSet().contains(tempChar)){
               for (int j = 0; j < tables.size(); j++) {
                   if (tables.get(j).getVnChar().equals(tempChar)&&tables.get(j).getVtChar().equals(content.charAt(i)+"")){
                       String pop = stack.pop();
                       System.out.println("弹出栈："+pop);
                       String value = tables.get(j).getValue();
                       if ((value.charAt(0)+"").equals("@")){
                           continue;
                       }
                       for (int k = value.length()-1; k >= 0; k--) {
                           stack.push(value.charAt(k)+"");
                       }
                   }
               }

           }else if (!tables.get(i).getVnChar().equals(tempChar)&&!tables.get(i).getVtChar().equals(content.charAt(i))){
               System.out.println("分析出错！");
               return ;
           }

       }
       System.out.println("分析失败！");

   }



    public void clear(){
        srcG = null;
    }

    /**
     * 主程序
     * */
    public static void main(String[] args) {
        GrammarMain grammarMain = new GrammarMain();
        grammarMain.initG();

        Map<String, Set<String>> firstSet = grammarMain.getFirst();
        System.out.println("first集：");
        firstSet.forEach((k,v)-> System.out.println(k+"==>"+v));
        Map<String, Set<String>> followSet = grammarMain.getFollow();
        System.out.println("============================");
        System.out.println("follow集：");
        followSet.forEach((k,v)-> System.out.println(k+"==>"+v));
        List<Node> tables = grammarMain.printPredictionForm(firstSet, followSet, grammarMain.srcG);

        System.out.println("=====判断文法是否为LL(1)=====");
        boolean judge = grammarMain.judge(firstSet, followSet, grammarMain.srcG);
        System.out.println(judge);
        System.out.println("预测分析程序-======");
        grammarMain.prediction(grammarMain.srcG,tables);
    }



}
