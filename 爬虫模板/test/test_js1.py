import requests
from lxml import etree
import hashlib
session = requests.Session()
headers = {'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.139 Safari/537.36',
            'Referer': 'http://glidedsky.com/level/crawler-basic-1',
            'Host': 'glidedsky.com',
            'Accept': 'text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8',
            'Accept-Encoding': 'gzip, deflate',
            'Accept-Language': 'zh-CN,zh;q=0.9',
           'cookie':'_ga=GA1.2.1174699020.1564731287; remember_web_59ba36addc2b2f9401580f014c7f58ea4e30989d=eyJpdiI6IkJiazVrNjhVN1JBTGhTTnZ3TFFTK0E9PSIsInZhbHVlIjoiUWNDQ3ZZWklFdVI5eWR2ZWphVnk2dWNVb1p1U21iN25Bc2hjYjZLMUtHOEdLaHFoTXBYSTFRXC96VHIyWUJzXC8zdU1HVnhmMFAyanRvb3I1OHBxMGFzK3R3WUJLTHFGV0MzSCtKZ3oxYzZaaHcyaFJcL1d4Q3JcL0c3NVdNQW5WeE1QaTFzMjdWQ2w3RHQ3Tm05cHFmTXhacEhqRCtYXC84SWJ1RmNLZ3Nxd0xCWTQ9IiwibWFjIjoiOTMxMGNjZWNlMTE0ZjhlYjA4Mjc3MTU3ZjhlM2UxZjE1M2I0MjE5MTA1NDQ2OTI5Y2UwMmY5NzkyMTgzMDJlZiJ9; Hm_lvt_020fbaad6104bcddd1db12d6b78812f6=1564731287,1564748703,1564970028; _gid=GA1.2.1256167534.1564970028; Hm_lpvt_020fbaad6104bcddd1db12d6b78812f6=1564972149; _gat_gtag_UA_75859356_3=1; XSRF-TOKEN=eyJpdiI6IktkZ1BndzdtM0NoS2VGZjZjNnZuS1E9PSIsInZhbHVlIjoiUGlna2tqRjBTOWVWR0xiY3Vqd25UYUdRU1lzZE9pd3pJZVJHdmVoUGdPdURFb3ZFWHdLWkZ2Zk13UnZHWXVpWiIsIm1hYyI6IjQ0NjYzYTAxZjIzODA1ZDUyZjZjODI0NzdhM2Y5MDRjNDFhZTUwNTk4M2I4ZDljMDc1NjkxNzYyYWE5ZmE2MDUifQ%3D%3D; glidedsky_session=eyJpdiI6IjJmZWdcL0xmNUZMTGxGZnJhZzJVY2RnPT0iLCJ2YWx1ZSI6Imk1SWRUemVCRVVNNzRLT1Zna2dGZHRDM1k1V3FZSG10TUFDMGVEMzhGck02S0V0NEZlMTBTOW9RZ0RiMGJTVzQiLCJtYWMiOiJhMTRhODkzZjFlOWNiMzcyN2I1NWZjYjNmZGRlMDY5NzQ1MzU2MTAxMDk4YzljNjQ2NzBmNGJiZGU5NGJlYzE5In0%3D'}
url = 'http://glidedsky.com/level/web/crawler-javascript-obfuscation-1?page={}'
session.headers.update(headers)
count = 0
import re,json
for i in range(1,1001):
    print(i)
    r = session.get(url.format(i))
    text = r.text
    t = re.search(r'<div class="container" p="\d+" t="(?P<t>\d+)">',text)
    try:
        t=int((int(t['t'])-99)/99)
    except:
        print(t)
    sign = 'Xr0Z-javascript-obfuscation-1' +str(t)
    sign = hashlib.sha1(sign.encode('utf-8')).hexdigest()
    print(sign)
    url_='http://glidedsky.com/api/level/web/crawler-javascript-obfuscation-1/items?page={}&t={}&sign={}'.format(i,t,sign)
    r = session.get(url_)
    text = r.text

    nums = json.loads(text)['items']

    for num in nums:
        count += int(num)
print(count)