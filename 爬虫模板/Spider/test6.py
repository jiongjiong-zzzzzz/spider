import requests

url = 'https://en.artprecium.com/catalogue/vente_309_more-than-unique-multiples-estampes/resultat'

headers = {
    "Accept": "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8",
    "User-Agent": "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/67.0.3396.99 Safari/537.36"
}
# 构造form表单
data = {"IdEpoque": "",
        "MotCle": "",
        "Order": "",
        "LotParPage": "All",
        "IdTypologie": ""}

response = requests.post(url=url, data=data, headers=headers, timeout=10)

print(response)
