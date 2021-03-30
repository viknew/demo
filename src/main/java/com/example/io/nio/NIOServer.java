package com.example.io.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * @author Administrator
 * @描述:
 * @公司: 哈工大业信息技术股份有限公司
 * @创建日期: 2020-04-23
 * @创建时间: 19:59
 */
public class NIOServer
{
    private static Selector selector = null;
    public static void main(String[] args)
    {
        ServerSocketChannel channel;
        try
        {
            channel = ServerSocketChannel.open();
            channel.bind(new InetSocketAddress(8080));
            channel.configureBlocking(false);
            selector = Selector.open();
            channel.register(selector, SelectionKey.OP_ACCEPT);
            listen();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private static void listen()
    {
        try
        {
            while (true){
                   selector.select();
                   Set<SelectionKey> kes = selector.selectedKeys();
                   Iterator<SelectionKey> ites = kes.iterator();
                   while (ites.hasNext()){
                       SelectionKey key = ites.next();
                       ites.remove();
                       process(key);
                   }
                }

        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private static void process(SelectionKey key) throws IOException
    {

        if (key.isAcceptable()){
            //数据准备就绪
            ServerSocketChannel serverSocketChannel = (ServerSocketChannel)key.channel();
            SocketChannel socketChannel = serverSocketChannel.accept();
            socketChannel.configureBlocking(false);
            key = socketChannel.register(selector,SelectionKey.OP_READ);
        }
        else if (key.isReadable()){
            System.out.println("55444444");
            ByteBuffer byteBuffer = ByteBuffer.allocate(20);
            SocketChannel socketChannel = (SocketChannel)key.channel();
            StringBuilder stringBuilder = new StringBuilder();
            int len = 0;
            while ((len = socketChannel.read(byteBuffer)) > 0){
                byteBuffer.flip();
                stringBuilder.append(new String(byteBuffer.array(),0,len));
                //byteBuffer.clear();
            }
            key = socketChannel.register(selector,SelectionKey.OP_WRITE);
            System.out.println(stringBuilder.toString());

        }
    }

}
