from goose3 import Goose
from goose3.text import StopWordsChinese
from Spider.date_re import time_fix
import re,urllib
# 初始化，设置中文分词
g = Goose()
# 文章地址
urls = [
    'https://www.channelnewsasia.com/news/business/shares-knocked-lower-after-new-us-tariff-threat-on-chinese-goods-10727440'
       ]
for url in urls:
# 获取文章内容
    print(url)
    article = g.extract(url=url)
    # 标题
    print('标题：', article.title)
    # 显示正文
    #print(article.raw_html)
    print(article.cleaned_text)
    print(time_fix(article.raw_html))

