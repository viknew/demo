package com.example.io.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.ReferenceCountUtil;

/**
 * @author Administrator
 * @描述:
 * @公司: 哈工大业信息技术股份有限公司
 * @创建日期: 2020-04-23
 * @创建时间: 23:46
 */
public class MyServerHandler extends SimpleChannelInboundHandler<String>
{
    @Override
    protected void messageReceived(ChannelHandlerContext channelHandlerContext, String s) throws Exception
    {
        System.out.println("55555555555555555555555555555555555");
    }
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        boolean release = true;

        System.out.println("dddddddd");

        try {
            if (this.acceptInboundMessage(msg)) {
                this.messageReceived(ctx, (String)msg);
            } else {
                release = false;
                ctx.fireChannelRead(msg);
            }
        } finally {
            ReferenceCountUtil.release(msg);

        }

    }

}
