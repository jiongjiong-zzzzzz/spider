#encoding=utf-8
import requests,re,bs4,time,sys,hashlib,uuid,time,json,base64,rsa,platform,datetime,os,urllib

UserAgent = 'Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3497.92 Safari/537.36'
def get_time(format_date,time_pull = None):
    if time_pull:
        return int(time.mktime(time.strptime(time_pull, format_date))*1000)
def standard_work_list():
    return_data = []
    headers = {'User-Agent': UserAgent}
    res = requests.get('http://robot.ofweek.com/CATList-8321200-8100-robot.html', headers=headers)
    res.raise_for_status()
    reg_content = res.content.decode('GBK')
    html_page = bs4.BeautifulSoup(reg_content, 'lxml')
    infos = html_page.findAll('h3')
    for one_info in infos:
        _one_info = str(one_info)
        if '<img alt' in str(one_info):
            content_dir = re.match('<h3><a href="(?P<url>.+?)" target="_blank"><img alt="".+/>(?P<title>.+?)</a></h3>',
                                   _one_info)
        else:
            content_dir = re.match('<h3><a.+href="(?P<url>.+?)" target="_blank">(?P<title>.+?)</a></h3>', _one_info)
        if content_dir:
            _datetime = 0
            _url_tmp = content_dir['url'].strip()
            print({'url': _url_tmp, 'title': content_dir['title'].strip()})
            return_data.append({'url': _url_tmp, 'title': content_dir['title'].strip(), 'datetime': _datetime})
    return return_data


def standard_work_article(target_url):
    return_data = []
    headers = {'User-Agent': UserAgent}
    res = requests.get(target_url, headers=headers)
    res.raise_for_status()
    try:
        reg_content = res.content.decode('GBK')
    except:
        reg_content = res.content.decode('utf8', 'ignore')
    html_page = bs4.BeautifulSoup(reg_content, 'lxml')
    try:
        infos = html_page.find(class_='article_con').findAll('p')
    except:
        return
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

    tim = html_page.find(class_='sdate').text
    datetime_dir = re.match('(?P<year>\d{4})-(?P<month>\d+?)-(?P<day>\d+?) (?P<hour>\d+?):(?P<minute>\d+)',
                            tim.strip())
    tt_tmp = '%s-%s-%s %s:%s' % (
    datetime_dir['year'], datetime_dir['month'], datetime_dir['day'], datetime_dir['hour'], datetime_dir['minute'])
    _datetime = 0
    if datetime_dir:
        _datetime = get_time('%Y-%m-%d %H:%M', tt_tmp)
    _title = html_page.find('h1').text
    return _datetime, '%s<replace title>%s' % (_title, '\n'.join(return_data))

def main():
   list = standard_work_list()
   for url in list:
       print(standard_work_article(url['url']))



if __name__ == '__main__':
    main()
