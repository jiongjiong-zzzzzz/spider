#encoding=utf-8
import requests,re,bs4,time,sys,hashlib,uuid,time,json,base64,rsa,platform,datetime,os,urllib

UserAgent = 'Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3497.92 Safari/537.36'
def get_time(format_date,time_pull = None):
    if time_pull:
        return int(time.mktime(time.strptime(time_pull, format_date))*1000)
def standard_work_list():
    return_data = []
    headers = {'User-Agent': UserAgent}
    res = requests.get('http://city.ce.cn/main/cityweek/', headers=headers)
    res.raise_for_status()
    reg_content = res.content.decode('utf8')
    html_page = bs4.BeautifulSoup(reg_content, 'lxml')
    infos = html_page.find(class_='w_650').findAll('a')
    for one_info in infos:
        _one_info = str(one_info)
        if 'news' not in _one_info:
            continue
        content_dir = re.match('<a.+href="(?P<url>.+?)" target="_blank">(?P<title>.+?)</a>', _one_info)

        xiangxi=urllib.parse.unquote_plus('%E8%AF%A6%E7%BB%86')
        if xiangxi in content_dir['title']:
            continue
        if content_dir:

            _datetime = 0
            _url_tmp = content_dir['url'].strip()
            _url_tmp = _url_tmp.replace('../..','http://city.ce.cn')
            print({'url': _url_tmp, 'title': content_dir['title'].strip()})
            return_data.append({'url': _url_tmp, 'title': content_dir['title'].strip(), 'datetime': _datetime})
    return return_data


def standard_work_article(target_url):
    return_data = []
    headers = {'User-Agent': UserAgent}
    res = requests.get(target_url, headers=headers)
    res.raise_for_status()
    reg_content = res.content.decode('utf8')
    html_page = bs4.BeautifulSoup(reg_content, 'lxml')
    infos = html_page.find(class_='TRS_Editor').findAll('p')
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

    time = html_page.find("span", {'id': 'articleTime'}).text.strip()
    nian = urllib.parse.unquote_plus('%E5%B9%B4')
    yue = urllib.parse.unquote_plus('%E6%9C%88')
    ri = urllib.parse.unquote_plus('%E6%97%A5')
    datetime_dir = re.match(
        '(?P<year>\d{4})%s(?P<month>\d+?)%s(?P<day>\d+?)%s (?P<hour>\d+?):(?P<minute>\d+?)' % (nian, yue, ri),
        time)
    tt_tmp = '%s-%s-%s %s:%s' % (
    datetime_dir['year'], datetime_dir['month'], datetime_dir['day'], datetime_dir['hour'], datetime_dir['minute'])
    _datetime = 0
    if datetime_dir:
        _datetime = get_time('%Y-%m-%d %H:%M', tt_tmp)
    _title = html_page.find(id='articleTitle').text
    return _datetime, '%s<replace title>%s' % (_title, '\n'.join(return_data))

def main():
   list = standard_work_list()
   for url in list:
       print(standard_work_article(url['url']))



if __name__ == '__main__':
    main()
