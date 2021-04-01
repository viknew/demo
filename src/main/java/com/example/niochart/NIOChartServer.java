package com.example.niochart;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * @author Administrator
 * @描述:
 */
public class NIOChartServer
{
    private int port = 8080;
    private Charset charset = Charset.forName("UTF-8");

    private static HashSet<String> suers = new HashSet<>();

    private static String USER_EXITS = "系统提示:该昵称已经存在，请换一个昵称";
    private static String USER_CONTENT_SPLIT = "#@#";
    private Selector selector = null;

    public NIOChartServer(int port)
    {
        this.port = port;
        try
        {
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.bind(new InetSocketAddress(this.port));
            serverSocketChannel.configureBlocking(false);
            selector = Selector.open();
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            System.out.println("服务已启动！端口号:" + this.port);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args)
    {
        new NIOChartServer(8080).listen();
    }

    private void listen()
    {
        while (true){
            try
            {
                int select = selector.select();
                if(select == 0){
                    continue;
                }
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectionKeys.iterator();
                while (iterator.hasNext()){
                    SelectionKey next = iterator.next();
                    iterator.remove();
                    process(next);
                }
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    /**
     * 具体处理逻辑
     * @param next
     */
    private void process(SelectionKey next) throws IOException
    {
        if(next.isAcceptable()){
            //链接就绪状态
            ServerSocketChannel channel = (ServerSocketChannel)next.channel();
            SocketChannel client = channel.accept();
            client.configureBlocking(false);
            client.register(selector,SelectionKey.OP_READ);
            next.interestOps(SelectionKey.OP_ACCEPT);
            client.write(charset.encode("请输入您的昵称!"));
        }
        if(next.isReadable()){
            SocketChannel client = (SocketChannel) next.channel();
            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
            StringBuilder stringBuilder = new StringBuilder();
            try{
                while (client.read(byteBuffer) != 0){
                    byteBuffer.flip();
                    stringBuilder.append(charset.decode(byteBuffer));
                }
                next.interestOps(SelectionKey.OP_READ);
            }catch (IOException e){
                next.cancel();
                if(next.channel() != null){
                    next.channel().close();
                }
            }

            if(stringBuilder.length() > 0){
                String[] split = stringBuilder.toString().split(USER_CONTENT_SPLIT);
                if(split != null && split.length == 1){
                     String nickname = split[0];
                     if(suers.contains(nickname)){
                         //该昵称已经登陆过
                         client.write(charset.encode(USER_EXITS));
                     }else{
                         suers.add(nickname);
                         //通知所有在线人
                         int onlineCount = onlineCount();
                         String message = "欢迎 " + nickname + " 进入聊天室！ 当前在线人数:" + onlineCount + "人！";
                         broadCast(null,message);

                     }
                }else if(split != null && split.length > 1){
                    //发送了聊天内容
                    String nickname = split[0];
                    String message = stringBuilder.substring(nickname.length() + USER_CONTENT_SPLIT.length());
                    message = nickname + " 说 " + message;
                    if(suers.contains(nickname)){
                        broadCast(client,message);
                    }
                }
            }


        }
    }

    private void broadCast(Object o, String message)
    {
        try{
            for (SelectionKey key : selector.keys())
            {
                java.nio.channels.Channel channel = key.channel();
                if(channel instanceof SocketChannel && channel != o){
                    ((SocketChannel)channel).write(charset.encode(message));
                }
            }
        }catch (IOException e){
            e.printStackTrace();
        }

    }

    private int onlineCount()
    {
        int i = 0;
        for (SelectionKey key : selector.keys())
        {
            java.nio.channels.Channel channel = key.channel();
            if(channel instanceof SocketChannel){
                i++;
            }
        }
        return i;
    }
}
