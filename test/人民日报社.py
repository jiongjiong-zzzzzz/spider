import requests
import json
import requests,re,bs4,time,sys,hashlib,uuid,time,json,base64,rsa,platform,datetime,os,urllib


UserAgent = 'Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3497.92 Safari/537.36'
def get_time_stamp13():
    # 生成13时间戳   eg:1540281250399895
    datetime_now = datetime.datetime.now()

    # 10位，时间点相当于从1.1开始的当年时间编号
    date_stamp = str(int(time.mktime(datetime_now.timetuple())))

    # 3位，微秒
    data_microsecond = str("%06d"%datetime_now.microsecond)[0:3]

    date_stamp = date_stamp+data_microsecond
    return int(date_stamp)

def get_time(format_date,time_pull = None):
    if time_pull:
        return int(time.mktime(time.strptime(time_pull, format_date))*1000)
def standard_work_list():
    return_data = []
    url_list = []
    headers = {'User-Agent': UserAgent}
    url='http://news.people.com.cn/210801/211150/index.js?_=%s'%(get_time_stamp13())
    res = requests.get(url,headers=headers)
    res.raise_for_status()
    reg_content = res.content.decode('utf-8')
    data = json.loads(reg_content)
    data = data['items']
    for one_info in data:
        _datetime = 0
        _url_tmp=one_info['url']

        if _url_tmp in url_list:
            continue
        elif 'v.people' in _url_tmp or 'health.people' in _url_tmp:
            continue
        else:url_list.append(_url_tmp)
        title=one_info['title']
        print({'url': _url_tmp, 'title':title.strip()})
        return_data.append({'url': _url_tmp, 'title':title , 'datetime': _datetime})
    return return_data


def standard_work_article(target_url):
    return_data = []
    headers = {'User-Agent': UserAgent}
    res = requests.get(target_url, headers=headers)
    res.raise_for_status()
    reg_content = res.content.decode('GB2312','ignore')
    if 'clearfix jsy_2018' in  reg_content:
        return
    html_page = bs4.BeautifulSoup(reg_content, 'lxml')
    try:
        infos = html_page.find(class_='fl text_con_left').findAll('p')
    except:
        try:
            infos = html_page.find(id='p_content').findAll('p')
        except:
            try:
                infos = html_page.find(class_='text_con text_con01').findAll('p')
            except:
                try:
                    infos = html_page.find(class_='content clear clearfix').findAll('p')
                except:
                    try:
                        infos = html_page.find(class_='artDet').findAll('p')
                    except:
                        try:
                            infos = html_page.find(class_='gray box_text').findAll('p')
                        except:
                            try:
                                infos = html_page.find(class_='show_text').findAll('p')
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
    nian = urllib.parse.unquote_plus('%E5%B9%B4')
    yue = urllib.parse.unquote_plus('%E6%9C%88')
    ri = urllib.parse.unquote_plus('%E6%97%A5')
    date = re.search(r"(\d{4}%s\d{1,2}%s\d{1,2}%s\d{1,2}:\d{1,2})" % (nian, yue, ri), reg_content)
    datetime_dir = re.match(
        '(?P<year>\d{4})%s(?P<month>\d+?)%s(?P<day>\d+?)%s(?P<hour>\d+?):(?P<minute>\d+)' % (nian, yue, ri), date[0])
    tt_tmp = '%s-%s-%s %s:%s' % (
        datetime_dir['year'], datetime_dir['month'], datetime_dir['day'], datetime_dir['hour'], datetime_dir['minute'])
    _datetime = 0
    if datetime_dir:
        _datetime = get_time('%Y-%m-%d %H:%M', tt_tmp)
    try:
        _title = html_page.find('h1').text
    except:
        _title = html_page.find('title').text
    return _datetime, '%s<replace title>%s' % (_title, '\n'.join(return_data))
def main():
    list = standard_work_list()
    for url in list:
        print(url)
        print(standard_work_article(url['url']))


if __name__ == '__main__':
    main()
