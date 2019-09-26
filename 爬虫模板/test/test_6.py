import requests
from lxml import etree
session = requests.Session()
headers = {'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.139 Safari/537.36',
            'Referer': 'http://glidedsky.com/level/crawler-basic-1',
            'Host': 'glidedsky.com',
            'Accept': 'text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8',
            'Accept-Encoding': 'gzip, deflate',
            'Accept-Language': 'zh-CN,zh;q=0.9',
            'cookie':'_ga=GA1.2.1976315618.1564047267; remember_web_59ba36addc2b2f9401580f014c7f58ea4e30989d=eyJpdiI6IkZZdWRGYVBXbXQ2bGFTRzl6bVQwR3c9PSIsInZhbHVlIjoibzBjMTl0dnhyQ0xcL1ZhSnhyQW4xWGlUXC94dkI0eVlyYzh4UFU5YW5LTXAxY2xNOEYrVkhGSnFzeFJuUUVLYTJ1Sm8xM1B6UlJlZFRHS1FRaVwvN0Zib3doVzdYaksxTWNSVGdPYWdcLzdmTnFUVFJZQ1VKalVOdUFDSTNvaDVMV2ZxRGMzemJLVDl6bk44R1Urejlyb3RxVk41ZnA3WjBBU1hxUE94NWp0bENKRT0iLCJtYWMiOiIwNzVmOGMwYWE2MzI1YTA3MDhhMDY3MzU3OWVmNTU3OWZhOWY4MjQxNjkxOTBhNmNiNzZlOTFmZmUzMWVkNjc5In0%3D; Hm_lvt_020fbaad6104bcddd1db12d6b78812f6=1564313055,1564384899,1564463614,1564564055; _gid=GA1.2.98221679.1564564055; XSRF-TOKEN=eyJpdiI6Inl3UGg2U0FDRmNNbGY0ZjAzK0xiQUE9PSIsInZhbHVlIjoiQ0M3ZmkzSEVweWhZajFMK2ExcDIwMjN4YnN1ZWxJRE54cTdPTEVuYWozRmZRZ2J3OVltbzVmN2IydFJqakpvdSIsIm1hYyI6ImVmMTFlMmM0NWNlOTg4YzJkZGMzZWFiMjE5ZjU1MGJiNDJlYzY0MjRkYjQ1Njk2ODVmMTRmMmEwNTI2MzRlZGUifQ%3D%3D; glidedsky_session=eyJpdiI6ImVcL2RjQ3NLYkNNV0FpcGNFSVhaRlRRPT0iLCJ2YWx1ZSI6IlZlaGorb2VzSHpyRlowWHVBN2lWSFVMSHo3MThYQTlHcDhTNEw0R0xaS1k3T0tUdGdJZ01YN0FETkZvTklmWlkiLCJtYWMiOiI3ZjJhMTFiOTA2ZWJiMzY1YTZiOWUyZjUzODJkMGQyYzEwMDkxYmJkYWExZTAyMzY4ZjAyYzE4NmYzZTc5ZjVlIn0%3D; Hm_lpvt_020fbaad6104bcddd1db12d6b78812f6=1564564071'}
url = 'http://glidedsky.com/level/web/crawler-captcha-1'
session.headers.update(headers)
r = session.get(url,headers=headers)
text = r.text
html = etree.HTML(text)
nums = html.xpath("//div[@class='col-md-1']/text()")
count = 0
import  re
for num in nums:
    num = re.findall(r"\d+",num)
    count += int(num[0])
print(count)