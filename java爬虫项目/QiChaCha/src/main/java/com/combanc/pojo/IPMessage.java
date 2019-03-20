package com.combanc.pojo;

/**
 * @Title:           IPMessage
 * @Description:     代理ip
 * @Company:         combanc
 * @Author:          shihw
 * @Date:            2018/9/19
 * @JDK:             1.8
 * @Encoding:        UTF-8
 */
public class IPMessage{
    private String IPAddress;
    private String IPPort;
    private int useCount;          

    public IPMessage() { this.useCount = 0; }

    public IPMessage(String IPAddress, String IPPort) {
        this.IPAddress = IPAddress;
        this.IPPort = IPPort;
        this.useCount = 0;
    }

    public String getIPAddress() {
        return IPAddress;
    }

    public void setIPAddress(String IPAddress) {
        this.IPAddress = IPAddress;
    }

    public String getIPPort() {
        return IPPort;
    }

    public void setIPPort(String IPPort) {
        this.IPPort = IPPort;
    }


    public int getUseCount() {
        return useCount;
    }

    public void setUseCount() {
        this.useCount++;
    }

    public void initCount() {
        this.useCount = 0;
    }

    @Override
    public String toString() {
        return IPAddress+":"+IPPort;
    }
}
