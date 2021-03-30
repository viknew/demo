package com.example.io.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.CharsetUtil;

/**
 * @author Administrator
 * @描述:
 * @公司: 哈工大业信息技术股份有限公司
 * @创建日期: 2020-04-23
 * @创建时间: 23:10
 */
public class NettyServer
{

    public static void main(String[] args)
    {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try
        {

            ServerBootstrap sbs = new ServerBootstrap();
            sbs.group(bossGroup,workerGroup)
                            .channel(NioServerSocketChannel.class)
                            .option(ChannelOption.SO_BACKLOG,128)
                            .childOption(ChannelOption.SO_KEEPALIVE,true)
                            .handler(new LoggingHandler(LogLevel.DEBUG))
                            .childHandler(new ChannelInitializer<SocketChannel>()
                            {
                                @Override protected void initChannel(SocketChannel socketChannel) throws Exception
                                {

                                    socketChannel.pipeline()
                                                    .addLast(new LineBasedFrameDecoder(1024))
                                                    .addLast(new StringDecoder(CharsetUtil.UTF_8))
                                                    .addLast(new StringEncoder(CharsetUtil.UTF_8))
                                                    .addLast(new MyServerHandler());
                                }
                            });
            System.err.println("server 开启--------------");
            // 绑定端口，开始接受链接
            ChannelFuture cf = sbs.bind(8080).sync();

            // 开多个端口
            //          ChannelFuture cf2 = sbs.bind(3333).sync();
            //          cf2.channel().closeFuture().sync();

            // 等待服务端口的关闭；在这个例子中不会发生，但你可以优雅实现；关闭你的服务
            cf.channel().closeFuture().sync();
        }catch (Exception e){
            e.printStackTrace();
        }
        finally{
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }

    }
}
