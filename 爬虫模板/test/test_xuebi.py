import requests
from lxml import etree
import re
import requests,base64
from lxml import etree
from PIL import Image
from news.mongodb import  insert
session = requests.Session()
headers = {'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.139 Safari/537.36',
            'Referer': 'http://glidedsky.com/level/crawler-basic-1',
            'Host': 'glidedsky.com',
            'Accept': 'text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8',
            'Accept-Encoding': 'gzip, deflate',
            'Accept-Language': 'zh-CN,zh;q=0.9',
           'cookie':'_ga=GA1.2.1174699020.1564731287; remember_web_59ba36addc2b2f9401580f014c7f58ea4e30989d=eyJpdiI6InRzUW9qYVE5YXljN0lxVmNDTHBxM2c9PSIsInZhbHVlIjoiS3NBdmZ4RnNIVXZGaEduVGNpenh0bUlBNjFvdEkyOGJJUVNHVnRKUWg5ajNCSGhxTEEyM3pOazdwRGJBYjMxSW5qQ211ZHN1MHk0djFySUdUOWhwU2tRMVwvb1VmWE53WjRuejY2VDhlSUxvMDBRY3ptRzRjTEVJZGNPUDVwZUNLVjZkVVkzYnpheDJnV0tRNmxTN0ZVbjVhSXpXR3NTYW5jMGFrWHJIUEtOUT0iLCJtYWMiOiI0NGE3NjhhNjFlODNjZWE5MzA3YzUxNDkyZjYzODdiYmRiNmNjNTUzNmU1YTdkMTljMGM2NGFlODRkYWRiZTIwIn0%3D; Hm_lvt_020fbaad6104bcddd1db12d6b78812f6=1566716191,1566885960,1567231285,1567474535; _gid=GA1.2.1384802975.1567474535; XSRF-TOKEN=eyJpdiI6IjRZbFB5aUN0R0VUOTFjNmRVb21peWc9PSIsInZhbHVlIjoiYWV3b0ZRbkJyaklLcUx6WndpaGRmS0tSTWVNZkF0TkV2cTBjN1wvVXVHaE1EXC9vdjY1SEs3ajVYaHZ6YWhndURiIiwibWFjIjoiZTYyYzY4ZWEyZmJjNWNmNWJlMmQwYzRhZTI3MzcwYzY4Y2U5NmFkMjIzMDY4N2E0NTRlMTYyZTliNWNiOTAyMyJ9; glidedsky_session=eyJpdiI6Im5hdnlvYkd2c0F6bnJ5ZE1nN3FLK2c9PSIsInZhbHVlIjoiZzlSNW9vY01ZYkhieGVnbzhGY0xzWFlocm1uY3E2eU1tXC9UVVAyZTlUdlVoU0szeHZFMjRwS3ErWWYxbWZIc20iLCJtYWMiOiI4OWFkYmQwNGMyYzJmZDJjNDVhNjUyMjM4ZTVkNGQ5NjgxODU0ZmJjMjJkYWUxZmZkYzRhZTdlMGJiMzMxMWJhIn0%3D; Hm_lpvt_020fbaad6104bcddd1db12d6b78812f6=1567476424'}
url = 'http://glidedsky.com/level/web/crawler-sprite-image-2?page={}'
session.headers.update(headers)
count = 0
for i in range(1,1001):
    print(str(i), end=":")
    r = session.get(url.format(i),headers=headers)
    text = r.text
    item = {'id_': i, 'html': text}
    insert(item)
    tagString = re.search(r'data:image/png;base64,(?P<url>.+?)\"\)',text)
    imgdata = base64.b64decode(tagString['url'])
    filename = 'img'+str(i)
    file = open('E:/img/3/{}.jpg'.format(filename), 'wb')
    file.write(imgdata)
    file.close()
    im = Image.open('E:/img/1/{}.jpg'.format(filename))
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