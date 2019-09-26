import urllib.parse as urlparse


hosts = ['http://www.sohu.com/c/8/1460?spm=smpc.news-home.top-subnav.2.1563353511990tWyoOS0',
        'http://auto.sohu.com/?spm=smpc.news-home.header.4.1563353511990tWyoOS0',
         'https://www.sohu.com/a/326996927_430555',
         'https://news.sina.com.cn/',
         'https://finance.sina.com.cn/',
         'http://finance.sina.com.cn/stock/newstock/',
         'https://finance.sina.com.cn/stock/newstock/zrzdt/2019-07-17/doc-ihytcitm2597531.shtml',
         'https://news.baidu.com/guoji',
         'http://baijiahao.baidu.com/s?id=1639179639266270885']
for host in hosts:
    cc = urlparse.urlparse(host)
    print(cc)