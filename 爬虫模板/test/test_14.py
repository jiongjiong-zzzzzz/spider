import requests
from lxml import etree
session = requests.Session()
headers = {'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.139 Safari/537.36',
            'Host': 'www.hongshu.com',
            'Accept': 'text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8',
            'cookie':'__uuid__=bb8a5afd-d82f-b5a2-99c7-ee7068131a66; hscrcolor=1; PHPSESSID=rfnvjiqodjd8qauml4nbf1ci97; Hm_lvt_5268a54e187670ee0953ba36efee746a=1565687426; hsclastchapter96363=%7B%22title%22%3A%22%E7%AC%AC%E4%B8%80%E7%AB%A0%20%E5%86%8D%E8%A7%81%E7%9B%91%E7%8B%B1%E9%95%BF%22%2C%20%22bid%22%3A%2296363%22%2C%22jid%22%3A%22167675%22%2C%22cid%22%3A%2214663603%22%2C%22curpos%22%3A%221%22%2C%22total%22%3A%2278%22%2C%20%22addtime%22%3A%221565687531%22%7D; Hm_lpvt_5268a54e187670ee0953ba36efee746a=1565687532'}
url = 'https://www.hongshu.com/bookajax.do'
data = {
    'method':'getchpcontent',
    'bid':'96363',
    'jid': '167675',
    'cid':'14663603'
}
session.headers.update(headers)
r = session.post(url,data=data,headers=headers)
text = r.text
print(text)
