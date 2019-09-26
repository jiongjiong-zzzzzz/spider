#!/usr/bin/env python3
# coding:utf-8
# Author: veelion


import time
import urllib.parse as urlparse
import execjs
import re
import requests


print(execjs.get().name)

def go(url, session, cookie_dict):
    print('\n', cookie_dict)
    key = '__jsl_clearance'
    value = cookie_dict.pop(key)
    print(session.cookies)
    session.cookies.set(key, value, **cookie_dict)
    print(session.cookies)
    new = {
        'Referer': 'http://www.mps.gov.cn/n2253534/n2253535/index.html',
        'Accept': 'text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8',
        'Accept-Encoding': 'gzip, deflate',
        'Accept-Language': 'zh-CN,zh;q=0.9,en-US;q=0.8,en;q=0.7,zh-TW;q=0.6',
        'DNT': '1',
        'Host': 'www.mps.gov.cn',
        'Upgrade-Insecure-Requests': '1',
    }
    session.headers.update(new)
    print(session.headers)
    time.sleep(1.4)
    newcookie = {key: value}
    # r = session.get(url, cookies=newcookie)
    r = session.get(url, cookies=newcookie)
    print(len(r.text))
    print(session.cookies)
    with open('z.html', 'wb') as f:
        f.write(r.content)

    # if r.text.startswith('<script>'):
    #     return go(url, session, key, value, cookie_dict)



def parse_js(text):
    js = re.findall(r'<script>(.*?)</script>', text)
    js = js[0]
    with open('z-js.js', 'w') as f:
        f.write(js)
    js = js.replace('eval(', 'var zzz = (')
    print(js)

    ctx = execjs.compile(js)
    zzz = ctx.eval('zzz')
    print(zzz)

    p_begin = zzz.find('cookie')
    p_end = zzz.find("Path=/;'") + len("Path=/;'")

    new_js = 'var ' + zzz[p_begin:p_end]
    print(new_js)

    ctx = execjs.compile(new_js)
    cookie_str = ctx.eval('cookie')
    print(cookie_str)
    # __jsl_clearance=1562638360.469|0|BwZgllDMTblN%2FL8bc%2FGWscSP8w8%3D;Expires=Tue, 09-Jul-19 03:12:40 GMT;Path=/;

    ## parse_qsl 会把 字符串 unquote，但是发给服务器的cookie的__jsl_clearance 最后面的|后面的字符串必须是quote过的服务器才认为正确。
    ## 我是怎么发现这个问题的呢？没发现之前，用这个cookie去访问还是521.思考一下：
    ## 这时候从js得到的cookie_str应该是没错了，那就是requests 的session发到服务器时出错了，
    ## 于是，用charles代理查看request发的cookies有什么问题，发现没有quote
    ## 其实，不用parse_qsl，自己分割一下cookie_str也很方便。
    cookie_dict = urlparse.parse_qsl(cookie_str)
    cookie_dict = dict(cookie_dict)
    cookie_dict = {k.lower():v for k, v in cookie_dict.items()}
    print(cookie_dict)
    ll = cookie_dict['__jsl_clearance'].split('|')
    print(ll)
    ## 把最后的|的后面的字符串quote，'/'也要quote，所以设置 safe=''
    ll[-1] = urlparse.quote(ll[-1], safe='')
    print(ll)
    print(cookie_dict['__jsl_clearance'])
    cookie_dict['__jsl_clearance'] = '|'.join(ll)
    print(cookie_dict)
    time_format = '%a, %d-%b-%y %H:%M:%S GMT'
    cookie_dict['expires'] = time.mktime(time.strptime(cookie_dict['expires'], time_format)) + 25200
    cookie_dict['domain'] = 'www.mps.gov.cn'
    return cookie_dict


def crawl():
    session = requests.Session()
    ## 设置cookies 代理，查看发送的cookies有无问题
    # session.proxies = {
    #     'http': 'http://127.0.0.1:8888',
    # }
    ua = {
        'User-Agent': 'Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.102 Safari/537.36',
    }
    session.headers.update(ua)
    url = 'http://www.mps.gov.cn/n2253534/n2253535/index.html'
    r = session.get(url)

    cookie_dict = parse_js(r.text)
    go(url, session, cookie_dict)



if __name__ == '__main__':
    crawl()
