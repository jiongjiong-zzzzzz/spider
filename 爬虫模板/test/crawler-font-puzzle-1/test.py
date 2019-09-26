'''
异步方式爬取当当畅销书的图书信息
'''

import time
import aiohttp
import asyncio
import pandas as pd
from bs4 import BeautifulSoup
import re
from fontTools.ttLib import TTFont
from lxml import etree

# 获取网页（文本信息）
headers = {'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.139 Safari/537.36',
            'Referer': 'http://glidedsky.com/level/crawler-basic-1',
            'Host': 'glidedsky.com',
           'cookie': '_ga=GA1.2.1976315618.1564047267; _gid=GA1.2.925251583.1564047267; remember_web_59ba36addc2b2f9401580f014c7f58ea4e30989d=eyJpdiI6ImtzdTdSU292bkxqRTlkaDh5c1E1Mmc9PSIsInZhbHVlIjoiblNROGUraUkweUt0RW5kSGhtNUJnWlNGXC91Tzk5bTBZMUd6NXZVOUQ0djF0S3VEWnJtTzNkOENBTkkwenFDXC9xQk1QTFpCRG14N1M4MEZNZHNOZUlOY21PZ0Jzb3J0akIza083dTIzTlN5UDA5RzZXVll6bXlwRUphMjBScjNBTHdyazliUlREV3RvK2tyWFhHVENUSlVXZjRuWFBsaTJhZXdnNEJOM2hxZEU9IiwibWFjIjoiZThlMTUwNGFhOTFjYzkyOGI1MjQxMTBlZGNiMDQ3NjAxNjIxMzE4ZmVlNWFjMjNlNmI2NDc1MGQyMzI1ODBlNyJ9; Hm_lvt_020fbaad6104bcddd1db12d6b78812f6=1564047267,1564106182,1564108884,1564235050; _gat_gtag_UA_75859356_3=1; XSRF-TOKEN=eyJpdiI6InVEQUgxZWNLb0lqODYxNTd6TXhQQ0E9PSIsInZhbHVlIjoidGdrSWEwYXkxQ3U2aVdzNlFKTURERmtrSkJTZUhjakY1Y0s5ZDdaQWU2WTMrR3dtMVVuQzdIVDVhZndxZGhGTCIsIm1hYyI6ImUwYzkwYzBhMGFiMTViM2VkNjZlOWM4NTIyNjA4NDQwOWJjZjAxZjZkMzhmMTcwNjg3ZjhhZTFlZGFiZGVjMDQifQ%3D%3D; glidedsky_session=eyJpdiI6IlJDV0lWZUFRdDBOcFBUNWt4akpwWGc9PSIsInZhbHVlIjoiZHZzN3VXUnJHU2l5MGRDRWdMMUNsVXZcL1V1R1BIaVhodTFJa2EzOTZIbW51bjRodlVadlZCbnY2STh1MkNHYVAiLCJtYWMiOiJiYjNiOThkNTcxYTQwZDdmNWUyODhjMWI2Mjg2ODAwNjBmYmRiNjgzMzViOGU5YTllOTQ5MmQ2ZGY5N2I2Y2QwIn0%3D; Hm_lpvt_020fbaad6104bcddd1db12d6b78812f6=1564286808'}
sem = asyncio.Semaphore(10) # 信号量，控制协程数，防止爬的过快
async def fetch(session, url):
    with(await sem):
        async with session.get(url,headers=headers) as response:
            return await response.text()

async def font(session,text):
    woff_ = re.search(r'url\("(.*\.woff)"\)', text).group(1)
    print(woff_)
    with(await sem):
        async with session.get(woff_) as response:
            with open('font.ttf', 'wb') as f:

                        f.write(response.text())
            font = TTFont('font.ttf')
            return  font
# 解析网页
async def parser(text,font,count):
    aa = {'48': '0', '49': '1', '50': '2', '51': '3', '52': '4', '53': '5', '54': '6', '55': '7', '56': '8', '57': '9'}
    bb = {'cid00020': '3', 'cid00019': '2', 'cid00026': '9', 'cid00021': '4', 'cid00023': '6', 'cid00018': '1',
          'cid00024': '7', 'cid00025': '8', 'cid00022': '5', 'cid00017': '0'}
    dict_ = {}
    for k, v in font.getBestCmap().items():
        # print(aa[str(k)],bb[v])
        dict_[aa[str(k)]] = bb[v]
    html = etree.HTML(text)
    nums = html.xpath("//div[@class='col-md-1']/text()")
    import re
    for num in nums:
        list_ = []
        num = re.findall(r"\d", num)
        for n in num:
            list_.append(dict_[n])
        aa = ''.join(list_)
        count += int(aa)
    print(count)
    return count

# 处理网页
async def download(url):
    async with aiohttp.ClientSession() as session:
        html = await fetch(session, url)
        fonts = await font(session, html)
        count = parser(html,fonts)
def main():
    count = 0
    # 全部网页
    urls = ['http://glidedsky.com/level/web/crawler-font-puzzle-1?page=%d'%i for i in range(1,11)]
    # 利用asyncio模块进行异步IO处理
    loop = asyncio.get_event_loop()
    tasks = [asyncio.ensure_future(download(url)) for url in urls]
    loop.run_until_complete(asyncio.wait(tasks))  # 激活协程
    loop.close()
    print(count)
if __name__ == '__main__':
    main()
