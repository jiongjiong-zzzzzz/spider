import requests
from lxml import etree
import time
import re
from news.mongodb import  insert

def download(url,i):
    session = requests.Session()
    headers = {
        "Accept": "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8",
        "Accept-Encoding": "gzip, deflate",
        "Accept-Language": "zh-CN,zh;q=0.9",
        "Connection": "keep-alive",
        'cookie': '_ga=GA1.2.1174699020.1564731287; remember_web_59ba36addc2b2f9401580f014c7f58ea4e30989d=eyJpdiI6IkJiazVrNjhVN1JBTGhTTnZ3TFFTK0E9PSIsInZhbHVlIjoiUWNDQ3ZZWklFdVI5eWR2ZWphVnk2dWNVb1p1U21iN25Bc2hjYjZLMUtHOEdLaHFoTXBYSTFRXC96VHIyWUJzXC8zdU1HVnhmMFAyanRvb3I1OHBxMGFzK3R3WUJLTHFGV0MzSCtKZ3oxYzZaaHcyaFJcL1d4Q3JcL0c3NVdNQW5WeE1QaTFzMjdWQ2w3RHQ3Tm05cHFmTXhacEhqRCtYXC84SWJ1RmNLZ3Nxd0xCWTQ9IiwibWFjIjoiOTMxMGNjZWNlMTE0ZjhlYjA4Mjc3MTU3ZjhlM2UxZjE1M2I0MjE5MTA1NDQ2OTI5Y2UwMmY5NzkyMTgzMDJlZiJ9; Hm_lvt_020fbaad6104bcddd1db12d6b78812f6=1564970028,1565070543,1565088436,1565244426; _gid=GA1.2.1282572402.1565244426; XSRF-TOKEN=eyJpdiI6IkF2K1A0OE1kUHNGZ1hPMkdQOWo4NEE9PSIsInZhbHVlIjoiSVBrOXlVVVJFTFZ6bjhudkxCRFpZazdTN0JPR2lmUmJMb1E2OHhwRVV2QlNZM3dHK2NIalNaUkpCRkxrWlNWdyIsIm1hYyI6ImEzNWU5N2FmMzAyNGI5ZjcxOGRmMzhhNzBlNjY3NzFhNGJkMmFjMjM3M2QzOWQ0N2JjYWQzOTQyNTVjZTJkMjcifQ%3D%3D; glidedsky_session=eyJpdiI6IkdXb0pqdnZueDFtNFNOT2VMR2xFSHc9PSIsInZhbHVlIjoiNWRQajVOMlwvbUd2STY1Qjc3NTVPZVV2WVBMTzV4N3BDMHB6RHU2OW10cFVNeHI0WjlxblNSQjBaK05iQWRRTFAiLCJtYWMiOiI0NDI3YzNiZDNhNDc4YzYzNjRlNTBmNTdjZTMyNGRmOGNhNmFmNTM4NWI3NDgyZWRkYThiMjIyNzU0NGRhNzgwIn0%3D; Hm_lpvt_020fbaad6104bcddd1db12d6b78812f6=1565245311',
        "Host": "glidedsky.com",
        "Referer": "http://glidedsky.com/level/web/crawler-font-puzzle-2",
        "Upgrade-Insecure-Requests": "1",
        "User-Agent": "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.77 Safari/537.36",
    }
    session.headers.update(headers)
    try:
        r = session.get(url)
        r.raise_for_status()
        return r.text
    except Exception:
        print(Exception)
        return download(url, i)

def prase(url,i,count):
    text = download(url,i)
    woff_ = re.search(r'url\("(.*\.woff)"\)', text).group(1)
    return text,woff_
def main():
    count = 0
    for i in range(1,1001):
        print(i,count)
        url = 'http://glidedsky.com/level/web/crawler-font-puzzle-2?page={}'.format(i)
        text,woff_ = prase(url,i,count)
        item={'url':url,'html':text,'woff':woff_}
        insert(item)

    print(count)
if __name__ == '__main__':
    main()




