import requests
from lxml import etree
import  re
session = requests.Session()
headers = {'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.139 Safari/537.36',
            'Referer': 'http://glidedsky.com/level/crawler-basic-1',
            'Host': 'glidedsky.com',
            'Accept': 'text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8',
            'Accept-Encoding': 'gzip, deflate',
            'Accept-Language': 'zh-CN,zh;q=0.9',
           'cookie':'_ga=GA1.2.1976315618.1564047267; _gid=GA1.2.925251583.1564047267; remember_web_59ba36addc2b2f9401580f014c7f58ea4e30989d=eyJpdiI6ImtzdTdSU292bkxqRTlkaDh5c1E1Mmc9PSIsInZhbHVlIjoiblNROGUraUkweUt0RW5kSGhtNUJnWlNGXC91Tzk5bTBZMUd6NXZVOUQ0djF0S3VEWnJtTzNkOENBTkkwenFDXC9xQk1QTFpCRG14N1M4MEZNZHNOZUlOY21PZ0Jzb3J0akIza083dTIzTlN5UDA5RzZXVll6bXlwRUphMjBScjNBTHdyazliUlREV3RvK2tyWFhHVENUSlVXZjRuWFBsaTJhZXdnNEJOM2hxZEU9IiwibWFjIjoiZThlMTUwNGFhOTFjYzkyOGI1MjQxMTBlZGNiMDQ3NjAxNjIxMzE4ZmVlNWFjMjNlNmI2NDc1MGQyMzI1ODBlNyJ9; Hm_lvt_020fbaad6104bcddd1db12d6b78812f6=1564108884,1564235050,1564313055,1564384899; _gat_gtag_UA_75859356_3=1; XSRF-TOKEN=eyJpdiI6Im1DQVBjR0NMVTVOcmNGb2FHWGU1VXc9PSIsInZhbHVlIjoiN2VJYktQZnRuR0pJMElKb1Awc1hmOU8raWtjNnZXdllUYW9aZ2tSTW00WWhqWm94R0hSS3pRdUpEdHh2YzlDRiIsIm1hYyI6ImRlZGIwYmY0ZjVmNGZlYzQ2ODUzZGZlNjk3MTM1ZmUyZTJiYTAzZDZkMThkNGQzOWJlMWFiMGIxNjMyNDk3NjUifQ%3D%3D; glidedsky_session=eyJpdiI6ImwzRmhKOUJ1cXVUZE1mRm5xXC8zQ1F3PT0iLCJ2YWx1ZSI6IlprS216S08rQlB2RGFnXC9UbmhBNnhXMzhYT1NmZEkxZ3hJVnM2QnZxSzh4Z2JcL1RBNUxuVHFsTWE1ZnIxVTFSdSIsIm1hYyI6IjY3ZWIwOWQ1ZGUxMmEwYmIwYWNkMjc2ZDcxM2YxNGY1MDFlMjU0NWRjYjZmNjQxMDBjNGUyODU2NDlmMGYzYmEifQ%3D%3D; Hm_lpvt_020fbaad6104bcddd1db12d6b78812f6=1564385755'}
url = 'http://glidedsky.com/level/web/crawler-css-puzzle-1?page={}'
session.headers.update(headers)
count = 0
for i in range(1,1001):
    print(i)
    r = session.get(url.format(i),headers=headers)
    text = r.text
    html = etree.HTML(text)
    div_html = html.xpath("//div[@class='col-md-1']")

    for div_list in div_html:
        divs = div_list.xpath("./div")
        if len(divs) == 2:
            num = 0
            num_list = []
            for div in divs:
                text_div = div.xpath("./text()")
                if text_div:
                    num_list.append(text_div)
            if len(num_list) == 2:
                number = [-1, -1]
                for i in range(0, len(divs)):
                    div = divs[i]
                    class_name = div.xpath("./@class")[0]
                    data = div.xpath("./text()")[0]
                    left = re.findall(r"\.%s \{ left\:(.*?)em.+?\}" % class_name, text)
                    if not left:
                        # 如果left为空，表名位置不需要调整
                        number[i] = data
                    # 否则就需要调整位置
                    else:
                        index = i + int(left[0])
                        number[index] = data
                num = "".join(number)
                print(num)
                count += int(num)
                continue
        if len(divs)<3:
            for div in divs:
                text_div = div.xpath("./text()")
                if not text_div:
                    class_name = div.xpath("./@class")[0]
                    num = re.findall(r"\.%s\:before \{ content\:\"(\d*)\".+?\}"%class_name, text, re.S)[0]
                    print(num)
                    count += int(num)
        else:
            if len(divs) == 4:
                divs = divs[1:]
            number = [-1, -1, -1]
            for i in range(0, len(divs)):
                div = divs[i]
                class_name = div.xpath("./@class")[0]
                data = div.xpath("./text()")[0]
                opacity = re.findall(r"\.%s \{ opacity\:(.*?).+?\}"%class_name, text)
                if opacity:
                    continue
                left = re.findall(r"\.%s \{ left\:(.*?)em.+?\}"%class_name, text)
                if not left:
                    # 如果left为空，表名位置不需要调整
                    number[i] = data
                # 否则就需要调整位置
                else:
                    index = i + int(left[0])
                    number[index] = data
            try:
                num = "".join(number)
            except:
                print(number)
                number=number[1:]
                num = "".join(number)
            count += int(num)
            print(num)
    print(count)
print(count)


