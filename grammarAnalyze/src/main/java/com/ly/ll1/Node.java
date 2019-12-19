package com.ly.ll1;

/**
 * Created by BorisLiu on 2019/11/25
 */
public class Node {

    private String vnChar;
    private String vtChar;
    private String value;

    public Node(String vnChar, String vtChar, String value) {
        this.vnChar = vnChar;
        this.vtChar = vtChar;
        this.value = value;
    }

    public Node() {
    }

    public String getVnChar() {
        return vnChar;
    }

    public void setVnChar(String vnChar) {
        this.vnChar = vnChar;
    }

    public String getVtChar() {
        return vtChar;
    }

    public void setVtChar(String vtChar) {
        this.vtChar = vtChar;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Node{" +
                "vnChar='" + vnChar + '\'' +
                ", vtChar='" + vtChar + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
