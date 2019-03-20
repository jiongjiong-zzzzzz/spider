package cn.why.pojo;
/**
 * Title:           urlList
 * Description:     javabean封装url列表页标题及网址
 * Company:         AceGear
 * Author:          henrywang
 * Date:            2018/5/17
 * JDK:             10
 * Encoding:        UTF-8
 */
public class urlList {
    private String title; //标题
    private String link; //网址

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
