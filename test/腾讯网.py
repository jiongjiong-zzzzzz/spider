#encoding=utf-8
import requests,re,bs4,time,sys,hashlib,uuid,time,json,base64,rsa,platform,datetime,os,urllib

UserAgent = 'Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3497.92 Safari/537.36'
def get_time(format_date,time_pull = None):
    if time_pull:
        return int(time.mktime(time.strptime(time_pull, format_date))*1000)
def standard_work_list():
    return_data = []
    headers = {'User-Agent': UserAgent}
    res = requests.get('https://pacaio.match.qq.com/irs/rcd?cid=52&token=8f6b50e1667f130c10f981309e1d8200&ext=3921,3916&page=1&isForce=1&expIds=20190301A0QBIJ|20190301A0I3UV&callback=__jp5', headers=headers)
    res.raise_for_status()
    reg_content = res.content.decode('utf-8')
    html_json = re.search("\[.*\]", reg_content, re.S)
    data = json.loads(html_json[0])
    for one_info in data:
        _one_info = str(one_info)
        _one_info = _one_info.replace('\r\n','').strip()
        content_dir = one_info
        if content_dir:
            _datetime = 0
            _url_tmp = one_info['vurl']
            title = one_info['title']
            print(_url_tmp+' '+title)
            return_data.append({'url': _url_tmp, 'title': title, 'datetime': _datetime})
    return return_data


def standard_work_article(target_url):
    return_data = []
    headers = {'User-Agent': UserAgent}
    res = requests.get(target_url, headers=headers)
    res.raise_for_status()
    try:
        reg_content = res.content.decode('utf-8','ignore')
        html_page = bs4.BeautifulSoup(reg_content, 'lxml')
        infos = html_page.find(class_='content-box').findAll('p')
    except:
        reg_content = res.content.decode('gb2312', 'ignore')
        html_page = bs4.BeautifulSoup(reg_content, 'lxml')
        infos = html_page.find(class_='content-article').findAll('p')
    for one_info in infos:
        content_dir = re.search('<p[\\s\\S]+/p>', str(one_info))
        if content_dir:
            need_content = one_info.text
        else:
            if isinstance(one_info, bs4.NavigableString):
                need_content = one_info
            else:
                continue
        if not need_content.strip():
            continue
        return_data.append(need_content.strip())

    tim = re.search(r"(\d{4}-\d{1,2}-\d{1,2}\s\d{1,2}:\d{1,2})", reg_content)
    datetime_dir = re.match('(?P<year>\d{4})-(?P<month>\d+?)-(?P<day>\d+?) (?P<hour>\d+?):(?P<minute>\d+)',
                            tim[0])
    tt_tmp = '%s-%s-%s %s:%s' % (datetime_dir['year'], datetime_dir['month'], datetime_dir['day'], datetime_dir['hour'], datetime_dir['minute'])
    _datetime = 0
    if datetime_dir:
        _datetime = get_time('%Y-%m-%d %H:%M', tt_tmp)
    _title = html_page.find('title').text
    tit = urllib.parse.unquote_plus('%E5%A4%A9%E5%A4%A9%E5%BF%AB%E6%8A%A5')
    if tit in _title:
        _title = html_page.find(class_='title').text
    _title = _title.replace('\r','').replace('\n','').replace('\t','').strip()
    return _datetime, '%s<replace title>%s' % (_title, '\n'.join(return_data))

def main():
   list = standard_work_list()
   for url in list:
       print(standard_work_article(url['url']))



if __name__ == '__main__':
    main()
