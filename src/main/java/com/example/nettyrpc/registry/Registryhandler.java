package com.example.nettyrpc.registry;

import com.example.nettyrpc.api.IRpcHello;
import com.example.nettyrpc.protocol.RpcProtocol;
import com.example.nettyrpc.provider.RpcHelloImp;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelHandlerInvoker;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.concurrent.EventExecutorGroup;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Administrator
 * @描述:
 * @公司: 哈工大业信息技术股份有限公司
 * @创建日期: 2021-03-25
 * @创建时间: 23:57
 */
public class Registryhandler extends ChannelInboundHandlerAdapter
{
    private static Map<String, String> cacheMap = new ConcurrentHashMap<String, String>();

    static{
        cacheMap.put(IRpcHello.class.getName(), RpcHelloImp.class.getCanonicalName());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception
    {
        cause.printStackTrace();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception
    {
        RpcProtocol protocol = new RpcProtocol();
        protocol.setClassName(IRpcHello.class.getName());
        protocol.setMethodName("sayHello");
        protocol.setParamTypes(new Class[]{String.class});
        protocol.setValues(new Object[]{"liuwenwei"});
        if(cacheMap.containsKey(protocol.getClassName())){
            String s = cacheMap.get(protocol.getClassName());
            Class<?> t = ClassLoader.getSystemClassLoader().loadClass(s);
            Object o = t.newInstance();
            Method method = t.getMethod(protocol.getMethodName(),protocol.getParamTypes());
            method.invoke(o,protocol.getValues());
        }
        ctx.write("11111111111111");
        ctx.flush();
        ctx.close();

    }
}
