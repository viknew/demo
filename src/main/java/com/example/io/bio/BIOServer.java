package com.example.io.bio;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author Administrator
 * @描述:
 * @公司: 哈工大业信息技术股份有限公司
 * @创建日期: 2020-04-23
 * @创建时间: 17:04
 */
public class BIOServer
{
    public static void main(String[] args) throws IOException
    {
        ServerSocket serverSocket = new ServerSocket(8080);
        while (true)
        {
            try(Socket socket = serverSocket.accept())
            {
                process(socket);
            }
        }
    }

    private static void process(Socket socket) throws IOException
    {
        try(InputStream inputStream = socket.getInputStream())
        {
            byte[] bytes = new byte[1024];
            StringBuffer stringBuffer = new StringBuffer();
            while (inputStream.read(bytes) > 0)
            {
                stringBuffer.append(new String(bytes));
            }
            System.out.println(stringBuffer.toString());
        }
    }
}
