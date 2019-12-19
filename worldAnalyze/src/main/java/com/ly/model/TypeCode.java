package com.ly.model;

/**
 * Created by BorisLiu on 2019/10/8
 */
public abstract class TypeCode<T> implements Comparable<TypeCode>{
    private T value;
    private int typeCode;



    public int getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(int typeCode) {
        this.typeCode = typeCode;
    }

    public T getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "( " +
                  value +
                " , " + typeCode +
                ')';
    }

    public void setValue(T value) {
        this.value = value;
    }

    @Override
    public int compareTo(TypeCode o) {
        System.out.println("调用compareTo 方法了...");
        return this.getValue().toString().compareTo(o.getValue().toString());
    }

//    @Override
//    public boolean equals(Object obj) {
//
//        if (obj instanceof TypeCode){
//
//            TypeCode record = (TypeCode)obj;
//            System.out.println("equals============="+record.getValue() + "==>"+this.getValue());
//
//            if (record.getValue().equals(this.value)){
//                System.out.println("equals+++");
//            }
//            return record.getValue().equals(this.value);
//        }
//        return false;
//    }
}
