import requests,base64
from lxml import etree
from PIL import Image
session = requests.Session()
headers = {'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.139 Safari/537.36',
            'Referer': 'http://glidedsky.com/level/crawler-basic-1',
            'Host': 'glidedsky.com',
            'Accept': 'text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8',
            'Accept-Encoding': 'gzip, deflate',
            'Accept-Language': 'zh-CN,zh;q=0.9',
           }
# url = 'http://glidedsky.com/level/web/crawler-basic-1'
# session.headers.update(headers)
# r = session.get(url,headers=headers)
# print(r.headers['Set-Cookie'])
tagString = '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'
imgdata=base64.b64decode(tagString)
file=open( 'xueb.jpg','wb')
file.write(imgdata)
file.close()
im = Image.open('xuebi2.jpg')
imX,imY = im.size
avg = imX/10
print(avg)
xiabiao = dict(zip([x for x in range(0,10,1)],[avg/2 + x * avg for x in range(0,10,1)]))
print(xiabiao)

