import requests
from lxml import etree
import re
import requests,base64
from lxml import etree
from PIL import Image
from news.mongodb2 import  insert
session = requests.Session()
headers = {'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.139 Safari/537.36',
            'Referer': 'http://glidedsky.com/level/crawler-basic-1',
            'Host': 'glidedsky.com',
            'Accept': 'text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8',
            'Accept-Encoding': 'gzip, deflate',
            'Accept-Language': 'zh-CN,zh;q=0.9',
           'cookie':'_ga=GA1.2.1174699020.1564731287; remember_web_59ba36addc2b2f9401580f014c7f58ea4e30989d=eyJpdiI6IkJiazVrNjhVN1JBTGhTTnZ3TFFTK0E9PSIsInZhbHVlIjoiUWNDQ3ZZWklFdVI5eWR2ZWphVnk2dWNVb1p1U21iN25Bc2hjYjZLMUtHOEdLaHFoTXBYSTFRXC96VHIyWUJzXC8zdU1HVnhmMFAyanRvb3I1OHBxMGFzK3R3WUJLTHFGV0MzSCtKZ3oxYzZaaHcyaFJcL1d4Q3JcL0c3NVdNQW5WeE1QaTFzMjdWQ2w3RHQ3Tm05cHFmTXhacEhqRCtYXC84SWJ1RmNLZ3Nxd0xCWTQ9IiwibWFjIjoiOTMxMGNjZWNlMTE0ZjhlYjA4Mjc3MTU3ZjhlM2UxZjE1M2I0MjE5MTA1NDQ2OTI5Y2UwMmY5NzkyMTgzMDJlZiJ9; _gid=GA1.2.1282572402.1565244426; Hm_lvt_020fbaad6104bcddd1db12d6b78812f6=1565070543,1565088436,1565244426,1565263278; XSRF-TOKEN=eyJpdiI6IjFHdFRPSWdSNUh4RTVDeEhlakRkSWc9PSIsInZhbHVlIjoiRkNFRlR0cFwveUpFMW9RdGVEK29PTFVuMXR1eCt3NlJGNzJ4dVdRMzJpYlYwVXBrWm93cGw5K09yenpwYTJvcFkiLCJtYWMiOiI1Zjg5YTcyNzRlMjcyZTI3ZGM3Y2MzMjBkMTFlYmVkODExODA0OWZmMzI1MWRiYzg3MjA3MjhmYjU4Mzg1MmNiIn0%3D; glidedsky_session=eyJpdiI6IjdzeXRQTkpqTXVpV0xzdVBSNitERXc9PSIsInZhbHVlIjoiNHJ3UzBrVkpRaklEanMwMGs0UVlpdkRpeExDSjJlRnVMTEFDTUdBczJ5OXJrM01wbFN1U2VIbEdvNWxYcFdoSCIsIm1hYyI6IjcyOTY2NTEyNTIxMjgzYmU0MTdhZjcyZmUxNWY3ZDI2NzI2MzhlYTkxYzU2ZDgxMThjMjAwNDgzZTA0NzI4NmYifQ%3D%3D; Hm_lpvt_020fbaad6104bcddd1db12d6b78812f6=1565340925'}
url = 'http://glidedsky.com/level/web/crawler-sprite-image-1?page={}'
session.headers.update(headers)
count = 0
for i in range(1,1001):
    print(str(i), end=":")
    r = session.get(url.format(i),headers=headers)
    text = r.text
    item = {'id_': i, 'html': text }
    insert(item)
    tagString = re.search(r'data:image/png;base64,(?P<url>.+?)\"\)',text)
    imgdata = base64.b64decode(tagString['url'])
    filename = 'img'+str(i)
    file = open('E:/img/2/{}.jpg'.format(i), 'wb')
    file.write(imgdata)
    file.close()
    im = Image.open('E:/img/2/{}.jpg'.format(i))
    imX, imY = im.size
    avg = imX / 10
    xiabiao = dict(zip([x for x in range(0, 10, 1)], [avg / 2 + x * avg for x in range(0, 10, 1)]))
    html = etree.HTML(text)
    nums = html.xpath("//div[@class='col-md-1']")
    for div_list in nums:
        divs = div_list.xpath("./div")
        list_ = []
        for div in divs:
            class_name = div.xpath("./@class")[0].split(' ')[0]
            left = re.findall(r"\.%s \{ background-position-x\:(.*?)px.+?\}" % class_name, text)[0]
            for k,v in xiabiao.items():
                if int(v)+int(left)>0:
                    list_.append(str(k))
                    break
        num ="".join(list_)
        print(num, end=",")
        count += int(num)
    print()
print(count)