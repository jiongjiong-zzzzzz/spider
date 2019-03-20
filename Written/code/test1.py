
import csv
#csv读取
csv_file = csv.reader(open('201801.csv','r'))
return_data = []
for items in csv_file:
    item = items.split(',')
    for info in item:
        stock={
            'TradingDate' :item[0],
            'StockCode' : item[1],
             'OpenPrice':item[2],
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









