#!usr/bin/python3
# -*- coding:utf-8 -*-
# 爬取阿里云com指定位数域名信息
from getdomainName import savecom
from regcom import getcom
import threading

def getlistdata():
        print("数据解析开始...")
        of = open('totallist.txt','r',encoding='utf-8')
        a = of.read()
        totallist = eval(a).split(",")
        of.close()
        # 判断数据len
        if len(totallist) < 100000:
            print("数据量比较小，单线程工作")
            t1 = threading.Thread(target=getcom,args=(totallist,0,len(totallist)))
            t1.start()
        else :
            print("数据量比较大，拆分多个线程")
            Threadlist = {}
            i = 0
            while i < (len(totallist)//100000) + 1:
                strNo = "t"+str(i)
                Threadlist[strNo]=i
                i = i+1
            for key in Threadlist:
                if Threadlist[key]*100000+100000 > len(totallist):
                    print("线程%s开始工作"%(key))
                    key = threading.Thread(target=getcom,args=(totallist,Threadlist[key]*100000,len(totallist)))
                    # print(Threadlist[key]*100000)
                    # print(len(totallist))
                    print("最后的")
                    key.start()
                else:
                    print("线程%s开始工作"%(key))
                    key = threading.Thread(target=getcom,args=(totallist,Threadlist[key]*100000,Threadlist[key]*100000+100000))
                    # print(Threadlist[key]*100000)
                    # print(Threadlist[key]*100000+100000)
                    key.start()

if __name__ == '__main__':
    # 创建totallist.txt
    totallist = open('totallist.txt','w+', encoding='utf-8')
    totallist.write("\"nmgwap\"")
    totallist.close()
    # 获取组合总数据
    savecom()
    # 分析总数据，拆分线程
    getlistdata()