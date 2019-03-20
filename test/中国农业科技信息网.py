#encoding=utf-8
import requests,re,bs4,time,sys,hashlib,uuid,time,json,base64,rsa,platform,datetime,os,urllib

UserAgent = 'Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3497.92 Safari/537.36'
def get_time(format_date,time_pull = None):
    if time_pull:
        return int(time.mktime(time.strptime(time_pull, format_date))*1000)
def standard_work_list():
    return_data = []
    headers = {'User-Agent': UserAgent}
    res = requests.get('http://www.cast.net.cn/zx/kjyw/index.shtml', headers=headers)
    res.raise_for_status()
    reg_content = res.content.decode('gb2312')
    html_page = bs4.BeautifulSoup(reg_content, 'lxml')
    infos = html_page.find(id='ul').findAll('a')
    for one_info in infos:
        content_dir=one_info
        _datetime = 0
        _url_tmp = 'http://www.cast.net.cn/zx/kjyw/'+content_dir['href'].strip()
        print({'url': _url_tmp, 'title': content_dir['title'].strip()})
        return_data.append({'url': _url_tmp, 'title': content_dir['title'].strip(), 'datetime': _datetime})
    return return_data


def standard_work_article(target_url):
    return_data = []
    headers = {'User-Agent': UserAgent}
    res = requests.get(target_url, headers=headers)
    res.raise_for_status()
    try:
        reg_content = res.content.decode('gb2312')
    except:
        reg_content = res.content.decode('gb2312','ignore')
    html_page = bs4.BeautifulSoup(reg_content, 'lxml')
    infos = html_page.find(class_='zhengwen').text
    need_content = infos
    return_data.append(need_content.strip())
    date = re.search(r"(\d{4}-\d{1,2}-\d{1,2})", reg_content)
    datetime_dir = re.match('(?P<year>\d{4})-(?P<month>\d+?)-(?P<day>\d+)',
                            date[0].strip())
    tt_tmp = '%s-%s-%s' % (
    datetime_dir['year'], datetime_dir['month'], datetime_dir['day'])
    _datetime = 0
    if datetime_dir:
        _datetime = get_time('%Y-%m-%d', tt_tmp)
    _title = html_page.find('h3').text
    return _datetime, '%s<replace title>%s' % (_title, '\n'.join(return_data))

def main():
   list = standard_work_list()
   for url in list:
       print(standard_work_article(url['url']))



if __name__ == '__main__':
    main()
