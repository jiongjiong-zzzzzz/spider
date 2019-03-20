import pymysql
from itertools import islice
import operator
'''
取出csv文件的数据，放入列表，
该方法最终返回一个包含所有数据的列表
'''
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
#连接数据库
db = pymysql.connect("localhost", "root", "123", "stock")
# 使用 cursor() 方法创建一个游标对象 cursor
cursor = db.cursor()
# 使用预处理语句创建表
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
            #sql语句 在stock数据库中创建以股票代码为名的数据库表
            sql = """CREATE TABLE %s (
          `TradingDate` varchar(255) DEFAULT NULL,
          `StockCode` varchar(255) DEFAULT NULL,
          `OpenPrice` decimal(10,2) DEFAULT NULL,
          `HighPrice` decimal(10,2) DEFAULT NULL,
          `LowPrice` decimal(10,2) DEFAULT NULL,
          `ClosePrice` decimal(10,2) DEFAULT NULL,
          `TradeVolume` varchar(255) DEFAULT NULL,
          `TradeAmount` varchar(255) DEFAULT NULL,
          `AdjustFactor` varchar(255) DEFAULT NULL,
          `SuspendState` varchar(5) DEFAULT NULL,
          `IsSpecialTreat` varchar(5) DEFAULT NULL
        ) ENGINE=InnoDB DEFAULT CHARSET=utf8;"""%('_'+stockcode)
            cursor.execute(sql)
            print('_'+stockcode+'表创建成功')
            #将不重复的股票代码放入stockcodes列表
            stockcodes.append(stockcode)


    '''
    infos 包含 csv文件所有数据，循环遍历infos，
    将数据存入stock中已经建好的数据库表中，
    第一题完成
    '''
    for info in infos:
            date = str(info['TradingDate'])
            code = str(info['StockCode'])
            stockcode = info['StockCode']
            sql = """INSERT INTO %s
                        VALUES (%s, %s, %s, %s, %s,%s, %s, %s, %s, %s, %s)"""\
                    %('_'+stockcode,"'"+date+"'","'"+code+"'",info['OpenPrice'] ,info['HighPrice'],info['LowPrice'],
                    info['ClosePrice'], info['TradeVolume'], info['TradeAmount'], info['AdjustFactor'],
                    info['SuspendState'], info['IsSpecialTreat'])
            try:
                # 执行sql语句
                cursor.execute(sql)
                # 提交到数据库执行
                db.commit()
                print('_' + stockcode + '插入数据成功')
            except:
                # Rollback in case there is any error
                db.rollback()


    '''
    第二题，stockcodes列表存放不重复的股票代码，遍历stockcodes
    得到各个stockcode(股票代码)，作为SELECT语句中的表名，取出日期为
    2018-01-02 和 2018-01-10 中的收盘价，得到改支股票的收益率，将
    股票代码，收益率以字典的形式存入Income列表，用sorted函数按收益率
    进行排序，得到前10%收益率的股票代码
    '''
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
    '''
    第三题，
    pass
    '''




def main():
    creat_table()

    # 关闭数据库连接
    db.close()

if __name__ == "__main__":
    main()


