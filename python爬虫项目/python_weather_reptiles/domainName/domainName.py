#!usr/bin/python3
# -*- coding:utf-8 -*-
# 爬取阿里云com指定位数域名信息
from urllib import request,parse
import itertools
import threading
import time

class myThread (threading.Thread):
    def __init__(self, threadID, name, counter):
        threading.Thread.__init__(self)
        self.threadID = threadID
        self.name = name
        self.counter = counter
    def run(self):
        print ("开始线程：" + self.name)
        getcom(self.counter)
        print ("退出线程：" + self.name)

def getcom(comtpye):
    headers = {
        'Accept':'application/json, text/plain, */*',
        'Accept-Encoding':'gzip, deflate',
        'Accept-Language':'zh-CN,zh;q=0.8',
        'Connection':'keep-alive',
        'Content-Length':'14', 
        'Content-Type':'application/x-www-form-urlencoded',
        'Referer':'http://10.1.2.151/',
        'User-Agent':'Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/48.0.2564.23 Mobile Safari/537.36'
    }
    # 总数
    total = 0
    # 已注册
    registered = 0 
    # 已注册列表
    registeredList = []
    # 未注册
    unregistered = 0 
    # 未注册列表
    unregisteredList = []
    for l in itertools.product(comtpye,repeat=1):
        url = "https://checkapi.aliyun.com/check/checkdomain?domain="+"".join(l)+".com&command=&token=Y7ad669e1a6ef0c8e3e86c2101074f81b&ua=&currency=&site=&bid=&_csrf_token=&callback=jsonp_1542073978070_43280"
        req = request.Request(url)  # GET方法
        page = request.urlopen(req).read()
        page = page.decode('utf-8')
        if eval(page[26:-2])['module'][0]['avail'] == 0:
            registered = int(registered) + 1  
            registeredList.append(eval(page[26:-2])['module'][0])
            reg = open('registeredList.txt','a+', encoding='utf-8')
            reg.write(str(eval(page[26:-2])['module'][0]))
            reg.write("\n")
        else:
            unregistered = int(unregistered) + 1
            unregisteredList.append( eval(page[26:-2])['module'][0])
            unreg = open('unregisteredList.txt','a+', encoding='utf-8')
            unreg.write(str(eval(page[26:-2])['module'][0]))
            unreg.write("\n")
        total = int(total) + 1

    totallist1 = open('totallist1.txt','a+', encoding='utf-8')
    totallist1.write("com总数"+total)
    totallist1.write("\n")
    totallist1.write("com已注册总数")
    totallist1.write(registered)
    totallist1.write("\n")
    totallist1.write("com已注册列表")
    totallist1.write(registeredList)
    totallist1.write("\n")
    totallist1.write("com未注册总数")
    totallist1.write(unregistered)
    totallist1.write("\n")
    totallist1.write("com未注册列表")
    totallist1.write(unregisteredList)

# 创建新线程
thread1 = myThread(1,"数字线程","0123456789")
thread2 = myThread(2,"字母线程","qwertyuiopasdfghjklzxcvbnm")

# 开启新线程
thread1.start()
thread2.start()

