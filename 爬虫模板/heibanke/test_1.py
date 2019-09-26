import requests
from lxml import etree
import  re

def get_html(url,session):
    r = session.get(url)
    text = r.text
    html = etree.HTML(text)
    nums = html.xpath("//h3/text()")
    print(nums[0])
    num = re.findall(r"\d+",nums[0])
    url = 'http://www.heibanke.com/lesson/crawler_ex00/{}'.format(num[0])
    get_html(url, session)
def main():
    session = requests.Session()
    headers = {
        'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.139 Safari/537.36',
        'Host': 'www.heibanke.com',
        'Accept': 'text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8',
        'cookie': 'csrftoken=y82A4dB75GwzmAazpISTPnBgxNUDbsec; sessionid=t770x8ps5hxg0p4getnri1dgh9mrhn1n; Hm_lvt_74e694103cf02b31b28db0a346da0b6b=1565597117,1565598420; Hm_lpvt_74e694103cf02b31b28db0a346da0b6b=1565598824'}
    url = 'http://www.heibanke.com/lesson/crawler_ex00/'
    session.headers.update(headers)

    get_html(url,session)
if __name__ == '__main__':
    main()



