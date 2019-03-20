package com.wb.util;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.LinkedList;
import java.util.List;


/**
 * 根据对应URL下载对应json
 */
public class URLUtil
{

    private static Logger logger=Logger.getLogger(URLUtil.class);
    /**
     * 获取gb2312字符集的汉字列表
     * @return
     */
    public static  List<String>  getGB2312List()
    {
        List<String> result=new LinkedList<String>();

        //遍历数字集
        for (int i=0;i<10;i++)
        {
            result.add(""+i);
        }

        byte[] tmp=new byte[1];
        //遍历字母集
        for (int i=(int)'A';i<=(int)'z';i++)
        {
            tmp[0]=(byte) i;
            String b= null;
            try
            {
                b = new String(tmp,"ASCII");
            } catch (UnsupportedEncodingException e)
            {
                e.printStackTrace();
            }
            result.add(b);
        }

        StringBuffer buffer=new StringBuffer();
        buffer.append("0123456789");
        buffer.append("qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM");
        //对gb2312字符集遍历
        try
        {
            //遍历gb2312汉字编码分区
            for (int i=0xB0;i<0xF7;i++)
            {
                //遍历每个分区中的汉字
                for (int j=0xA1;j<0xFF;j++)
                {
                    byte[] bytes=new byte[2];
                    bytes[0]= (byte) i;
                    bytes[1]= (byte) j;

                    String s = new String(bytes, "gb2312");
                    result.add(s);
                }

            }
        } catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        return result;
    }


    /**
     *  通过jdk8的util类中的Base64类实现 base64编码
     * @param plainText
     * @return
     */
    public static String getEncodedBase64(String plainText)
    {
        String encoded = null;
        try
        {
            byte[] bytes = plainText.getBytes("UTF-8");
            encoded = Base64.getEncoder().encodeToString(bytes);
        } catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        return encoded;
    }
}
