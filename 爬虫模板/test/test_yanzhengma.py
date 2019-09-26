from selenium import webdriver
import time
import requests
from selenium.webdriver.common.by import By
from selenium.webdriver.common.action_chains import ActionChains
browser = webdriver.Chrome()
browser.get("http://glidedsky.com/level/web/crawler-captcha-1?page=1&ticket=t02suT0XSdEKR3xXXODfg_GgM88XQvd1VXhSp3yd8K778_1rxIOCvIE5s4usj6Gg7ZXNGgY4FrSLmD1-IZmvhQi3mvxlMykQo2_ZLGLukTSVVT7OrDyADnP-A**&rand_str=@YYd")
time.sleep(1)
browser.find_element_by_id("email").send_keys("315909056@qq.com")
browser.find_element_by_id("password").send_keys("5726615")
time.sleep(0.2)
browser.find_element_by_class_name("btn,btn-primary").click()
time.sleep(5)
browser.switch_to_frame('tcaptcha_iframe')

img_big = browser.find_element_by_id('slideBg').get_attribute('src')
image_result = requests.get(img_big).content
with open('big.jpg','wb') as f:
    f.write(image_result)

img_big = browser.find_element_by_id('slideBlock').get_attribute('src')
image_result = requests.get(img_big).content
with open('small.jpg','wb') as f:
    f.write(image_result)
slid_ing = browser.find_element_by_xpath('//div[@id="tcaptcha_drag_thumb"]')
import cv2
import numpy as np

otemp = 'small.jpg'
oblk = 'big.jpg'
target = cv2.imread(otemp, 0)
template = cv2.imread(oblk, 0)
w, h = target.shape[::-1]
temp = 'temp1.jpg'
targ = 'targ1.jpg'
cv2.imwrite(temp, template)
cv2.imwrite(targ, target)
target = cv2.imread(targ)
target = cv2.cvtColor(target, cv2.COLOR_BGR2GRAY)
target = abs(255 - target)
cv2.imwrite(targ, target)
target = cv2.imread(targ)
template = cv2.imread(temp)
result = cv2.matchTemplate(target, template, cv2.TM_CCOEFF_NORMED)
x, y = np.unravel_index(result.argmax(), result.shape)
#缺口位置
print(y)
distance = y/2 - 27.5
print(distance)


def get_tracks(distance):
    '''
    拿到移动轨迹，模仿人的滑动行为，先匀加速后匀减速
    匀变速运动基本公式：
    ①v=v0+at
    ②s=v0t+½at²
    ③v²-v0²=2as
    :param distance: 需要移动的距离
    :return: 存放每0.3秒移动的距离
    '''
    # 初速度
    v = 0
    # 单位时间为0.2s来统计轨迹，轨迹即0.2内的位移
    t = 0.3
    # 位移/轨迹列表，列表内的一个元素代表0.2s的位移
    tracks = []
    # 当前的位移
    current = 0
    # 到达mid值开始减速
    mid = distance * 4 / 5

    while current < distance:
        if current < mid:
            # 加速度越小，单位时间的位移越小,模拟的轨迹就越多越详细
            a = 2
        else:
            a = -3

        # 初速度
        v0 = v
        # 0.2秒时间内的位移
        s = v0 * t + 0.5 * a * (t ** 2)
        # 当前的位置
        current += s
        # 添加到轨迹列表
        tracks.append(round(s))

        # 速度已经达到v,该速度作为下次的初速度
        v = v0 + a * t
    return tracks
tracks=get_tracks(distance)
ActionChains(browser).click_and_hold(on_element=slid_ing).perform()  # 点击鼠标左键，按住不放
time.sleep(0.2)
# print('第二步,拖动元素')
for track in tracks:
        ActionChains(browser).move_by_offset(xoffset=track,yoffset=0).perform()
    # else:
    #     ActionChains(browser).move_by_offset(xoffset=3,yoffset=0).perform() #先移过一点
    #     ActionChains(browser).move_by_offset(xoffset=-3,yoffset=0).perform() #再退回来，是不是更像人了

# ActionChains(driver).move_by_offset(xoffset=-random.randint(0, 1), yoffset=0).perform()   # 微调，根据实际情况微调
time.sleep(1)
# print('第三步,释放鼠标')
ActionChains(browser).release(on_element=slid_ing).perform()


print('成功')
