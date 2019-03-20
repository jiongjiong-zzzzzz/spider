from goose3 import Goose
from goose3.text import StopWordsChinese
from Spider.date_re import time_fix
import re,urllib
# 初始化，设置中文分词
g = Goose({'stopwords_class': StopWordsChinese})
# 文章地址
urls = [
    'http://www.cnelc.com/text/2/190124/AD100886718_1.html'
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

