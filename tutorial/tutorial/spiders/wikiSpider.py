import scrapy.cmdline
from tutorial.items import TutorialItem
import requests
from bs4 import BeautifulSoup

# 获取请求的500个网址，用requests+BeautifulSoup搞定
def get_urls():
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

    # print(urls)
    return urls

# 使用scrapy框架爬取
class bookSpider(scrapy.Spider):
    name = 'wikiScrapy'  # 爬虫名称
    start_urls = get_urls()  # 需要爬取的500个网址

    def parse(self, response):
        item = TutorialItem()
        # name and description
        item['name'] = response.css('span.wikibase-title-label').xpath('text()').extract_first()
        item['desc'] = response.css('span.wikibase-descriptionview-text').xpath('text()').extract_first()

        yield item

# 执行该爬虫，并转化为csv文件
scrapy.cmdline.execute(['scrapy', 'crawl', 'wikiScrapy', '-o', 'wiki.csv', '-t', 'csv'])