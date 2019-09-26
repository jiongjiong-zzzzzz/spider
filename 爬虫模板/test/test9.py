import pymongo
from lxml import etree
import requests
from fontTools.ttLib import TTFont
from lxml import etree
from PIL import Image
import re
client = pymongo.MongoClient('localhost', 27017)
db = client['glidedsky']
collection = db['xuebi1']
# 查询文档
cursor=collection.find()
print(cursor.count())   # 获取文档个数
count = 0
i=0
for item in cursor:
    i=i+1
    print(str(i),end=":")
    text = item['html']
    filename = 'img' + str(i)
    im = Image.open('E:/img/1/{}.jpg'.format(filename))
    imX, imY = im.size
    avg = imX / 10
    xiabiao = dict(zip([x for x in range(0, 10, 1)], [avg / 2 + x * avg for x in range(0, 10, 1)]))
    html = etree.HTML(text)
    nums = html.xpath("//div[@class='col-md-1']")
    list_1=[]
    for div_list in nums:
        divs = div_list.xpath("./div")
        list_ = []
        for div in divs:
            class_name = div.xpath("./@class")[0].split(' ')[0]
            left = re.findall(r"\.%s \{ background-position-x\:(.*?)px.+?\}" % class_name, text)[0]
            for k, v in xiabiao.items():
                if int(v) + int(left) > 0:
                    list_.append(str(k))
                    break
        num = "".join(list_)
        list_1.append(num)
        print(num,end=",")
        count += int(num)
    print('')
    #print(count)
print(count)