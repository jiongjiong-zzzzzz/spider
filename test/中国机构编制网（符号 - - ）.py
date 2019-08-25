 #encoding=utf-8
import requests,re,bs4,time,sys,hashlib,uuid,time,json,base64,rsa,platform,datetime,os,urllib

UserAgent = 'Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3497.92 Safari/537.36'
def get_time(format_date,time_pull = None):
    if time_pull:
        return int(time.mktime(time.strptime(time_pull, format_date))*1000)
def standard_work_list():
    return_data = []
    headers = {'User-Agent': UserAgent,
               'cookie': 'FSSBBIl1UgzbN7N80S=sZNJvmAwarkWZC8SxVXinpTzPm2WV_G.KaC_qHA6WqSVHiqCCEPxHi8KdK9.22oq; _gscu_277575923=52272480qz89cl62; _trs_uv=jt3qyf7j_970_32k4; gwdshare_firstime=1552454499738; _gscu_205129936=52454507s46p1g17; _gscbrs_277575923=1; _trs_ua_s_1=jw02vlka_970_l0fv; _gscs_277575923=585809853gyiq219|pv:3; FSSBBIl1UgzbN7N80T=3WYw4sIdtttAZUC3fLECT0UdlqrHiS6GIEmQSgPduCDZvJ6iD8U1AWK8LeXo_cJuYyJVdrgbPbnuvYAs8jETNpaw6lQCyPXUzuyBgl0BlxyDJjuJWfVGNXO9Tif3a4HqhTuZGyVqtAdS02vcxEiVZcjAVTJoVVnsJYLyH_sYC0jj8OpPyAV5hfgbp.0vl3H_0oG7XCNlw3v60ytP_rp0CExvHSiB3gc26YDW_dGJW.OUeWEaWyLR0eOl1tbGiEbnEGnNYAs_8oDpC_507nWAGw5XAMP8MzfDOEp9le8MJ79BTqLR3Pe8YxNviTGICFCcMoniFyCnPIFwMfFJEcvhvIPRp'}

    res = requests.get('http://app.scopsr.gov.cn/xwzx/ttxw/', headers=headers)
    res.raise_for_status()
    reg_content = res.content.decode('UTF-8')
    html_page = bs4.BeautifulSoup(reg_content, 'lxml')
    infos = html_page.findAll('a',{'class':'hui14'})
    for one_info in infos:
        _one_info = str(one_info)
        if 'target' not in _one_info:
            continue
        content_dir = one_info
        if content_dir:
            _datetime = 0
            _url_tmp = content_dir['href'].replace('./','http://app.scopsr.gov.cn/xwzx/ttxw/')
            print({'url': _url_tmp, 'title': content_dir.text.strip()})
            return_data.append({'url': _url_tmp, 'title': content_dir.text, 'datetime': _datetime})
    return return_data


def standard_work_article(target_url):
    return_data = []
    headers = {'User-Agent': UserAgent}
    res = requests.get(target_url, headers=headers)
    res.raise_for_status()
    reg_content = res.content.decode('UTF-8')
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
