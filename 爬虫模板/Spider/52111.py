import requests
import execjs
headers={
        'User-Agent':'Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/64.0.3282.119 Safari/537.36'
    }
url = 'http://epub.sipo.gov.cn/patentoutline.action'
response = requests.get(url,headers)

js_html = response.text
print(js_html)





