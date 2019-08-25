 #encoding=utf-8
import requests,re,bs4,time,sys,hashlib,uuid,time,json,base64,rsa,platform,datetime,os,urllib

UserAgent = 'Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3497.92 Safari/537.36'
def get_time(format_date,time_pull = None):
    if time_pull:
        return int(time.mktime(time.strptime(time_pull, format_date))*1000)
def standard_work_list():
    return_data = []
    headers = {'User-Agent': UserAgent}
    res = requests.get('http://rst.hebei.gov.cn/a/news/', headers=headers)
    res.raise_for_status()
    reg_content = res.content.decode('utf-8','ignore')
    infos = re.findall(r'<a href="(?P<url>.+?)" target="_blank">(?P<title>.+?)</a>',reg_content)

    for one_info in infos:
        _one_info = str(one_info)
        if 'img' in _one_info  or 'lianxiwomen'  in _one_info or 'coremail'  in _one_info or 'banquanshengm'  in _one_info or 'beian' in _one_info:
            continue
        content_dir = one_info
        if content_dir:
            _datetime = 0
            _url_tmp = content_dir[0]
            if 'http' not in _url_tmp:
                _url_tmp = 'http://rst.hebei.gov.cn'+content_dir[0]
            pattern = re.compile('[0-9]+')
            match = pattern.findall(_url_tmp)
            if match:
                print({'url': _url_tmp, 'title': content_dir[1].strip()})
                return_data.append({'url': _url_tmp, 'title': content_dir[1], 'datetime': _datetime})
    return return_data


def standard_work_article(target_url):
    return_data = []
    headers = {'User-Agent': UserAgent}
    res = requests.get(target_url, headers=headers)
    res.raise_for_status()
    reg_content = res.content.decode('utf-8','ignore')
    html_page = bs4.BeautifulSoup(reg_content, 'lxml')
    infos = html_page.find(class_='body').text.replace('\n','').replace('\t','').replace('\xa0','').replace('\r','')
    if not infos:
        return
    return_data.append(infos)
    date = re.search(r"(\d{4}-\d{1,2}-\d{1,2})", reg_content)
    datetime_dir = re.match('(?P<year>\d{4})-(?P<month>\d+?)-(?P<day>\d+)',
                            date[0])
    tt_tmp = '%s-%s-%s' % (
        datetime_dir['year'], datetime_dir['month'], datetime_dir['day'])

    _datetime = 0
    if datetime_dir:
        _datetime = get_time('%Y-%m-%d', tt_tmp)
    _title = html_page.find('title').text
    return _datetime, '%s<replace title>%s' % (_title, '\n'.join(return_data))

def main():
   list = standard_work_list()
   for url in list:
       print(standard_work_article(url['url']))



if __name__ == '__main__':
    main()
