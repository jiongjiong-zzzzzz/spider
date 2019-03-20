import re
import datetime
import urllib

def time_fix(reg_content):
    nian = urllib.parse.unquote_plus('%E5%B9%B4')
    yue = urllib.parse.unquote_plus('%E6%9C%88')
    ri = urllib.parse.unquote_plus('%E6%97%A5')
    date = re.search(r"(\d{4}%s\d{1,2}%s\d{1,2}%s\s\d{1,2}:\d{1,2})" % (nian, yue, ri), reg_content)
    if not date:
        date = re.search(r"(\d{4}%s\d{1,2}%s\d{1,2}%s)" % (nian, yue, ri), reg_content)
        if not date:
            date = re.search(r"(\d{4}-\d{1,2}-\d{1,2}\s\d{1,2}:\d{1,2})", reg_content)
            if not date:
                date = re.search(r"(\d{4}/\d{1,2}/\d{1,2}\s\d{1,2}:\d{1,2})", reg_content)
                if not date:
                    date = re.search(r"(\d{4}-\d{1,2}-\d{1,2})", reg_content)
                    if not date:
                        date = re.search(r"(\d{4}/\d{1,2}/\d{1,2})", reg_content)
    return date[0]
