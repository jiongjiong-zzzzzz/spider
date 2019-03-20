# -*- coding: utf-8 -*-
import scrapy
import json
import re
from scrapy.http import Request
from new_sina.items import NewSinaItem
from scrapy.crawler import CrawlerProcess
from scrapy.utils.project import get_project_settings
class SinaNewsSpider(scrapy.Spider):
    name = 'sina_news'
    #base_url = 'https://feed.mix.sina.com.cn/api/roll/get?pageid=153&lid=2509&k=&num=50&page=0&r=0.2039175258717716&callback=jQuery111202898175079273473_1548138061938&_=1548138061939'

    def start_requests(self):
        start_uids = range(1,53)
        for page in start_uids:
            yield Request(url='https://feed.mix.sina.com.cn/api/roll/get?pageid=153&lid=2509&k=&num=50&page=%s&r=0.2039175258717716&callback=jQuery111202898175079273473_1548138061938&_=1548138061939'%(page), callback=self.parse)
    def parse(self, response):
        html_json = re.findall(r'.*"data":(.+?)}}[)].*', response.text, re.S)
        data = json.loads(html_json[0])
        for one_info in data:
            item = NewSinaItem()
            item['title'] = one_info['title']
            item['href'] = one_info['url']
            yield Request(
                url=item['href'],
                callback=self.parse_detail,
                meta = {"item":item}
            )
    def parse_detail(self,response):
        item = response.meta['item']
        try:
            date = re.search(r"(\d{4}年\d{1,2}月\d{1,2}日\s\d{1,2}:\d{1,2})" , response.text)
            item['publish_date'] = date[0]
        except:
            date= re.search(r"(\d{4}-\d{1,2}-\d{1,2}\s\d{1,2}:\d{1,2})", response.text)
            item['publish_date'] = date[0]
        #item['concent'] = response.xpath("//div[@class='article']//text()").extract()
        item['concent_img'] = response.xpath("//div[@class='img_wrapper']/img/@src").extract()
        item['concent_img'] = ['http:'+i for  i in item['concent_img']]
        print(item)



if __name__ == "__main__":
    process = CrawlerProcess(get_project_settings())
    process.crawl('sina_news')
    process.start()