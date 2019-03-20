from urllib.request import urlopen,Request
from http.client import HTTPResponse

url='http://www.bing.com'
ua='Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.139 Safari/537.36'
req=Request(url,headers={
    'user-Agent':ua
})
response = urlopen(req,timeout=5)
print (response.closed)

with response:
    print (type(response))
    print (response.status)
    print (response._method)
    print (response.read())
    #print (response,info())
    print (response.geturl())
print (response.closed)