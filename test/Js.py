import requests
import re
import jsbeautifier
import js2py

host_url = 'http://www.pbc.gov.cn/'
dest_url = 'http://www.pbc.gov.cn/tiaofasi/144941/144957/index.html'
# 利用session保存cookie信息，第一次请求会设置cookie类似{'wzwsconfirm': 'ab3039756ba3ee041f7e68f634d28882', 'wzwsvtime': '1488938461'}，与js解析得到的cookie合起来才能通过验证
r = requests.session()
content = r.get(dest_url).content
# 获取页面脚本内容
re_script = re.search(r'<script type="text/javascript">(?P<script>.*)</script>', content.decode('utf-8'),
                      flags=re.DOTALL)
# 用点匹配所有字符，用(?P<name>...)获取：https://docs.python.org/3/howto/regex.html#regex-howto
# cheatsheet：https://github.com/tartley/python-regex-cheatsheet/blob/master/cheatsheet.rst
script = re_script.group('script')
script = script.replace('\r\n', '')
# 在美化之前，去掉\r\n之类的字符才有更好的效果
res = jsbeautifier.beautify(script)
# 美化并一定程度解析js代码：https://github.com/beautify-web/js-beautify
with open('x.js', 'w') as f:
    f.write(res)
# 写入文档进行查看分析

jscode_list = res.split('function')
var_ = jscode_list[0]
var_list = var_.split('\n')
template_js = var_list[3]  # 依顺序获取，亦可用正则
template_py = js2py.eval_js(template_js)
# 将所有全局变量插入第一个函数变为局部变量并计算
function1_js = 'function' + jscode_list[1]
position = function1_js.index('{') + 1
function1_js = function1_js[:position] + var_ + function1_js[position:]
function1_py = js2py.eval_js(function1_js)
cookie1 = function1_py(str(template_py))  # 结果类似'NA=='
# 保存得到的第一个cookie
cookies = {}
cookies['wzwstemplate'] = cookie1
# 对第三个函数做类似操作
function3_js = 'function' + jscode_list[3]
position = function3_js.index('{') + 1
function3_js = function3_js[:position] + var_ + function3_js[position:]
function3_py = js2py.eval_js(function3_js)
middle_var = function3_py()  # 是一个str变量，结果类似'WZWS_CONFIRM_PREFIX_LABEL4132209'
cookie2 = function1_py(middle_var)
cookies['wzwschallenge'] = cookie2
# 关于js代码中的document.cookie参见 https://developer.mozilla.org/zh-CN/docs/Web/API/Document/cookie
dynamicurl = js2py.eval_js(var_list[0])

# 利用新的cookie对提供的动态网址进行访问即是我们要达到的内容页面了
r.cookies.update(cookies)
content = r.get(host_url + dynamicurl).content

# 最后验证是否爬取到有效信息
if u'银行卡清算机构管理办法' in content.decode('utf-8'):
    print('success')
