from selenium import webdriver
from selenium.webdriver.common.action_chains import ActionChains
from time import sleep
import urllib
import cv2
import numpy as np

#这个函数是用来显示图片的。
def show(name):
    cv2.imshow('Show',name)
    cv2.waitKey(0)
    cv2.destroyAllWindows()
#这里我用的google的驱动。
driver = webdriver.Chrome()
driver.maximize_window()
url='https://open.captcha.qq.com/online.html'

#实现登录
def get_login(driver,url):
    driver.get(url)
    # elem=driver.find_element_by_xpath('//*[@id="username"]')
    # elem.send_keys('aaa5726615')
    # elem=driver.find_element_by_xpath('//*[@id="password"]')
    # elem.send_keys('aaa5726615')
    # elem=driver.find_element_by_xpath('//*[@id="login"]')
    # elem.click()
    driver.find_element_by_xpath("//div[@class='wp-onb-tit']/a[text()='可疑用户']").click()
    sleep(1)
    driver.find_element_by_id('code').click()
    sleep(5)
    driver.switch_to_frame(1)
    return driver

driver=get_login(driver,url)

#获取验证码中的图片
def get_image(driver):

    image1 = driver.find_element_by_id('slideBg').get_attribute('src')
    image2 = driver.find_element_by_id('slideBlock').get_attribute('src')
    req=urllib.request.Request(image1)
    bkg=open('slide_bkg.png','wb+')
    bkg.write(urllib.request.urlopen(req).read())
    bkg.close()
    req = urllib.request.Request(image2)
    blk = open('slide_block.png', 'wb+')
    blk.write(urllib.request.urlopen(req).read())
    blk.close()
    return 'slide_bkg.png','slide_block.png'

bkg,blk=get_image(driver)

#计算缺口的位置，由于缺口位置查找偶尔会出现找不准的现象，这里进行判断，如果查找的缺口位置x坐标小于450，我们进行刷新验证码操作，重新计算缺口位置，知道满足条件位置。（设置为450的原因是因为缺口出现位置的x坐标都大于450）
def get_distance(bkg,blk):
    block = cv2.imread(blk, 0)
    template = cv2.imread(bkg, 0)
    cv2.imwrite('template.jpg', template)
    cv2.imwrite('block.jpg', block)
    block = cv2.imread('block.jpg')
    block = cv2.cvtColor(block, cv2.COLOR_BGR2GRAY)
    block = abs(255 - block)
    cv2.imwrite('block.jpg', block)
    block = cv2.imread('block.jpg')
    template = cv2.imread('template.jpg')
    result = cv2.matchTemplate(block,template,cv2.TM_CCOEFF_NORMED)
    x, y = np.unravel_index(result.argmax(),result.shape)
    print(x, y)
    #这里就是下图中的绿色框框
    cv2.rectangle(template, (y+20, x+20), (y + 136-25, x + 136-25), (7, 249, 151), 2)
    #之所以加20的原因是滑块的四周存在白色填充
    print('x坐标为：%d'%(y+20))
    if y+20<450:
        elem=driver.find_element_by_xpath('//*[@id="reload"]/div')
        sleep(1)
        elem.click()
        bkg,blk=get_image(driver)
        y,template=get_distance(bkg,blk)
    return y,template

distance,template=get_distance(bkg,blk)

#这个是用来模拟人为拖动滑块行为，快到缺口位置时，减缓拖动的速度，服务器就是根据这个来判断是否是人为登录的。
def get_tracks(dis):
    v=0
    t=0.3
    #保存0.3内的位移
    tracks=[]
    current=0
    mid=distance*4/5
    while current<=dis:
        if current<mid:
            a=2
        else:
            a=-3
        v0=v
        s=v0*t+0.5*a*(t**2)
        current+=s
        tracks.append(round(s))
        v=v0+a*t
    return tracks
#原图的像素是680*390，而网页的是340*195，图像缩小了一倍。
#经过尝试，发现滑块的固定x坐标为70，这个地方有待加强，这里加20的原因上面已经解释了。
double_distance=int((distance-70+20)/2)
tracks=get_tracks(double_distance)
#由于计算机计算的误差，导致模拟人类行为时，会出现分布移动总和大于真实距离，这里就把这个差添加到tracks中，也就是最后进行一步左移。
tracks.append(-(sum(tracks)-double_distance))

element = driver.find_element_by_id('tcaptcha_drag_thumb')
ActionChains(driver).click_and_hold(on_element=element).perform()
for track in tracks:
    ActionChains(driver).move_by_offset(xoffset=track, yoffset=0).perform()
sleep(0.5)
ActionChains(driver).release(on_element=element).perform()
show(template)
