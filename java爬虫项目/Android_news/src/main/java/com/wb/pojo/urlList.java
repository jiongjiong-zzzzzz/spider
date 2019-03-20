package com.wb.pojo;
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
    private String key;  
    private int flag;  
    public urlList(String link, String key, int flag, int cid) {
		super();
		this.link = link;
		this.key = key;
		this.flag = flag;
		this.cid = cid;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public int getCid() {
		return cid;
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

	public void setCid(int cid) {
		this.cid = cid;
	}

	private int cid; // 

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
