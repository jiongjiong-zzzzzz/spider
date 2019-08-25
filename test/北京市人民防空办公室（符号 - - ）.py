 #encoding=utf-8
import requests,re,bs4,time,sys,hashlib,uuid,time,json,base64,rsa,platform,datetime,os,urllib

UserAgent = 'Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3497.92 Safari/537.36'
def get_time(format_date,time_pull = None):
    if time_pull:
        return int(time.mktime(time.strptime(time_pull, format_date))*1000)
def standard_work_list():

    return_data = []
    headers = {'User-Agent': UserAgent,
               'Referer':'http://rfb.beijing.gov.cn/homeweb/minfang/dtxx.html',
               'Origin':'http://rfb.beijing.gov.cn',
               'Host':'103.83.44.94:8090'}
    data = {'start':'0','count':12}
    res = requests.post('http://103.83.44.94:8090/FLAME/pagecitydynamic',data=data, headers=headers)
    res.raise_for_status()
    reg_content = res.content.decode('UTF-8')
    data = json.loads(reg_content)
    data = data['list']

    for one_info in data:
        _one_info = str(one_info)
        id_1 = one_info['dict']['id']
        id_2 = one_info['id']
        content_dir = one_info
        _datetime = 0
        _url_tmp = 'http://rfb.beijing.gov.cn/homeweb/minfang/detail_news/newsdetail.html?dictId={}&newsId={}'.format(id_1,id_2)
        print({'url': _url_tmp, 'title': one_info['title']})
        return_data.append({'url': _url_tmp, 'title': one_info['title'], 'datetime': _datetime})
    return return_data


def standard_work_article(target_url):
    return_data = []
    headers = {'User-Agent': UserAgent}
    res = requests.get(target_url, headers=headers)
    res.raise_for_status()
    reg_content = res.content.decode('UTF-8')
    html_page = bs4.BeautifulSoup(reg_content, 'lxml')
    infos = html_page.find(id='detail').findAll('p')
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
    return _datetime, '%s<replace title>%s' % (_title, '\n'.join(return_data))

def main():
   list = standard_work_list()
   for url in list:
       print(standard_work_article(url['url']))



if __name__ == '__main__':
    main()
