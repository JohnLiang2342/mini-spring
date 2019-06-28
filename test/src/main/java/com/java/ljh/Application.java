package com.java.ljh;


import com.java.ljh.starter.MiniApplication;

public class Application {
    public static void main(String args[]){
        System.out.println("hello world!");
        MiniApplication.run(Application.class, args);//启动框架
    }
}
