package com.example.nettyrpc.registry;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.serialization.ClassResolver;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

import java.net.ServerSocket;

/**
 * @author Administrator
 * @描述:
 * @公司: 哈工大业信息技术股份有限公司
 * @创建日期: 2021-03-25
 * @创建时间: 23:37
 */
public class Registry
{
    private int port;

    public Registry(int port)
    {
        this.port = port;
    }

    private void start(){
        //启动服务
        try{
            EventLoopGroup bossGroup = new NioEventLoopGroup();
            EventLoopGroup workGroup = new NioEventLoopGroup();
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup,workGroup)
                            .channel(NioServerSocketChannel.class)
                            .childHandler(new ChannelInitializer<SocketChannel>()
                            {
                                @Override
                                protected void initChannel(SocketChannel socketChannel) throws Exception
                                {
                                    ChannelPipeline pipeline = socketChannel.pipeline();
                                    //编解码
                                    /*.addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE,0,4,0,4))
                                                .addLast(new LengthFieldPrepender(4))
                                                .addLast("encoder",new ObjectEncoder())
                                                .addLast("decoder",new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.cacheDisabled(null)))*/
                                    pipeline.addLast(new Registryhandler());
                                }
                            })
                            .option(ChannelOption.SO_BACKLOG,128)
                            .childOption(ChannelOption.SO_KEEPALIVE,true);

            ChannelFuture sync = serverBootstrap.bind(this.port).sync();
            sync.channel().closeFuture().sync();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public static void main(String[] args)
    {
        Registry registry = new Registry(8080);
        registry.start();
    }
}
