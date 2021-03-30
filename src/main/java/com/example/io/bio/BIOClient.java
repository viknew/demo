package com.example.io.bio;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

/**
 * @author Administrator
 * @描述:
 * @公司: 哈工大业信息技术股份有限公司
 * @创建日期: 2020-04-23
 * @创建时间: 17:14
 */
public class BIOClient
{
    public static void main(String[] args) throws IOException
    {
        OutputStream outputStream = null;
        Socket socket = null;
        try{
            socket = new Socket("localhost", 8080);
            outputStream = socket.getOutputStream();
            outputStream.write("零零零零".getBytes());
            outputStream.flush();
        }
        catch (IOException e){
            e.printStackTrace();
        }
        finally
        {
            if (outputStream != null)
            {
                outputStream.close();
            }
            if (socket != null){
                socket.close();
            }
        }
    }
}
