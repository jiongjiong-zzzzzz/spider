import requests
from bs4 import BeautifulSoup
import time
import aiohttp
import asyncio

# 开始时间
t1 = time.time()
print('#' * 50)

url = "http://www.wikidata.org/w/index.php?title=Special:WhatLinksHere/Q5&limit=500&from=0"
# 请求头部
headers = {
    'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/67.0.3396.87 Safari/537.36'}
# 发送HTTP请求
req = requests.get(url, headers=headers)
# 解析网页
soup = BeautifulSoup(req.text, "lxml")
# 找到name和Description所在的记录
human_list = soup.find(id='mw-whatlinkshere-list')('li')

urls = []
# 获取网址
for human in human_list:
    url = human.find('a')['href']
    urls.append('https://www.wikidata.org' + url)


# 异步HTTP请求
async def fetch(session, url):
    async with session.get(url) as response:
        return await response.text()


# 解析网页
async def parser(html):
    # 利用BeautifulSoup将获取到的文本解析成HTML
    soup = BeautifulSoup(html, "lxml")
    # 获取name和description
    name = soup.find('span', class_="wikibase-title-label")
    desc = soup.find('span', class_="wikibase-descriptionview-text")
    if name is not None and desc is not None:
        print('%-40s,\t%s' % (name.text, desc.text))


# 处理网页，获取name和description
async def download(url):
    async with aiohttp.ClientSession() as session:
        try:
            html = await fetch(session, url)
            await parser(html)
        except Exception as err:
            print(err)


# 利用asyncio模块进行异步IO处理
loop = asyncio.get_event_loop()
tasks = [asyncio.ensure_future(download(url)) for url in urls]
tasks = asyncio.gather(*tasks)
loop.run_until_complete(tasks)

t2 = time.time()  # 结束时间
print('使用异步，总共耗时：%s' % (t2 - t1))
print('#' * 50)