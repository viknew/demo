package com.example.observer;

import java.util.Observable;
import java.util.Observer;

/**
 * @author Administrator
 * @描述:
 */
public class Teacher implements Observer
{
    private String name;

    Teacher(String name){
        this.name = name;
    }

    @Override public void update(Observable o, Object arg)
    {
        Gper gper = (Gper) o;
        Question question = (Question) arg;
        System.out.println(name + "老师，您好，您在" + gper.getName() + "社区有问题需要处理,问题描述如下:/n"
        + question.getContent() + ",该问题提问者:" + question.getUserName());
    }
}
