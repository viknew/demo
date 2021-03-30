package com.example.niochart;

import java.io.IOException;
import java.io.Writer;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

/**
 * @author Administrator
 * @描述:
 * @公司: 哈工大业信息技术股份有限公司
 * @创建日期: 2021-03-30
 * @创建时间: 23:00
 */
public class NIOChartClient
{
    private final InetSocketAddress inetSocketAddress = new InetSocketAddress("localhost",8080);
    private Charset charset = Charset.forName("UTF-8");
    private static String USER_EXITS = "系统提示:该昵称已经存在，请换一个昵称";
    private static String USER_CONTENT_SPLIT = "#@#";
    private String nickname = "";
    private Selector selector = null;
    private SocketChannel client = null;

    public static void main(String[] args)
    {
        new NIOChartClient().session();
    }

    public NIOChartClient()
    {
        try {
            selector  = Selector.open();
            client = SocketChannel.open(inetSocketAddress);
            client.configureBlocking(false);
            client.register(selector, SelectionKey.OP_READ);
            System.out.println("请输入昵称:");
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private void session()
    {
        new Reader().start();
        new Writer().start();
    }

    private class Reader extends Thread{
        @Override
        public void run()
        {
            try{
                while (true){
                    int select = selector.select();
                    if(select != 0){
                        Set<SelectionKey> keys = selector.keys();
                        Iterator<SelectionKey> iterator = keys.iterator();
                        while (iterator.hasNext()){
                            SelectionKey key = iterator.next();
                            iterator.remove();
                            process(key);
                        }
                    }
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }

        private void process(SelectionKey key)
        {
            if(key.isReadable()){
                //可读状态
                try{
                    SocketChannel channel = (SocketChannel)key.channel();
                    ByteBuffer buffer = ByteBuffer.allocate(1024);
                    String content = "";
                    while (channel.read(buffer) > 0){
                        buffer.flip();
                        content += charset.decode(buffer);
                    }
                    if(USER_EXITS.equals(content)){
                        nickname = "";
                    }
                    System.out.println(content);
                    key.interestOps(SelectionKey.OP_READ);
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
    }

    private class Writer extends Thread{
        @Override
        public void run()
        {

            try{
                Scanner scanner = new Scanner(System.in);
                while (scanner.hasNextLine()){
                    String message = scanner.next();
                    if("".equals(message)){
                        continue;
                    }
                    if("".equals(nickname)){
                        nickname = message;
                        message = nickname + USER_CONTENT_SPLIT;
                    }else{
                        message = nickname + USER_CONTENT_SPLIT + message;
                    }
                    client.write(charset.encode(message));
                }
                scanner.close();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }
}
