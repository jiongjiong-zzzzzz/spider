from Spider.ezpymysql import Connection


db = Connection(
    'localhost',
    'weixin',
    'root',
    '123'
)

# 获取多天记录
sql = 'select distinct 特色产业 from sheet2'
data = db.query(sql)
print(data)
list_1=[]
for info in data:
    list_1.append(info['特色产业'])
print(list_1)
list_2 =['1类县','2类县','3类县']
dict_={}
for item in list_1:
    dict_ = {}
    for info_ in list_2:
        sql = "select count(县类别) from sheet2 where 特色产业='%s' and 县类别='%s'"%(item,info_)
        data = db.query(sql)
        num = data[0]['count(县类别)']
        try:
            dict_[info_] = num
        except:
            pass
    hushu = dict_['1类县']
    renshu = dict_['2类县']
    avg = dict_['3类县']
    sql = 'insert into one values(%s, %s, %s, %s)'
    last_id = db.execute(sql, item, hushu,renshu,avg)
    print(dict_)

for item in list_1:
    sql = "select sum(a),sum(b),sum(c) from sheet2 where 特色产业='%s'"%item
    data = db.query(sql)
    hushu = data[0]['sum(a)']
    renshu = data[0]['sum(b)']
    avg = data[0]['sum(c)']
    sql = 'insert into two values(%s, %s, %s, %s)'
    last_id = db.execute(sql, item, hushu,renshu,avg)
    print(data)

for item in list_1:
    sql = "select sum(参与新型经营主体数量)  from sheet2 where 特色产业='{}'" .format(item)
    data = db.query(sql)
    hushu = data[0]['sum(参与新型经营主体数量)']
    sql = 'insert into three values(%s, %s)'
    last_id = db.execute(sql, item, hushu)

for item in list_1:
    sql = "select sum(覆盖贫困村),sum(有产业贫困户数),avg(贫困户产业覆盖率) from sheet2 where 特色产业='{}'" .format(item)
    data = db.query(sql)
    hushu = data[0]['sum(覆盖贫困村)']
    renshu = data[0]['sum(有产业贫困户数)']
    avg = data[0]['avg(贫困户产业覆盖率)']
    sql = 'insert into four values(%s, %s, %s, %s)'
    last_id = db.execute(sql, item, hushu,renshu,avg)
    print(data)





for item in list_1:
    sql = "select sum(其中带动贫困户户数),sum(其中带动贫困户人数) from sheet2 where 特色产业='{}'" .format(item)
    data = db.query(sql)
    hushu = data[0]['sum(其中带动贫困户户数)']
    renshu = data[0]['sum(其中带动贫困户人数)']
    sql = 'insert into five values(%s, %s, %s)'
    last_id = db.execute(sql, item, hushu,renshu)











