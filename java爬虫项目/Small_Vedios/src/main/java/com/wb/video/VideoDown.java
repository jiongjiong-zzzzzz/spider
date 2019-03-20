package com.wb.video;

import org.apache.commons.io.FileUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.File;
import java.net.URL;

/**
 * Title:           VideoDown
 * Description:
 * Company:         AceGear
 * Author:          henrywang
 * Date:            2018/5/5
 * JDK:             10
 * Encoding:        UTF-8
 */
public class VideoDown extends Thread{
    //文件存储的文件夹
    private String folder;

    //图片地址
    private String url;
    private String fileName;
    public VideoDown(){
    }
    public VideoDown(String folder, String fileName, String url) {
        // TODO Auto-generated constructor stub
        this.fileName = fileName;
        this.folder=folder;
        this.url=url;
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
        super.run();
        try {
            Connection.Response document=Jsoup.connect(url)
                    .ignoreContentType(true)
                    .timeout(10000)
                    .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:60.0) Gecko/20100101 Firefox/60.0")
                    .execute();
            URL url2 = document.url();
            System.out.println(url2);
            File file = new File(folder + "/" + fileName + ".mp4");
            FileUtils.copyURLToFile(url2,file,100000,100000);


        } catch (Exception e) {
            // TODO: handle exception
        }


    }
}
