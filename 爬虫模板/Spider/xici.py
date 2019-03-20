import requests
from bs4 import BeautifulSoup
import time
import aiohttp
import asyncio
import re
from lxml import etree


# 开始时间
t1 = time.time()
print('#' * 50)

url = "https://www.xicidaili.com/nn/"
# 请求头部
headers = {
    'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/67.0.3396.87 Safari/537.36'}
# 发送HTTP请求
req = requests.get(url, headers=headers)
# 解析网页
print(req.text)
tree_node = etree.HTML(req.text)
port_ =[]
ip_ = []
port_nodes = tree_node.xpath("//table[@id='ip_list']/tr/td[3]")
for port in port_nodes:
    port_.append(port.text)
ip_nodes = tree_node.xpath("//table[@id='ip_list']/tr/td[2]")
for ip in ip_nodes:
    ip_.append(ip.text)
ip_port = zip(ip_,port_)
for ip in ip_port:
    print(ip[0])
    print(ip[1])



t2 = time.time()  # 结束时间
print('总共耗时：%s' % (t2 - t1))
print('#' * 50)