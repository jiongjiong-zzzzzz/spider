# -*- coding: utf-8 -*-

# Scrapy settings for zhihu project
#
# For simplicity, this file contains only settings considered important or
# commonly used. You can find more settings consulting the documentation:
#
#     https://doc.scrapy.org/en/latest/topics/settings.html
#     https://doc.scrapy.org/en/latest/topics/downloader-middleware.html
#     https://doc.scrapy.org/en/latest/topics/spider-middleware.html

BOT_NAME = 'zhihu'

SPIDER_MODULES = ['zhihu.spiders']
NEWSPIDER_MODULE = 'zhihu.spiders'



MONGODB_HOST = '127.0.0.1'
MONGODB_PORT = 27017
MONGODB_DBNAME = 'zhihu'
MONGODB_DOCNAME = 'userinfor'

# Crawl responsibly by identifying yourself (and your website) on the user-agent
USER_AGENT = 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_2) AppleWebKit/537.36(KHTML, like Gecko) Chrome/64.0.3282.186 Safari/537.36'


# Obey robots.txt rules
ROBOTSTXT_OBEY = False

# Configure maximum concurrent requests performed by Scrapy (default: 16)
#CONCURRENT_REQUESTS = 32

# Configure a delay for requests for the same website (default: 0)
# See https://doc.scrapy.org/en/latest/topics/settings.html#download-delay
# See also autothrottle settings and docs
#DOWNLOAD_DELAY = 3
# The download delay setting will honor only one of:
#CONCURRENT_REQUESTS_PER_DOMAIN = 16
#CONCURRENT_REQUESTS_PER_IP = 16

# Disable cookies (enabled by default)
#COOKIES_ENABLED = False

# Disable Telnet Console (enabled by default)
#TELNETCONSOLE_ENABLED = False

# Override the default request headers:
DEFAULT_REQUEST_HEADERS = {
    "accept": 'application/json, text/plain, */*',
    "Accept-Language": 'zh-CN,zh;q=0.9,en;q=0.8',
    "authorization": 'oauth c3cef7c66a1843f8b3a9e6a1e3160e20',
    "Cache-Control": 'no-cache',
    "Connection": 'keep-alive',
    "Host": 'www.zhihu.com',
    "Pragma": 'no-cache',
    "Referer": 'https://www.zhihu.com/',
    "X-UDID": 'AFBCMYYIigyPTn-w9gPOx5CNrckgcsQKrhk=',
    "cookie":'_zap=74a1bc89-5927-46f1-a903-26b9d4f9906c;q_c1=679bbf981bc54edaa36a718a757d7110|1506423740000|1502849291000;d_c0="AFBCMYYIigyPTn-w9gPOx5CNrckgcsQKrhk=|1508201688";q_c1=f3521e394ce8459094ba76547cddd3e5|1517535767000|1502849291000;aliyungf_tc=AQAAACykS2tz0ggA5KAxJPLJJw8rf8SF; _xsrf=c8e59c5f-190a-4b71-ad56-1425517c7a9b;r_cap_id="Yjc3Y2Y1ODkxYzcxNGZkOGFhMDUwZjBhNjFhZTEyYjI=|1519810983|a19b0558ddd2a119ed7581c8fd59427ab2298d03";cap_id="ZDM1Y2UzZTBhZTQ2NDc3OWIzYmE3YzczYmY0YmVlNzE=|1519810983|4c6504306036f99443b659ce4f8ea2723ebb6a96";l_cap_id="NDcyOGU5YzUyNTdmNDc1OTlhMGU1Mzc3MjQ4NDY5YjI=|1519810983|ed1d25b9a6905ad1891a94984d8cecd51b8a96e0";__utma=51854390.1002977338.1508201688.1516880301.1519810987.10;__utmc=51854390; __utmz=51854390.1519810987.10.10.utmcsr=zhihu.com|utmccn=(referral)|utmcmd=referral|utmcct=/people/liuyu-43-97/activities;__utmv=51854390.000--|2=registration_date=20160118=1^3=entry_date=20170816=1;capsion_ticket="2|1:0|10:1519878553|14:capsion_ticket|44:N2NhNTJmNGQ5M2EyNDUzODk1MzIxYjgzNjFkM2FiZmY=b0b25b31dbdc0c80f49a9db073ec4953c5c4f6edd1bb1978bcee89c9b64f0b9"',


}

# Enable or disable spider middlewares
# See https://doc.scrapy.org/en/latest/topics/spider-middleware.html
#SPIDER_MIDDLEWARES = {
#    'zhihu.middlewares.ZhihuSpiderMiddleware': 543,
#}

# Enable or disable downloader middlewares
# See https://doc.scrapy.org/en/latest/topics/downloader-middleware.html
DOWNLOADER_MIDDLEWARES = {
 # 'zhihu.middlewares.MyCustomDownloaderMiddleware': 543,
 'zhihu.middlewares.ChangeProxy': 543,
}


# Enable or disable extensions
# See https://doc.scrapy.org/en/latest/topics/extensions.html
#EXTENSIONS = {
#    'scrapy.extensions.telnet.TelnetConsole': None,
#}

# Configure item pipelines
# See https://doc.scrapy.org/en/latest/topics/item-pipeline.html
ITEM_PIPELINES = {
 'zhihu.pipelines.ZhihuPipeline': 300,
}


# Enable and configure the AutoThrottle extension (disabled by default)
# See https://doc.scrapy.org/en/latest/topics/autothrottle.html
#AUTOTHROTTLE_ENABLED = True
# The initial download delay
#AUTOTHROTTLE_START_DELAY = 5
# The maximum download delay to be set in case of high latencies
#AUTOTHROTTLE_MAX_DELAY = 60
# The average number of requests Scrapy should be sending in parallel to
# each remote server
#AUTOTHROTTLE_TARGET_CONCURRENCY = 1.0
# Enable showing throttling stats for every response received:
#AUTOTHROTTLE_DEBUG = False

# Enable and configure HTTP caching (disabled by default)
# See https://doc.scrapy.org/en/latest/topics/downloader-middleware.html#httpcache-middleware-settings
#HTTPCACHE_ENABLED = True
#HTTPCACHE_EXPIRATION_SECS = 0
#HTTPCACHE_DIR = 'httpcache'
#HTTPCACHE_IGNORE_HTTP_CODES = []
#HTTPCACHE_STORAGE = 'scrapy.extensions.httpcache.FilesystemCacheStorage'
SCHEDULER = "scrapy_redis.scheduler.Scheduler"
DUPEFILTER_CLASS = "scrapy_redis.dupefilter.RFPDupeFilter"
SCHEDULER_PERSIST = True
SCHEDULER_QUEUE_CLASS = 'scrapy_redis.queue.SpiderQueue'