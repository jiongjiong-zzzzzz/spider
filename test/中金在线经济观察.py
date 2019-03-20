#encoding=utf8
import requests
import json
import chardet
import requests,re,bs4,time,sys,hashlib,uuid,time,json,base64,rsa,platform,datetime,os,urllib
import re

UserAgent = 'Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3497.92 Safari/537.36'
def get_time(format_date,time_pull = None):
    if time_pull:
        return int(time.mktime(time.strptime(time_pull, format_date))*1000)
def standard_work_list():
    return_data = []
    headers = {'User-Agent': UserAgent}
    url='http://app.cnfol.com/test/newlist_api.php?catid=1702&page=1&callback=callback&_=1547445739777'
    res = requests.get(url,headers=headers)
    res.raise_for_status()
    reg_content = res.content.decode('utf-8')
    html_json = re.findall(r'callback[(](.+?)[)]',reg_content,re.S)
    data = json.loads(html_json[0])
    for one_info in data:
        _datetime = 0
        _url_tmp=one_info['Url']
        title=one_info['Title']
        return_data.append({'url': _url_tmp, 'title':title , 'datetime': _datetime})
    return return_data

def standard_work_article(target_url):
    return_data = []
    headers = {'User-Agent': UserAgent}
    res = requests.get(target_url, headers=headers)
    try:
        res.raise_for_status()
    except:

        return
    try:
        reg_content = res.content.decode('utf8')
    except:
        reg_content = res.content.decode('utf8', 'ignore')
    html_page = bs4.BeautifulSoup(reg_content, 'lxml')
    need_content = html_page.find(class_='ArtM').text
    return_data.append(need_content.strip())
    tim = re.search(r"(\d{4}-\d{1,2}-\d{1,2}\s\d{1,2}:\d{1,2})", reg_content)
    datetime_dir = re.match('(?P<year>\d{4})-(?P<month>\d+?)-(?P<day>\d+?) (?P<hour>\d+?):(?P<minute>\d+?)',tim[0] )
    tt_tmp = '%s-%s-%s %s:%s' % (
        datetime_dir['year'], datetime_dir['month'], datetime_dir['day'], datetime_dir['hour'], datetime_dir['minute'])
    _datetime = 0
    if datetime_dir:
        _datetime = get_time('%Y-%m-%d %H:%M', tt_tmp)
    _title = html_page.find(id='Title').text
    return _datetime, '%s<replace title>%s' % (_title, '\n'.join(return_data))
def main():
   list = standard_work_list()
   for url in list:
        print(standard_work_article(url['url']))


if __name__ == '__main__':
    main()
