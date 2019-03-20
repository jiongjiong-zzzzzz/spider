#!usr/bin/python3
# -*- coding:utf-8 -*-
# 破解wifi
import time
import pywifi  #破解wifi
from pywifi import const  #引用一些定义
wifi = pywifi.PyWiFi()  # 抓取WiFi接口
iface = wifi.interfaces()[0]   # 抓取无线网卡列表
iface.scan() # 扫描
time.sleep(5)
bsses = iface.scan_results()    # 扫描到的结果
for pjwifi in bsses:
    print(pjwifi.ssid)  # 所有WiFi名
    print(pjwifi.bssid) # mac地址
    print(pjwifi.signal)    # 信号强度(值越大信号越强)

