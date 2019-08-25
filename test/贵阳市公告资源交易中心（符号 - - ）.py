 #encoding=utf-8
import requests,re,bs4,time,sys,hashlib,uuid,time,json,base64,rsa,platform,datetime,os,urllib

UserAgent = 'Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3497.92 Safari/537.36'
def get_time(format_date,time_pull = None):
    if time_pull:
        return int(time.mktime(time.strptime(time_pull, format_date))*1000)
def standard_work_list():
    return_data = []
    headers = {'User-Agent': UserAgent}
    res = requests.get('http://ggzy.guiyang.gov.cn/c14570/index.html', headers=headers)
    res.raise_for_status()
    reg_content = res.content.decode('utf-8','ignore')
    infos = re.findall(r'var dataList=(.+?)}];', reg_content)
    infos = infos[0].replace('[{"infolist":[', '').replace(']', '')
    infos = re.findall(r'"title":"(?P<title>.+?)",.+?"url":"(?P<url>.+?)",', infos)
    for one_info in infos:
        _one_info = str(one_info)
        if 'html' not in _one_info or 'tzgg' in _one_info:
            continue
        content_dir = one_info
        if content_dir:
            _datetime = 0
            _url_tmp = content_dir[1]

            print({'url': _url_tmp, 'title': content_dir[0].strip()})
            return_data.append({'url': _url_tmp, 'title': content_dir[0].strip(), 'datetime': _datetime})
    return return_data


def standard_work_article(target_url):
    return_data = []
    headers = {'User-Agent': UserAgent}
    res = requests.get(target_url, headers=headers)
    res.raise_for_status()
    reg_content = res.content.decode('utf-8','ignore')
    html_page = bs4.BeautifulSoup(reg_content, 'lxml')
    try:
        infos = html_page.find(id='zoom').findAll('p')
    except:
        return
    if not infos:
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
    date = re.search(r"(\d{4}-\d{1,2}-\d{1,2})", reg_content)
    datetime_dir = re.match('(?P<year>\d{4})-(?P<month>\d+?)-(?P<day>\d+)',
                            date[0])
    tt_tmp = '%s-%s-%s' % (
        datetime_dir['year'], datetime_dir['month'], datetime_dir['day'])

    _datetime = 0
    if datetime_dir:
        _datetime = get_time('%Y-%m-%d', tt_tmp)
    _title = html_page.find('title').text
    #_title = re.findall(r'<div class="three-page-title" style="color:#123d89!important;">(.+?)</div>', reg_content)

    return _datetime, '%s<replace title>%s' % (_title, '\n'.join(return_data))

def main():
   list = standard_work_list()
   for url in list:
       print(standard_work_article(url['url']))



if __name__ == '__main__':
    main()
