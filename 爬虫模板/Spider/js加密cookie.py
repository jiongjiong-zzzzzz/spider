#encoding=utf-8
import requests,re,bs4,time,sys,hashlib,uuid,time,json,base64,rsa,platform,datetime,os,urllib
import execjs
import frida
process = frida.get_usb_device().attach('com.example.seccon2015.rock_paper_scissors')
UserAgent = 'Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.139 Safari/537.36'

headers = {'User-Agent': UserAgent}
res = requests.get('http://www.mps.gov.cn/n2253534/n2253535/index.html', headers=headers)
#res.raise_for_status()
__jsluid = res.headers["Set-Cookie"].split(';')[0]
cookie1 = __jsluid# 解密
get_js = re.findall(r'<script>(.*?)</script>', res.text)[0].replace('eval', 'return')
resHtml = "function getClearance(){" + get_js + "};"
ctx = execjs.compile(resHtml)
# 一级解密结果
temp1 = ctx.call('getClearance')

s = 'var a' + temp1.split('document.cookie')[1].split("Path=/;'")[0]+"Path=/;';return a;"
s = re.sub(r'document.create.*?firstChild.href', '"{}"'.format('http://www.mps.gov.cn/n2253534/n2253535/index.html'), s)
# print s
resHtml = "function getClearance(){" + s + "};"
ctx = execjs.compile(resHtml)
# 二级解密结果
jsl_clearance = ctx.call('getClearance')
jsl_clearance = jsl_clearance.split(';')[0]
Cookie = 'maxPageNum5097045=258; __jsluid={}; zh_choose=n; {}'.format(__jsluid,jsl_clearance)
headers = {'User-Agent': UserAgent,
           'Cookie':Cookie}
res = requests.get('http://www.mps.gov.cn/n2253534/n2253535/index.html', headers=headers)
reg_content = res.content.decode('utf-8', 'ignore')
html_page = bs4.BeautifulSoup(reg_content, 'lxml')
infos = html_page.find(class_='sec_list').findAll('a')
for url in infos:
    url = 'http://www.court.gov.cn'+url['href']
    res = requests.get(url, headers=headers)
    res.raise_for_status()
    reg_content = res.content.decode('UTF-8')
    html_page = bs4.BeautifulSoup(reg_content, 'lxml')
    date = html_page.find(class_='txt_txt').text
    date1 = re.search(r"(\d{4}-\d{2})", date)
    date2 = re.search(r"(\d{2})", date)
    date = date1[0]+'-'+date2[0]
    infos = html_page.find(class_='text_content').findAll('p')
    print(infos)