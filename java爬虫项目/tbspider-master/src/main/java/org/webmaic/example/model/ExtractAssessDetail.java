package org.webmaic.example.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

public class ExtractAssessDetail {

    private Long assessId;

    private Long goodsId;

    private String assessContent;

    private Date assessTime;

    private String assessSku;

    private String haveImage;

    private String dataSource;

    private Date collectTime;

    private Integer isExtract;

    public Integer getIsExtract() {
        return isExtract;
    }

    public void setIsExtract(Integer isExtract) {
        this.isExtract = isExtract;
    }

    public Long getAssessId() {
        return assessId;
    }

    public void setAssessId(Long assessId) {
        this.assessId = assessId;
    }

    public Long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }

    public String getAssessContent() {
        return assessContent;
    }

    public void setAssessContent(String assessContent) {
        this.assessContent = assessContent;
    }

    public Date getAssessTime() {
        return assessTime;
    }

    public void setAssessTime(Date assessTime) {
        this.assessTime = assessTime;
    }

    public String getAssessSku() {
        return assessSku;
    }

    public void setAssessSku(String assessSku) {
        this.assessSku = assessSku;
    }

    public String getHaveImage() {
        return haveImage;
    }

    public void setHaveImage(String haveImage) {
        this.haveImage = haveImage;
    }

    public String getDataSource() {
        return dataSource;
    }

    public void setDataSource(String dataSource) {
        this.dataSource = dataSource;
    }

    public Date getCollectTime() {
        return collectTime;
    }

    public void setCollectTime(Date collectTime) {
        this.collectTime = collectTime;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
}
