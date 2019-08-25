 #encoding=utf-8
import requests,re,bs4,time,sys,hashlib,uuid,time,json,base64,rsa,platform,datetime,os,urllib

UserAgent = 'Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3497.92 Safari/537.36'
def get_time(format_date,time_pull = None):
    if time_pull:
        return int(time.mktime(time.strptime(time_pull, format_date))*1000)
def standard_work_list():
    return_data = []
    headers = {'User-Agent': UserAgent}
    res = requests.get('http://yjglj.beijing.gov.cn/col/col98/index.html', headers=headers)
    res.raise_for_status()
    reg_content = res.content.decode('UTF-8')
    html_page = bs4.BeautifulSoup(reg_content, 'lxml')
    infos = re.findall(r'''<a href='(?P<url>.+?)'title='(?P<title>.+?)'target="_blank">.+?</a>''',reg_content)
    for one_info in infos:
        _one_info = str(one_info)

        content_dir = one_info
        if content_dir:
            _datetime = 0
            if 'art'  in content_dir[0]:
                _url_tmp = 'http://yjglj.beijing.gov.cn'+content_dir[0]
            else:continue
            print({'url': _url_tmp, 'title': content_dir[1]})
            return_data.append({'url': _url_tmp, 'title': content_dir[1], 'datetime': _datetime})
    return return_data


def standard_work_article(target_url):
    return_data = []
    headers = {'User-Agent': UserAgent}
    res = requests.get(target_url, headers=headers)
    res.raise_for_status()
    reg_content = res.content.decode('UTF-8')
    html_page = bs4.BeautifulSoup(reg_content, 'lxml')
    infos = html_page.find(id='zoom').findAll('p')
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
    nian = urllib.parse.unquote_plus('%e5%8f%91%e5%b8%83%e6%97%a5%e6%9c%9f%ef%bc%9a')
    date = re.search(r'{}(?P<time>.*?)</td>'.format(nian), reg_content,re.S)
    date=date[0].replace('\r','').replace('\n','').replace('\t','').replace('</td>','').replace(nian,'')
    datetime_dir = re.match('(?P<year>\d{4})-(?P<month>\d+?)-(?P<day>\d+)',
                            date)
    tt_tmp = '%s-%s-%s' % (
        datetime_dir['year'], datetime_dir['month'], datetime_dir['day'])
    _datetime = 0
    if datetime_dir:
        _datetime = get_time('%Y-%m-%d', tt_tmp)
    _title = html_page.find(class_='title_w').text.replace('\r','').replace('\n','').replace('\t','')
    return _datetime, '%s<replace title>%s' % (_title, '\n'.join(return_data))

def main():
   list = standard_work_list()
   for url in list:
       print(standard_work_article(url['url']))



if __name__ == '__main__':
    main()
