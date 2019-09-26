import requests
from bs4 import BeautifulSoup
import time
from concurrent.futures import ThreadPoolExecutor, wait, ALL_COMPLETED
from lxml import etree
import re
import bs4
# 开始时间
t1 = time.time()
print('#' * 50)

url = "https://borneobulletin.com.bn/?s=the+Belt+and+Road"
# 请求头部
headers = {'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.139 Safari/537.36'}
# 发送HTTP请求
res = requests.get(url, headers=headers)
# 解析网页
selector = etree.HTML(res.text)
xpath_res = "//div[@class='td-ss-main-content']//h3[@class='entry-title td-module-title']/a/@href"
infos = selector.xpath(xpath_res)
for url in infos:
    return_data = []
    res = requests.get(url, headers=headers)
    # 解析网页
    soup = BeautifulSoup(res.text, "lxml")
    title = soup.find('h1', class_="entry-title").text
    date = soup.find('span', class_="td-post-date").text
    infos = soup.find(class_='td-post-content').findAll('p')
    for one_info in infos:
        content_dir = re.search('<p[\\s\\S]+/p>', str(one_info))
        if content_dir:
            need_content = one_info.text
        else:
            if isinstance(one_info, bs4.NavigableString):
                need_content = one_info
            else:
                continue
        if not need_content.strip():
            continue
        return_data.append(need_content.strip())
    print(url)
    print(title)
    print(date)
    print(return_data)

# 获取每个网页的name和description

