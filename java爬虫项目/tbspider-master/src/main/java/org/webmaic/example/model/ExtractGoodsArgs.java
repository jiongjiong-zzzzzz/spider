package org.webmaic.example.model;

import java.util.Date;

public class ExtractGoodsArgs {
    private Integer id;

    private Long goodsId;

    private String argsName;

    private String argsValues;

    private String dataSource;

    private Date collectTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }

    public String getArgsName() {
        return argsName;
    }

    public void setArgsName(String argsName) {
        this.argsName = argsName == null ? null : argsName.trim();
    }

    public String getArgsValues() {
        return argsValues;
    }

    public void setArgsValues(String argsValues) {
        this.argsValues = argsValues == null ? null : argsValues.trim();
    }

    public String getDataSource() {
        return dataSource;
    }

    public void setDataSource(String dataSource) {
        this.dataSource = dataSource == null ? null : dataSource.trim();
    }

    public Date getCollectTime() {
        return collectTime;
    }

    public void setCollectTime(Date collectTime) {
        this.collectTime = collectTime;
    }
}