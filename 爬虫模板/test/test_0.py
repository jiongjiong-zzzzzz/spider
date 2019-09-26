import requests
from lxml import etree
session = requests.Session()
headers = {'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.139 Safari/537.36',
            'Referer': 'http://glidedsky.com/level/crawler-basic-1',
            'Host': 'glidedsky.com',
            'Accept': 'text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8',
            'Accept-Encoding': 'gzip, deflate',
            'Accept-Language': 'zh-CN,zh;q=0.9',
           }
url = 'http://glidedsky.com/level/web/crawler-basic-1'
session.headers.update(headers)
r = session.get(url,headers=headers)
print(r.headers['Set-Cookie'])

