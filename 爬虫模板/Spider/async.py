import time
import asyncio


async def hi(msg,sec):
    await asyncio.sleep(sec)
    print('{} @{}'.format(msg,time.strftime('%H:%M:%S')))
    return sec
async def main():
    print('begin at {}'.format(time.strftime('%H:%M:%S')))
    tasks=[]
    for i in range(1,5):
        t = asyncio.create_task(hi(i,i))
        tasks.append(t)
    for t in tasks:
        r = await t
        print("r:",r)
    print('end at {}'.format(time.strftime('%H:%M:%S')))

asyncio.run(main())

