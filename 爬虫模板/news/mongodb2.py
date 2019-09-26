import  pymongo
client = pymongo.MongoClient('localhost', 27017)
db = client['glidedsky']
collection = db['xuebi2']
def insert(item):
    try:
        collection.insert_one(item)
    except:
        pass
