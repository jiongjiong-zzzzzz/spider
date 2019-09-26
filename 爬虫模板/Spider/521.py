import requests
import execjs
headers={
        'User-Agent':'Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/64.0.3282.119 Safari/537.36'
    }
url = 'http://www.mps.gov.cn/n2253534/n2253535/index.html'
response = requests.get(url,headers)
__jsluid_h = response.headers["Set-Cookie"].split(';')[0]
js_html = response.text

js_html = js_html.strip().replace("<script>", "").replace("</script>", "").replace("eval", "return ").replace("\x00","")
print(js_html)
js_1 = '''function clearance(){''' + js_html + ''';}'''

ctx = execjs.compile(js_1)
js_2 = ctx.call("clearance")

print(js_2)
js_2 = "function clearance2(){ var a" + js_2.split("document.cookie")[1].split("Expires=Sun")[
    0] + "Path=/;';return a;};".replace("window.headless", "''")
js_2 = js_2.replace('return return','return eval')
print (js_2)
ctx_2 = execjs.compile(js_2)
clearance2 = ctx_2.call("clearance2")
print (clearance2)
cookie =__jsluid_h+';'+clearance2.split(';')[0]
print(cookie)
headers={'User-Agent':'Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.139 Safari/537.36',
         'cookie':'maxPageNum5097045=259; __jsluid=328605e1a60939bf562083db25a6a7a2; __jsluid_h=b7340093f10e6af8860e9ed62784cf63; zh_choose=n; __FTabceffgh=2019-7-5-15-47-18; __NRUabceffgh=1562312838048; __RECabceffgh=1; __RTabceffgh=2019-7-7-17-42-58; __jsl_clearance=1562500808.378|0|D%2FNiFP4GtCvWXcCy7ge4PnFdnKQ%3D',
         'Accept':'text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8',
         'Host':'www.mps.gov.cn'}
response = requests.get(url,headers)
print(response.text)





