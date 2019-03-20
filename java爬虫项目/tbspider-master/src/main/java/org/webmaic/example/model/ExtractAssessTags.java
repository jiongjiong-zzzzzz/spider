package org.webmaic.example.model;

import java.util.Date;

public class ExtractAssessTags {
    private Long id;

    private Long tagId;

    private Long goodsId;

    private String tagContent;

    private Integer tagNum;

    private String dataSource;

    private Date collectTime;
    //初步判断：当标签为红时该值为1(true)，否则为0(false)
    private  Integer posi;

    public Integer getPosi() {
        return posi;
    }

    public void setPosi(Integer posi) {
        this.posi = posi;
    }

    /**
     * @return ID
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return TAG_ID
     */
    public Long getTagId() {
        return tagId;
    }

    /**
     * @param tagId
     */
    public void setTagId(Long tagId) {
        this.tagId = tagId;
    }

    /**
     * @return GOODS_ID
     */
    public Long getGoodsId() {
        return goodsId;
    }

    /**
     * @param goodsId
     */
    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }

    /**
     * @return TAG_CONTENT
     */
    public String getTagContent() {
        return tagContent;
    }

    /**
     * @param tagContent
     */
    public void setTagContent(String tagContent) {
        this.tagContent = tagContent;
    }

    /**
     * @return TAG_NUM
     */
    public Integer getTagNum() {
        return tagNum;
    }

    /**
     * @param tagNum
     */
    public void setTagNum(Integer tagNum) {
        this.tagNum = tagNum;
    }

    /**
     * @return DATA_SOURCE
     */
    public String getDataSource() {
        return dataSource;
    }

    /**
     * @param dataSource
     */
    public void setDataSource(String dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * @return COLLECT_TIME
     */
    public Date getCollectTime() {
        return collectTime;
    }

    /**
     * @param collectTime
     */
    public void setCollectTime(Date collectTime) {
        this.collectTime = collectTime;
    }
}