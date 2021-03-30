package com.example.observer;

import java.util.Observable;

/**
 * @author Administrator
 * @描述:
 * @公司: 哈工大业信息技术股份有限公司
 * @创建日期: 2020-05-10
 * @创建时间: 00:21
 */
public class Gper extends Observable
{

    private static Gper gper = null;

    private static String name = "咕泡生态圈";

    private void Gper(){}

    public static Gper getInstance(){
        if (gper == null){
            gper = new Gper();
        }
        return gper;
    }

    public String getName(){
        return name;
    }

    public void publishQuestion(Question question){
        setChanged();
        notifyObservers(question);
    }

}
