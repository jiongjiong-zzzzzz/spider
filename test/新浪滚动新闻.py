#encoding=gb2312
import requests,re,bs4,time,sys,hashlib,uuid,time,json,base64,rsa,platform,datetime,os,urllib

UserAgent = 'Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3497.92 Safari/537.36'
def get_time(format_date,time_pull = None):
    if time_pull:
        return int(time.mktime(time.strptime(time_pull, format_date))*1000)
def standard_work_list():
    return_data = []
    headers = {'User-Agent': UserAgent}
    for page in range(1,53):
        url='https://feed.mix.sina.com.cn/api/roll/get?pageid=153&lid=2509&k=&num=50&page=%s&r=0.2039175258717716&callback=jQuery111202898175079273473_1548138061938&_=1548138061939'%(page)
        res = requests.get(url, headers=headers)
        res.raise_for_status()
        reg_content = res.content.decode('UTF-8')
        html_json = re.findall(r'.*"data":(.+?)}}[)].*', reg_content, re.S)
        data = json.loads(html_json[0])
        for one_info in data:
            _datetime = 0
            _url_tmp = one_info['url']
            title = one_info['title']
            print('url:'+_url_tmp+'  '+'title:'+title)
            return_data.append({'url': _url_tmp, 'title': title, 'datetime': _datetime})
    return return_data


def standard_work_article(target_url):
    return_data = []
    headers = {'User-Agent': UserAgent}
    res = requests.get(target_url, headers=headers)
    res.raise_for_status()
    reg_content = res.content.decode('utf-8','ignore')
    html_page = bs4.BeautifulSoup(reg_content, 'lxml')
    try:
        infos = html_page.find(class_='article').findAll('p')
    except:
        infos = html_page.find(id='artibody').findAll('p')
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
    try:
        nian = urllib.parse.unquote_plus('%E5%B9%B4')
        yue = urllib.parse.unquote_plus('%E6%9C%88')
        ri = urllib.parse.unquote_plus('%E6%97%A5')
        date = re.search(r"(\d{4}%s\d{1,2}%s\d{1,2}%s\s\d{1,2}:\d{1,2})"%(nian,yue,ri) ,reg_content )
        datetime_dir = re.match('(?P<year>\d{4})%s(?P<month>\d+?)%s(?P<day>\d+?)%s (?P<hour>\d+?):(?P<minute>\d+)'%(nian,yue,ri),date[0])
    except:
        tim = re.search(r"(\d{4}-\d{1,2}-\d{1,2}\s\d{1,2}:\d{1,2})", reg_content)
        datetime_dir = re.match('(?P<year>\d{4})-(?P<month>\d+?)-(?P<day>\d+?) (?P<hour>\d+?):(?P<minute>\d+)',
                                tim[0])
    tt_tmp = '%s-%s-%s %s:%s' % (
        datetime_dir['year'], datetime_dir['month'], datetime_dir['day'], datetime_dir['hour'], datetime_dir['minute'])
    _datetime = 0
    if datetime_dir:
        _datetime = get_time('%Y-%m-%d %H:%M', tt_tmp)
    _title = html_page.find('title').text
    _title = _title.replace('\n','').strip()
    return _datetime, '%s<replace title>%s' % (_title, '\n'.join(return_data))

def main():
   list = standard_work_list()
   print(len(list))
   for url in list:
       print(standard_work_article(url['url']))
if __name__ == '__main__':
    main()
