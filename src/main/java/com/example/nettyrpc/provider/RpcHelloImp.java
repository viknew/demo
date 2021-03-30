package com.example.nettyrpc.provider;

import com.example.nettyrpc.api.IRpcHello;

/**
 * @author Administrator
 * @描述:
 * @公司: 哈工大业信息技术股份有限公司
 * @创建日期: 2021-03-25
 * @创建时间: 23:34
 */
public class RpcHelloImp implements IRpcHello
{
    @Override
    public String sayHello(String name)
    {
        return "hello" + name + "!";
    }
}
