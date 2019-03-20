#!usr/bin/python3
# -*- coding:utf-8 -*-
# 爬取阿里云com指定位数域名信息
from urllib import request,parse
import itertools
import threading
import time

def savecom():
    comtpye = 'abcdefghijklmnopqrstuvwxyz'
    print("开始分析数据，请稍等...")
    starttime = time.time()
    for l in itertools.product(comtpye,repeat=2):
        of = open('totallist.txt','r',encoding='utf-8')
        a = of.read()
        totallist = eval(a).split(",")
        of.close()
        wf = open('totallist.txt','w+',encoding='utf-8')
        wa = wf.read()
        totallist.append("".join(l))
        totallist = '"'+",".join(totallist)+'"'
        wf.write(totallist)
        wf.close()
     
    # 循环结束  
    endtime = time.time()
    # 计算时间
    totaltime = endtime - starttime   
    print("本次解析结束数据耗时：%s"%(totaltime))
    # print(totaltime)
    print("进行数据解析")

if __name__ == '__main__':
    # 创建totallist.txt
    totallist = open('totallist.txt','w+', encoding='utf-8')
    totallist.write("\"nmgwap\"")
    totallist.close()
    savecom()
