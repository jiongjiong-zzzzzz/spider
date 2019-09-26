from fontTools.ttLib import TTFont
import requests
from lxml import etree
import time
session = requests.Session()
# 代理服务器
proxyHost = "http-dyn.abuyun.com"
proxyPort = "9020"

# 代理隧道验证信息
proxyUser = "HM2JUEKN11WD27CD"
proxyPass = "533BB05637237280"

proxyMeta = "http://%(user)s:%(pass)s@%(host)s:%(port)s" % {
    "host": proxyHost,
    "port": proxyPort,
    "user": proxyUser,
    "pass": proxyPass,
}

proxies = {
    "http": proxyMeta,
    "https": proxyMeta,
}
headers = {'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.139 Safari/537.36',
            'Referer': 'http://glidedsky.com/level/crawler-basic-1',
            'Host': 'glidedsky.com',
           'cookie': '_ga=GA1.2.1976315618.1564047267; _gid=GA1.2.925251583.1564047267; remember_web_59ba36addc2b2f9401580f014c7f58ea4e30989d=eyJpdiI6ImtzdTdSU292bkxqRTlkaDh5c1E1Mmc9PSIsInZhbHVlIjoiblNROGUraUkweUt0RW5kSGhtNUJnWlNGXC91Tzk5bTBZMUd6NXZVOUQ0djF0S3VEWnJtTzNkOENBTkkwenFDXC9xQk1QTFpCRG14N1M4MEZNZHNOZUlOY21PZ0Jzb3J0akIza083dTIzTlN5UDA5RzZXVll6bXlwRUphMjBScjNBTHdyazliUlREV3RvK2tyWFhHVENUSlVXZjRuWFBsaTJhZXdnNEJOM2hxZEU9IiwibWFjIjoiZThlMTUwNGFhOTFjYzkyOGI1MjQxMTBlZGNiMDQ3NjAxNjIxMzE4ZmVlNWFjMjNlNmI2NDc1MGQyMzI1ODBlNyJ9; Hm_lvt_020fbaad6104bcddd1db12d6b78812f6=1564047267,1564106182,1564108884,1564235050; _gat_gtag_UA_75859356_3=1; XSRF-TOKEN=eyJpdiI6InVEQUgxZWNLb0lqODYxNTd6TXhQQ0E9PSIsInZhbHVlIjoidGdrSWEwYXkxQ3U2aVdzNlFKTURERmtrSkJTZUhjakY1Y0s5ZDdaQWU2WTMrR3dtMVVuQzdIVDVhZndxZGhGTCIsIm1hYyI6ImUwYzkwYzBhMGFiMTViM2VkNjZlOWM4NTIyNjA4NDQwOWJjZjAxZjZkMzhmMTcwNjg3ZjhhZTFlZGFiZGVjMDQifQ%3D%3D; glidedsky_session=eyJpdiI6IlJDV0lWZUFRdDBOcFBUNWt4akpwWGc9PSIsInZhbHVlIjoiZHZzN3VXUnJHU2l5MGRDRWdMMUNsVXZcL1V1R1BIaVhodTFJa2EzOTZIbW51bjRodlVadlZCbnY2STh1MkNHYVAiLCJtYWMiOiJiYjNiOThkNTcxYTQwZDdmNWUyODhjMWI2Mjg2ODAwNjBmYmRiNjgzMzViOGU5YTllOTQ5MmQ2ZGY5N2I2Y2QwIn0%3D; Hm_lpvt_020fbaad6104bcddd1db12d6b78812f6=1564286808'}
session.headers.update(headers)
url = 'http://glidedsky.com/level/web/crawler-font-puzzle-1?page={}'
count = 0
for i in range(1,1001):

    print(i)
    filename = '0'+str(i)+'.xml'
    r = session.get(url.format(i),proxies=proxies)
    import re
    text = r.text
    try:
        woff_ = re.search(r'url\("(.*\.woff)"\)', text).group(1)
    except:
        print()
    print(woff_)
    response_woff = requests.get(woff_, headers = {'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.139 Safari/537.36'},stream=True)
    with open('font.ttf', 'wb') as f:
        for chunk in response_woff.iter_content(chunk_size=1024):
            if chunk:
                f.write(chunk)
    font = TTFont('font.ttf')

    aa = {'48':'0','49':'1','50':'2', '51':'3','52':'4','53':'5','54':'6','55':'7','56':'8','57':'9'}
    bb = {'cid00020':'3','cid00019':'2','cid00026':'9','cid00021':'4','cid00023':'6','cid00018':'1','cid00024':'7','cid00025':'8','cid00022':'5','cid00017':'0'}
    dict_={}
    for k, v in font.getBestCmap().items():
        #print(aa[str(k)],bb[v])
        dict_[aa[str(k)]]=bb[v]
    html = etree.HTML(text)
    nums = html.xpath("//div[@class='col-md-1']/text()")
    import  re

    for num in nums:
        list_=[]
        num = re.findall(r"\d",num)
        for n in num:
            list_.append(dict_[n])
        aa=''.join(list_)
        count += int(aa)
    print(count)
print(count)


