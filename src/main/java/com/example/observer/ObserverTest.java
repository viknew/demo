package com.example.observer;

/**
 * @author Administrator
 * @描述:
 * @公司: 哈工大业信息技术股份有限公司
 * @创建日期: 2020-05-10
 * @创建时间: 20:34
 */
public class ObserverTest
{
    public static void main(String[] args)
    {
        Gper gper = Gper.getInstance();
        Question question = new Question();
        question.setContent("什么是观察者模式?");
        question.setUserName("谢谢");
        Teacher teacher = new Teacher("liu");
        gper.addObserver(teacher);
        gper.publishQuestion(question);
    }
}
