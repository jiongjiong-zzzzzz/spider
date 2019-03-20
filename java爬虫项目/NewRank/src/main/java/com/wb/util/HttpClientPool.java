package com.wb.util;


import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.log4j.Logger;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.io.InputStream;
import java.security.NoSuchAlgorithmException;
import java.util.Properties;


/**
 * HttpClient连接池参数
 * Created by tyler on 2017/6/8.
 */
public class HttpClientPool {
    private static final Logger logger = Logger.getLogger(HttpClientPool.class);
    private static PoolingHttpClientConnectionManager cm = null;
    static String path = System.getProperty("user.dir");
	static String driverPath = path + "/conf.properties";
    static InputStream in = new HttpClientPool().getClass().getResourceAsStream(driverPath);
    static Properties prop = new Properties();
    static CloseableHttpClient httpClient = null;
    static {
//        try {
//            prop.load(in);
//        } catch (IOException e) {
//            logger.error("读取conf.properties文件发生异常");
//        }finally {
//            try {
//                if(in!=null){
//                    in.close();
//                }
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
        int maxTotal = Integer.parseInt("200");
        int maxPerRoute = Integer.parseInt("20");

        LayeredConnectionSocketFactory sslsf = null;
        try {
            sslsf = new SSLConnectionSocketFactory(SSLContext.getDefault());
        } catch (NoSuchAlgorithmException e) {
            logger.error("创建SSL连接失败");
        }
        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("https", sslsf)
                .register("http", new PlainConnectionSocketFactory())
                .build();
        cm =new PoolingHttpClientConnectionManager(socketFactoryRegistry);
        cm.setMaxTotal(maxTotal);
        cm.setDefaultMaxPerRoute(maxPerRoute);
    }

    public static CloseableHttpClient getHttpClient() {
        int connectTimeout = Integer.parseInt("10000");
        int connectionRequestTimeout = Integer.parseInt("10000");
        int socketTimeout = Integer.parseInt("10000");
        RequestConfig defaultRequestConfig = RequestConfig.custom()
                .setConnectTimeout(connectTimeout)
                .setSocketTimeout(socketTimeout)
                .setConnectionRequestTimeout(connectionRequestTimeout)
                .setCircularRedirectsAllowed(true)
                .build();

        httpClient = HttpClients.custom()
                .setConnectionManager(cm)
                .setDefaultRequestConfig(defaultRequestConfig)
                .build();

        return httpClient;
    }

    public static void closeHttpClient(){
        if(httpClient!=null){
            try {
                httpClient.close();
            } catch (IOException e) {
                logger.error("关闭httpclient连接异常");
            }
        }
    }
}
