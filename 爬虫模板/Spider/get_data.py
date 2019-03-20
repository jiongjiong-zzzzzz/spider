import pymysql
import csv
import operator
from itertools import islice
db = pymysql.connect("localhost", "root", "123", "stock")
# 使用 cursor() 方法创建一个游标对象 cursor
cursor = db.cursor()
sql = "SELECT ClosePrice FROM %s WHERE TradingDate='2018-01-02' or TradingDate='2018-01-10'" % ('_000001')

cursor.execute(sql)
# 获取所有记录列表
def get_data():
    return_data = []
    with open('201801.csv', 'r') as f:
        for line in islice(f, 1, None):
            item = line.replace('\n','').split(',')
            stock = {
                'TradingDate': item[0],
                'StockCode': item[1],
                'OpenPrice': item[2],
                'HighPrice': item[3],
                'LowPrice': item[4],
                'ClosePrice': item[5],
                'TradeVolume': item[6],
                'TradeAmount': item[7],
                'AdjustFactor': item[8],
                'SuspendState': item[9],
                'IsSpecialTreat': item[10]
            }
            return_data.append(stock)
    f.close()
    return return_data
def creat_table():
    # infos列表 存放csv文件的所有股票数据
    infos = get_data()
    print(len(infos))
    #stockcodes列表存放所有的不重复的股票代码
    stockcodes=[]

    for info in infos:
        # 单支股票代码
        stockcode = info['StockCode']
        if stockcode not in stockcodes:
            stockcode = str(stockcode)
            stockcodes.append(stockcode)

    Income = []
    for stockcode in stockcodes:

        stockcode=str(stockcode)
        count = 0
        # SQL 查询语句
        sql = "SELECT ClosePrice FROM %s WHERE TradingDate='2018-01-02' or TradingDate='2018-01-10'" % ('_'+stockcode)
        try:
            # 执行SQL语句
            cursor.execute(sql)
            # 获取所有记录列表
            results = cursor.fetchall()
            for row in results:
                if count == 0:
                    date_2 = float(row[0])
                elif count == 1:
                    date_10 = float(row[0])
                count+=1
        except:
            print
            "Error: unable to fecth data"
        #计算收益 (date_10 - date_2) / date_2
        income = (date_10 - date_2) / date_2
        Income.append({'stockcode':stockcode,'income':income})
    #包含各支股票收益率的列表按收益率降序排序
    sorted_x = sorted(Income, key=operator.itemgetter('income'), reverse=True)
    #在所有的股票中，10%股票数量的计算
    num = int(len(stockcodes)) * 0.1
    count = 1
    #循环遍历，输出前10%收益率的股票代码
    for inCome in sorted_x:
        code=str(inCome['stockcode'])
        comein=str(inCome['income'])
        print('排名：'+str(count)+"," +'股票代码:' + code + "," + '股票收益率：' + comein)
        count+=1
        if count>num:
            break
    print(count)

def main():
    creat_table()

    # 关闭数据库连接
    db.close()

if __name__ == "__main__":
    main()
