import requests
from lxml import etree
import time
import re
from news.mongodb import  insert
proxyHost = "http-dyn.abuyun.com"
proxyPort = "9020"

# 代理隧道验证信息
proxyUser = "HM2JUEKN11WD27CD"
proxyPass = "533BB05637237280"

proxyMeta = "http://%(user)s:%(pass)s@%(host)s:%(port)s" % {
                "host": proxyHost,
                "port": proxyPort,
                "user": proxyUser,
                "pass": proxyPass,
        }

proxies = {'http': proxyMeta,'https': proxyMeta}

def download(url,i):
    session = requests.Session()
    headers = {
        'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.139 Safari/537.36',
        'Referer': 'http://glidedsky.com/level/crawler-basic-1',
        'Host': 'glidedsky.com',
        'Accept': 'text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8',
        'Accept-Encoding': 'gzip, deflate',
        'Accept-Language': 'zh-CN,zh;q=0.9',
        'cookie': '_ga=GA1.2.1976315618.1564047267; _gid=GA1.2.925251583.1564047267; remember_web_59ba36addc2b2f9401580f014c7f58ea4e30989d=eyJpdiI6ImtzdTdSU292bkxqRTlkaDh5c1E1Mmc9PSIsInZhbHVlIjoiblNROGUraUkweUt0RW5kSGhtNUJnWlNGXC91Tzk5bTBZMUd6NXZVOUQ0djF0S3VEWnJtTzNkOENBTkkwenFDXC9xQk1QTFpCRG14N1M4MEZNZHNOZUlOY21PZ0Jzb3J0akIza083dTIzTlN5UDA5RzZXVll6bXlwRUphMjBScjNBTHdyazliUlREV3RvK2tyWFhHVENUSlVXZjRuWFBsaTJhZXdnNEJOM2hxZEU9IiwibWFjIjoiZThlMTUwNGFhOTFjYzkyOGI1MjQxMTBlZGNiMDQ3NjAxNjIxMzE4ZmVlNWFjMjNlNmI2NDc1MGQyMzI1ODBlNyJ9; Hm_lvt_020fbaad6104bcddd1db12d6b78812f6=1564106182,1564108884,1564235050,1564313055; XSRF-TOKEN=eyJpdiI6ImxJR2IzN0ZKWWtVQ1J6ajNOS1ZJT3c9PSIsInZhbHVlIjoibzYrUmNSMndscFQxRnhjZDNzNEJWVm5wMnkrTGJoR0JCalFPWXlaNlVpZmloS1JUWE5FTnRWZFwvbWlaQXNNWGUiLCJtYWMiOiI3ODU0Y2FlMDQxODBlMWJlZTQ0ZTBlOGE3YzhmZjQ1OGU3YmQxNGQ5NjkyYjA0ZTk4ODExYTQyMWEzMWU1MTBkIn0%3D; glidedsky_session=eyJpdiI6Im9VYmJcL2JDMDgzSGVvWW5KeDluWDRnPT0iLCJ2YWx1ZSI6Im13RmRPUG9oSHllZWl3QVVFODU4SERqR0orSVVuMFpsZnoxQTh1MGFueFJtcEM2ZXQ2WXhCdlFVbXNmeHlxVFgiLCJtYWMiOiI2NGIxN2JlMTM2OTY0ZmMwOWMwODIzMzg4ZDRjODg5ZWUxNDUyMTZjN2E2OWZmYzYyZDAwMzNlMzljODJlNDhiIn0%3D; _gat_gtag_UA_75859356_3=1; Hm_lpvt_020fbaad6104bcddd1db12d6b78812f6=1564313158'}

    session.headers.update(headers)
    try:
        r = session.get(url,proxies=proxies)
        r.raise_for_status()
        return r.text
    except:
        return download(url, i)

def prase(url,i,count):
    text = download(url,i)
    woff_ = re.search(r'url\("(.*\.woff)"\)', text).group(1)
    return text,woff_
def main():
    count = 0
    for i in range(780, 1001):
        print(i,count)
        url = 'http://glidedsky.com/level/web/crawler-font-puzzle-1?page={}'.format(i)
        text,woff_ = prase(url,i,count)
        item={'url':url,'html':text,'woff':woff_}
        insert(item)

    print(count)
if __name__ == '__main__':
    main()




