import pymongo
from lxml import etree
import requests
from fontTools.ttLib import TTFont
client = pymongo.MongoClient('localhost', 27017)
db = client['glidedsky']
collection = db['font2']
# 查询文档
cursor=collection.find()
print(cursor.count())   # 获取文档个数
count = 0
i=0
s=set()
list_3 =[ ]
for item in cursor:
    i=i+1
    print(str(i)+'*'*10)
    text = item['html']
    woff = item['woff']
    # response_woff = requests.get(woff, headers={
    #     'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.139 Safari/537.36'},
    #                              stream=True)
    # with open('./font/font{}.ttf'.format(i), 'wb') as f:
    #     for chunk in response_woff.iter_content(chunk_size=1024):
    #         if chunk:
    #             f.write(chunk)
    font = TTFont('./font/font{}.ttf'.format(i))

    d = {
        "cid00017": '0', "cid00762": '0', "cid00930": '0', "cid00910": '0', "cid01049": '0',
        "cid01910": '0', "cid01403": '0', "cid44288": '0',

        "cid00018": '1', "cid00763": '1', "cid00931": '1', "cid00911": '1', "cid00951": '1',
        "cid02028": '1', "cid00775": '1', "cid09502": '1', "cid14087": '1', "cid01911": '1', "cid01060": '1',

        "cid00019": '2', "cid00764": '2', "cid00932": '2', "cid00912": '2', "cid01912": '2',
        "cid02029": '2', "cid01061": '2', "cid09693": '2', "cid00952": '2', "cid39933": '2', "cid00776": '2',

        "cid00020": '3', "cid00765": '3', "cid00933": '3', "cid01062": '3',
        "cid00777": '3',
        "cid02030": '3', "cid09513": '3', "cid00953": '3', "cid00913": '3',
        "cid01913": '3', "cid11878": '3',

        "cid00021": '4', "cid00766": '4', "cid00934": '4', "cid00914": '4', "cid01914": '4',
        "cid02031": '4', "cid13187": '4', "cid00778": '4', "cid00954": '4', "cid33331": '4', "cid01063": '4',

        "cid00022": '5', "cid00767": '5', "cid00935": '5', "cid00915": '5', "cid01064": '5',
        "cid00779": '5', "cid09894": '5', "cid00955": '5', "cid02032": '5', "cid01915": '5', "cid09705": '5',

        "cid00023": '6', "cid00768": '6', "cid00936": '6', "cid00916": '6', "cid00780": '6',
        "cid00956": '6', "cid43947": '6', "cid02034": '6', "cid10926": '6', "cid01917": '6', "cid01065": '6',

        "cid00024": '7', "cid00769": '7', "cid00937": '7', "cid00917": '7', "cid00957": '7',
        "cid01918": '7', "cid00781": '7', "cid21447": '7', "cid09506": '7', "cid01066": '7', "cid02035": "7",

        "cid00025": '8', "cid00770": '8', "cid01067": '8', "cid00918": '8', "cid01921": '8',
        "cid00782": '8', "cid02038": '8', "cid00958": '8', "cid00938": '8', "cid10920": '8',

        "cid00026": '9', "cid00771": '9', "cid00939": '9', "cid00919": '9', "cid01068": '9',
        "cid26924": '9', "cid19425": '8', "cid00783": '9', "cid01923": '9', "cid09631": '9',
        "cid02040": '9', "cid00959": "9",
    }
    dict_ = {}
    for k, v in font.getBestCmap().items():
        k = hex(k).replace('0x','\\u').encode('utf-8').decode('unicode_escape')
        dict_[k] = v
    html = etree.HTML(text)
    nums = html.xpath("//div[@class='col-md-1']/text()")
    import re
    for num in nums:
        list_ = []
        list_2 = []
        num = re.findall(r"[\u4e00-\u9fa5]+", num)
        for n in num[0]:
            s.add(dict_[n])
            list_2.append(dict_[n])
            list_3.append(dict_[n])
            list_.append(d[dict_[n]])
        aa = ''.join(list_)
        count += int(aa)
        print(list_2,list_)

    #print(count)
# a = {}
# for i in list_3:
#  if list_3.count(i) >= 1:
#   a[i] = list_3.count(i)
# print (a)
print(count)