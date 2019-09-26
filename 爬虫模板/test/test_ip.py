import requests
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
def download(url):
    session = requests.Session()
    headers = {
        'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.139 Safari/537.36',}
    session.headers.update(headers)
    r = session.get(url,proxies=proxies)
    r.raise_for_status()
    return r.content.decode('utf-8')
def main():
   url = 'http://www.sipo.gov.cn/'
   print(download(url))
if __name__ == '__main__':
    main()




