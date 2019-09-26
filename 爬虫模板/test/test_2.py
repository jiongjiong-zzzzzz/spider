import requests
from lxml import etree
session = requests.Session()
headers = {'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.139 Safari/537.36',
            'Referer': 'http://glidedsky.com/level/crawler-basic-1',
            'Host': 'glidedsky.com',
            'Accept': 'text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8',
            'Accept-Encoding': 'gzip, deflate',
            'Accept-Language': 'zh-CN,zh;q=0.9',
           'cookie':'Hm_lvt_020fbaad6104bcddd1db12d6b78812f6=1564047267; _ga=GA1.2.1976315618.1564047267; _gid=GA1.2.925251583.1564047267; XSRF-TOKEN=eyJpdiI6InBtb3dJZ3Z2NllLNjRvZVJ0Vis1U0E9PSIsInZhbHVlIjoiWWs2NlZwOTlHM2hpaVpCa0ZHZ0RZcEJkbW1xTVduTVlTUFNvcEJmbUk4ejlHYURxK3ZPNXF2XC9pU0o4anlhbDMiLCJtYWMiOiI2MmEyNmNmMDc5MjQ4OTIwNDBkZDMxNTVmMzUzOWYwZDU0YTYwMzdiM2IwOTBhMWUzZWU0ZDFiODY3YzM4ZmM4In0%3D; glidedsky_session=eyJpdiI6ImczTjBla1ZJSUptb0lTeEJHK2Vqb2c9PSIsInZhbHVlIjoiY2p5V2dLbFRHRnBYK0JPdWJNaUlXZnFHbWVMUTh6eHdWcGozVXFtRldSaVBqcStGSWpHbUdTdUN6QkhyS3haMCIsIm1hYyI6ImJhMzNkODQ1MTBkZmUyMDVkNjUwZTE1NThlY2Y2ZDg5MzdiZjVhMWQ2ZThhOGVmNTU2NDQyMjk4ZTE2NGJiYWMifQ%3D%3D; Hm_lpvt_020fbaad6104bcddd1db12d6b78812f6=1564050733'}
url = 'http://glidedsky.com/level/web/crawler-basic-2?page={}'
session.headers.update(headers)
count = 0
for i in range(1,1001):
    print(i)
    r = session.get(url.format(i),headers=headers)
    text = r.text
    html = etree.HTML(text)
    nums = html.xpath("//div[@class='col-md-1']/text()")
    import re

    for num in nums:
        num = re.findall(r"\d+", num)
        count += int(num[0])
print(count)