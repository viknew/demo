package com.example.observer;

import java.util.Observable;

/**
 * @author Administrator
 * @描述:
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
