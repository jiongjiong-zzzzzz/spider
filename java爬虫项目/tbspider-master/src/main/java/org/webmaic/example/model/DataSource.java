package org.webmaic.example.model;

public enum DataSource {
    TMall("TMall");


    private String desc;//对应名称
    /**
     * 私有构造,防止被外部调用
     * @param desc
     */
    private DataSource(String desc){
        this.desc=desc;
    }
    /**
     * 定义方法,返回描述,跟常规类的定义没区别
     * @return
     */
    public String getDesc(){
        return desc;
    }
}
