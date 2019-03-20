#encoding=utf-8
import requests,re,bs4,time,sys,hashlib,uuid,time,json,base64,rsa,platform,datetime,os,urllib

UserAgent = 'Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.139 Safari/537.36'
def get_time(format_date,time_pull = None):
    if time_pull:
        return int(time.mktime(time.strptime(time_pull, format_date))*1000)
def standard_work_list():
    return_data = []
    headers = {'User-Agent': UserAgent,
               'Cookie':'maxPageNum831221=73; yfx_c_g_u_id_10003701=_ck19031111203212071883109350337; yfx_f_l_v_t_10003701=f_t_1552274432194__r_t_1552274432194__v_t_1552274432194__r_c_0; yfx_mr_10003701=%3A%3Amarket_type_free_search%3A%3A%3A%3Abaidu%3A%3A%3A%3A%3A%3A%3A%3Awww.baidu.com%3A%3A%3A%3Apmf_from_free_search; yfx_mr_f_10003701=%3A%3Amarket_type_free_search%3A%3A%3A%3Abaidu%3A%3A%3A%3A%3A%3A%3A%3Awww.baidu.com%3A%3A%3A%3Apmf_from_free_search; yfx_key_10003701=; _Jo0OQK=7B1669865ED266DA36E62DC31FD23E54EB604EF5C4AEF7936A3EC3BB9920AD26F8D678CF2870A791FD50A073802AAA6CCAB72A67BCDA1A4A2FBB6233BDDC409366334275DAD340EB4DDFFF13AA80B4DD4EFFFF13AA80B4DD4EF210F3BD32E043658FE26683A79EA3885GJ1Z1Mw==',
               'Referer':'http://www.chinatax.gov.cn/n810219/n810724/index.html',
               'Host':'www.chinatax.gov.cn'}
    res = requests.get('http://www.chinatax.gov.cn/n810219/n810724/index.html', headers=headers)
    res.raise_for_status()
    reg_content = res.content.decode('utf-8')
    html_page = bs4.BeautifulSoup(reg_content, 'lxml')
    infos = html_page.select('span#comp_831221 dl dd a')
    for one_info in infos:
        _one_info = str(one_info)
        content_dir = one_info
        if content_dir:
            _datetime = 0
            _url_tmp = content_dir['href'].strip()
            _url_tmp=_url_tmp.replace('../../','http://www.chinatax.gov.cn/')
            print({'url': _url_tmp, 'title': content_dir.text.strip()})
            return_data.append({'url': _url_tmp, 'title': content_dir.text.strip(), 'datetime': _datetime})
    return return_data


def standard_work_article(target_url):
    return_data = []
    headers = {'User-Agent': UserAgent}
    res = requests.get(target_url, headers=headers)
    time.sleep(1)
    res.raise_for_status()
    reg_content = res.content.decode('utf-8')
    html_page = bs4.BeautifulSoup(reg_content, 'lxml')
    infos = html_page.find(class_='sv_texth3').findAll('p')
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

    date = re.search(r"(\d{4}%s\d{1,2}%s\d{1,2}%s)"%(nian, yue, ri), reg_content)

    datetime_dir = re.match('(?P<year>\d{4})%s(?P<month>\d+?)%s(?P<day>\d+?)%s' % (nian, yue, ri), date[0])

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
