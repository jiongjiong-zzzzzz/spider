#!/usr/bin/env python
# -*- coding:utf-8 -*-

# 使用了线程库
import threading
# 队列
import queue
# 解析库
from lxml import etree
# 请求处理
import requests
# json处理
import json
import time
import bs4
import re
import urllib
def get_time(format_date,time_pull = None):
    if time_pull:
        return int(time.mktime(time.strptime(time_pull, format_date))*1000)
class ThreadCrawl(threading.Thread):
    def __init__(self, threadName, pageQueue, dataQueue):
        #threading.Thread.__init__(self)
        # 调用父类初始化方法
        super(ThreadCrawl, self).__init__()
        # 线程名
        self.threadName = threadName
        # 页码队列
        self.pageQueue = pageQueue
        # 数据队列
        self.dataQueue = dataQueue
        # 请求报头
        self.headers = {'User-Agent':'Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.101 Safari/537.36'}

    def run(self):
        print ("启动 " + self.threadName)
        while not CRAWL_EXIT:
            try:
                page = self.pageQueue.get(False)
                if page == 0:
                    url = 'http://hnsfpb.hunan.gov.cn/xxgk_71121/gzdt/sxdt/index.html'
                else:
                    url = 'http://hnsfpb.hunan.gov.cn/xxgk_71121/gzdt/sxdt/index_%s.html' % (page)
                time.sleep(0.3)
                res = requests.get(url, headers=self.headers)
                res.raise_for_status()
                reg_content = res.content.decode('utf-8')
                html_page = bs4.BeautifulSoup(reg_content, 'lxml')
                infos = html_page.find(class_='table').findAll('a')
                for one_info in infos:
                    _one_info = str(one_info)
                    content_dir = one_info
                    if content_dir:
                        _datetime = 0
                        _url_tmp = content_dir['href'].strip()
                        _url_tmp = _url_tmp.replace('./', 'http://hnsfpb.hunan.gov.cn/xxgk_71121/gzdt/sxdt/')
                        print(self.threadName +' '+_url_tmp)
                        self.dataQueue.put(_url_tmp)
                #print len(content)
            except:
                pass
        print ("结束 " + self.threadName)

class ThreadParse(threading.Thread):
    def __init__(self, threadName, dataQueue,  lock):
        super(ThreadParse, self).__init__()
        # 线程名
        self.threadName = threadName
        # 数据队列
        self.dataQueue = dataQueue
        self.lock = lock
        self.headers = {
            'User-Agent': 'Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.101 Safari/537.36'}

    def run(self):
        print ("启动" + self.threadName)
        while not PARSE_EXIT:
            try:
                href = self.dataQueue.get(False)
                self.parse(href)
            except:
                pass
        print ("退出" + self.threadName)

    def parse(self, href):
        # 解析为HTML DOM
        return_data = []
        try:
            res = requests.get(href, headers=self.headers, timeout=5)
            res.raise_for_status()
            reg_content = res.content.decode('utf-8')
            html_page = bs4.BeautifulSoup(reg_content, 'lxml')
        except:
            return 'timeout'
        try:
            infos = html_page.find(class_='Custom_UnionStyle').findAll('p')
            if not infos:
                infos = html_page.find(class_='tys-main-zt-show').findAll('p')
        except:
            return
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
        nian = urllib.parse.unquote_plus('%E5%B9%B4')
        yue = urllib.parse.unquote_plus('%E6%9C%88')
        ri = urllib.parse.unquote_plus('%E6%97%A5')
        date = re.search(r"(\d{4}%s\d{1,2}%s\d{1,2}%s)" % (nian, yue, ri), reg_content)
        datetime_dir = re.match('(?P<year>\d{4})%s(?P<month>\d+?)%s(?P<day>\d+?)%s' % (nian, yue, ri), date[0])
        tt_tmp = '%s-%s-%s' % (
            datetime_dir['year'], datetime_dir['month'], datetime_dir['day'])
        _datetime = 0
        if datetime_dir:
            _datetime = get_time('%Y-%m-%d', tt_tmp)
        _title = html_page.find('h3').text
        print(self.threadName+"———"+_title+" "+str(_datetime))

            # with 后面有两个必须执行的操作：__enter__ 和 _exit__
            # 不管里面的操作结果如何，都会执行打开、关闭
            # 打开锁、处理内容、释放锁


CRAWL_EXIT = False
PARSE_EXIT = False


def main():
    # 页码的队列，表示20个页面
    pageQueue = queue.Queue(40)
    # 放入1~10的数字，先进先出
    for i in range(0, 10):
        pageQueue.put(i)

    # 采集结果(每页的HTML源码)的数据队列，参数为空表示不限制
    dataQueue = queue.Queue()
    # 创建锁
    lock = threading.Lock()

    # 三个采集线程的名字
    crawlList = ["采集线程1号", "采集线程2号", "采集线程3号"]
    # 存储三个采集线程的列表集合
    threadcrawl = []
    for threadName in crawlList:
        thread = ThreadCrawl(threadName, pageQueue, dataQueue)
        thread.start()
        threadcrawl.append(thread)

    #time.sleep(3)
    # 三个解析线程的名字
    parseList = ["解析线程1号","解析线程2号","解析线程3号"]
    # 存储三个解析线程
    threadparse = []
    for threadName in parseList:
        thread = ThreadParse(threadName, dataQueue, lock)
        thread.start()
        threadparse.append(thread)

    # 等待pageQueue队列为空，也就是等待之前的操作执行完毕
    while not pageQueue.empty():
        pass

    # 如果pageQueue为空，采集线程退出循环
    global CRAWL_EXIT
    CRAWL_EXIT = True

    print ("pageQueue为空")

    for thread in threadcrawl:
        thread.join()
        print ("1")

    while not dataQueue.empty():
        pass

    global PARSE_EXIT
    PARSE_EXIT = True

    for thread in threadparse:
        thread.join()
        print ("2")
    print ("谢谢使用！")

if __name__ == "__main__":
    main()