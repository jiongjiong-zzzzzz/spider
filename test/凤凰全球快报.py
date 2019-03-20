#encoding=utf-8
import requests,re,bs4,time,sys,hashlib,uuid,time,json,base64,rsa,platform,datetime,os,urllib

UserAgent = 'Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3497.92 Safari/537.36'
def get_time(format_date,time_pull = None):
    if time_pull:
        return int(time.mktime(time.strptime(time_pull, format_date))*1000)
def standard_work_list():
    return_data = []
    headers = {'User-Agent': UserAgent}
    res = requests.get('http://finance.ifeng.com/shanklist/1-66-', headers=headers)
    res.raise_for_status()
    reg_content = res.content.decode('utf8')
    html_json = re.findall(r'.*"newsstream":(.+?),"cooperation".*', reg_content, re.S)
    data = json.loads(html_json[0])
    for one_info in data:
        _datetime = 0
        _url_tmp=one_info['url']
        title=one_info['title']
        return_data.append({'url': _url_tmp, 'title':title , 'datetime': _datetime})
    return return_data

def standard_work_article(target_url):
    return_data = []
    headers = {'User-Agent': UserAgent}
    res = requests.get(target_url, headers=headers)
    res.raise_for_status()
    reg_content = res.content.decode('utf8')
    html_json = re.findall(r'.*"contentList":(.+?),"currentPage".*', reg_content, re.S)
    content_dir=html_json[0]
    data = json.loads(content_dir)
    content=data[0]['data']
    if '<p>' not in content:
        content = data[1]['data']
    html_page = bs4.BeautifulSoup(content, 'lxml')
    infos = html_page.findAll('p')
    for one_info in infos:
        content_dir = re.search('<p[\\s\\S]+/p>', str(one_info))
        if content_dir:
            need_content = one_info.text
            need_content = need_content.strip()
        else:
            if isinstance(one_info, bs4.NavigableString):
                need_content = one_info
            else:
                continue
        if not need_content.strip():
            continue
        return_data.append(need_content.strip())
    time = re.findall(r'"newsTime":"([^"]+)"', reg_content, re.I)[0]
    datetime_dir = re.match('(?P<year>\d{4})-(?P<month>\d+?)-(?P<day>\d+?) (?P<hour>\d+?):(?P<minute>\d+):\d+' ,
                            time)
    tt_tmp = '%s-%s-%s %s:%s' % (
    datetime_dir['year'], datetime_dir['month'], datetime_dir['day'], datetime_dir['hour'], datetime_dir['minute'])
    _datetime = 0
    if datetime_dir:
        _datetime = get_time('%Y-%m-%d %H:%M', tt_tmp)
    _title = re.findall(r'.*<title>(.+?)</title>.*', reg_content, re.S)[0]
    return _datetime, '%s<replace title>%s' % (_title, '\n'.join(return_data))
def main():
   list = standard_work_list()
   for url in list:
       print(standard_work_article(url['url']))

if __name__ == '__main__':
    main()
