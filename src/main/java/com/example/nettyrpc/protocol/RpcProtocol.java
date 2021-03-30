package com.example.nettyrpc.protocol;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Administrator
 * @描述:
 * @公司: 哈工大业信息技术股份有限公司
 * @创建日期: 2021-03-25
 * @创建时间: 23:35
 */
@Data
public class RpcProtocol implements Serializable
{
    private String className;

    private String methodName;

    private Class<?> [] paramTypes;

    private Object[] values;
}
