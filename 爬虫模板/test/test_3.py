import requests
from lxml import etree
import time
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

proxies = {'http': proxyMeta,'https': proxyMeta}

def download(url,i):
    session = requests.Session()
    headers = {
        'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.139 Safari/537.36',
        'Referer': 'http://glidedsky.com/level/crawler-basic-1',
        'Host': 'glidedsky.com',
        'Accept': 'text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8',
        'Accept-Encoding': 'gzip, deflate',
        'Accept-Language': 'zh-CN,zh;q=0.9',
        'cookie': '_ga=GA1.2.1976315618.1564047267; _gid=GA1.2.925251583.1564047267; remember_web_59ba36addc2b2f9401580f014c7f58ea4e30989d=eyJpdiI6ImtzdTdSU292bkxqRTlkaDh5c1E1Mmc9PSIsInZhbHVlIjoiblNROGUraUkweUt0RW5kSGhtNUJnWlNGXC91Tzk5bTBZMUd6NXZVOUQ0djF0S3VEWnJtTzNkOENBTkkwenFDXC9xQk1QTFpCRG14N1M4MEZNZHNOZUlOY21PZ0Jzb3J0akIza083dTIzTlN5UDA5RzZXVll6bXlwRUphMjBScjNBTHdyazliUlREV3RvK2tyWFhHVENUSlVXZjRuWFBsaTJhZXdnNEJOM2hxZEU9IiwibWFjIjoiZThlMTUwNGFhOTFjYzkyOGI1MjQxMTBlZGNiMDQ3NjAxNjIxMzE4ZmVlNWFjMjNlNmI2NDc1MGQyMzI1ODBlNyJ9; Hm_lvt_020fbaad6104bcddd1db12d6b78812f6=1564047267,1564106182,1564108884,1564235050; _gat_gtag_UA_75859356_3=1; XSRF-TOKEN=eyJpdiI6InVEQUgxZWNLb0lqODYxNTd6TXhQQ0E9PSIsInZhbHVlIjoidGdrSWEwYXkxQ3U2aVdzNlFKTURERmtrSkJTZUhjakY1Y0s5ZDdaQWU2WTMrR3dtMVVuQzdIVDVhZndxZGhGTCIsIm1hYyI6ImUwYzkwYzBhMGFiMTViM2VkNjZlOWM4NTIyNjA4NDQwOWJjZjAxZjZkMzhmMTcwNjg3ZjhhZTFlZGFiZGVjMDQifQ%3D%3D; glidedsky_session=eyJpdiI6IlJDV0lWZUFRdDBOcFBUNWt4akpwWGc9PSIsInZhbHVlIjoiZHZzN3VXUnJHU2l5MGRDRWdMMUNsVXZcL1V1R1BIaVhodTFJa2EzOTZIbW51bjRodlVadlZCbnY2STh1MkNHYVAiLCJtYWMiOiJiYjNiOThkNTcxYTQwZDdmNWUyODhjMWI2Mjg2ODAwNjBmYmRiNjgzMzViOGU5YTllOTQ5MmQ2ZGY5N2I2Y2QwIn0%3D; Hm_lpvt_020fbaad6104bcddd1db12d6b78812f6=1564286808'}

    session.headers.update(headers)
    r = session.get(url,proxies=proxies)
    try:
        r.raise_for_status()
        return r.text
    except:
        return download(url, i)

def prase(url,i,count):
    text = download(url,i)
    html = etree.HTML(text)
    nums = html.xpath("//div[@class='col-md-1']/text()")
    import re
    for num in nums:
        num = re.findall(r"\d+", num)
        count += int(num[0])
    return count

def main():
    count = 0
    for i in range(1, 1001):
        print(i,count)
        url = 'http://glidedsky.com/level/web/crawler-ip-block-2?page={}'.format(i)
        count = prase(url,i,count)

    print(count)
if __name__ == '__main__':
    main()




