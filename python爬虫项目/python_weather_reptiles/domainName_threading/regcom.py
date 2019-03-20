#!usr/bin/python3
# -*- coding:utf-8 -*-
# 爬取阿里云com指定位数域名信息
from urllib import request,parse
import itertools
import threading
import time

def getcom(listdata,startNo,endNo):
    print("开始请求数据")
    print(startNo)
    print(endNo)
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
    print("开始查询，请稍等...")
    for num in range(startNo,endNo):
        url = "https://checkapi.aliyun.com/check/checkdomain?domain="+listdata[num]+".com&command=&token=Y7ad669e1a6ef0c8e3e86c2101074f81b&ua=&currency=&site=&bid=&_csrf_token=&callback=jsonp_1542073978070_43280"
        req = request.Request(url)  # GET方法
        page = request.urlopen(req).read()
        page = page.decode('utf-8')
        print(page)
        if eval(page[26:-2])['success'] == 'true':
            if eval(page[26:-2])['module'][0]['avail'] == 0:
                registered = int(registered) + 1  
                registeredList.append(eval(page[26:-2])['module'][0])
                reg = open('registeredList.txt','a+', encoding='utf-8')
                reg.write(str(eval(page[26:-2])['module'][0]))
                reg.write("\n")
                reg.close()
                print("===>>域名：%s.com已经被注册,查询下一个 \n"%(listdata[num]))
            else:
                unregistered = int(unregistered) + 1
                unregisteredList.append( eval(page[26:-2])['module'][0])
                unreg = open('unregisteredList.txt','a+', encoding='utf-8')
                unreg.write(str(eval(page[26:-2])['module'][0]))
                unreg.write("\n")
                unreg.close()
                print("===>>域名：%s.com还没有被注册,查询下一个 \n"%(listdata[num]))
        else:
            registered = int(registered) + 1  
            registeredList.append(eval(page[26:-2]))
            reg = open('registeredList.txt','a+', encoding='utf-8')
            reg.write(str(eval(page[26:-2])))
            reg.write("\n")
            reg.close()
            print("===>>域名：%s.com查询失败,查询下一个 \n"%(listdata[num]))
        total = int(total) + 1

    totalDataList = open('totalDataList.txt','a+', encoding='utf-8')
    totalDataList.write("com总数:")
    totalDataList.write(str(total))
    totalDataList.write("\n")
    totalDataList.write("com已注册总数")
    totalDataList.write(str(registered))
    totalDataList.write("\n")
    totalDataList.write("com已注册列表")
    totalDataList.write(str(registeredList))
    totalDataList.write("\n")
    totalDataList.write("com未注册总数")
    totalDataList.write(str(unregistered))
    totalDataList.write("\n")
    totalDataList.write("com未注册列表")
    totalDataList.write(str(unregisteredList))
    totalDataList.close()
    print("工作结束")


if __name__ == '__main__':
    list = ["qwert","qazws","qwesdd","qwesdd","qwesdd","qwesdd","qwesdd","qwesdd","qwesdd","qwesdd"]
    getcom(list,1,7)
