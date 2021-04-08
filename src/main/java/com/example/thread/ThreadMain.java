package com.example.thread;

/**
 * @author Administrator
 * @描述:
 * @公司: 哈工大业信息技术股份有限公司
 * @创建日期: 2021-04-02
 * @创建时间: 22:46
 */
public class ThreadMain
{
    public static void main(String[] args)
    {
        Thread t = new MyThread();
        t.start();
        t.interrupt();
        System.out.println("first interrupt myThread:" + t.isInterrupted());
        System.out.println("second interrupt myThread:" + t.isInterrupted());
        System.out.println("first interrupt myThread:" + Thread.currentThread().isInterrupted());
        System.out.println("second interrupt myThread:" + Thread.currentThread().isInterrupted());
    }

    private static class MyThread extends Thread{
        @Override public void run()
        {
            try
            {
                System.out.println("start sleep");
                int i=0;
                while (i<200000){
                    if(i%1000 == 0){
                        System.out.println("第几次 ： " + i);
                    }
                    i++;
                }
                Thread.currentThread().sleep(10000);
                System.out.println("end sleep");
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
    }

}
