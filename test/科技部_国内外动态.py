#encoding=utf-8
import requests,re,bs4,time,sys,hashlib,uuid,time,json,base64,rsa,platform,datetime,os,urllib

UserAgent = 'Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3497.92 Safari/537.36'
def get_time(format_date,time_pull = None):
    if time_pull:
        return int(time.mktime(time.strptime(time_pull, format_date))*1000)
def standard_work_list():
    return_data = []
    headers = {'User-Agent': UserAgent}
    res = requests.get('http://www.most.gov.cn/gnwkjdt/index.htm', headers=headers)
    res.raise_for_status()
    reg_content = res.content.decode('gb2312')
    html_page = bs4.BeautifulSoup(reg_content, 'lxml')
    infos = html_page.findAll(class_='STYLE30')
    for one_info in infos:
        _one_info = str(one_info)
        if 'mag_tt' in _one_info or 'cysdjd_nr_lt' in _one_info or 'cysdjd_nr_rt' in _one_info or 'copybd_a' in _one_info or '...' in _one_info:
            continue
        if '<img alt' in str(one_info):
            content_dir = re.match('<a.+href="(?P<url>.+?)" target="_blank"><img alt="(?P<title>.+?)" .+/></a>',
                                   _one_info)
        else:
            content_dir = re.match('<a.+href="(?P<url>.+?)" target="_blank">(?P<title>.+?)</a>', _one_info)
        if content_dir:
            _datetime = 0
            _url_tmp = content_dir['url'].strip()
            _url_tmp=_url_tmp.replace('./','http://www.most.gov.cn/gnwkjdt/')
            print({'url': _url_tmp, 'title': content_dir['title'].strip()})
            return_data.append({'url': _url_tmp, 'title': content_dir['title'].strip(), 'datetime': _datetime})
    return return_data


def standard_work_article(target_url):
    return_data = []
    headers = {'User-Agent': UserAgent}
    res = requests.get(target_url, headers=headers)
    res.raise_for_status()
    reg_content = res.content.decode('gb2312')
    html_page = bs4.BeautifulSoup(reg_content, 'lxml')
    need_content = html_page.find(id='Zoom').text
    need_content = need_content.strip()
    return_data.append(need_content.strip())
    nian = urllib.parse.unquote_plus('%E5%B9%B4')
    yue = urllib.parse.unquote_plus('%E6%9C%88')
    ri = urllib.parse.unquote_plus('%E6%97%A5')
    tim = html_page.find(class_='gray12 lh22').text.strip()
    date = re.search(r"(\d{4}%s\d{1,2}%s\d{1,2}%s)"%(nian, yue, ri), tim)
    datetime_dir = re.match('(?P<year>\d{4})%s(?P<month>\d+?)%s(?P<day>\d+?)%s' % (nian, yue, ri), date[0])
    tt_tmp = '%s-%s-%s' % (
        datetime_dir['year'], datetime_dir['month'], datetime_dir['day'])
    _datetime = 0
    if datetime_dir:
        _datetime = get_time('%Y-%m-%d', tt_tmp)
    _title = html_page.find(id='Title').text
    return _datetime, '%s<replace title>%s' % (_title, '\n'.join(return_data))

def main():
   list = standard_work_list()
   for url in list:
       print(standard_work_article(url['url']))



if __name__ == '__main__':
    main()
