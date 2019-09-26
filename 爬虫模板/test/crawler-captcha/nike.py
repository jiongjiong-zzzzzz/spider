import time
import captcha_tools
import requests
from lxml import etree
from selenium import webdriver
from selenium.webdriver.common.action_chains import ActionChains
browser = webdriver.Chrome()
browser.get("https://www.adidas.com.cn/member/login?locale=zh_CN")
time.sleep(1)
browser.find_element_by_id("loginMobile").send_keys("18888888888")
browser.find_element_by_id("smsCode").click()
#browser.find_element_by_id("password").send_keys("5726615")
time.sleep(0.2)




img_big = browser.find_element_by_class_name('yidun_bg-img').get_attribute('src')
image_result = requests.get(img_big).content
with open('big.jpg','wb') as f:
    f.write(image_result)

img_big = browser.find_element_by_class_name('yidun_jigsaw').get_attribute('src')
image_result = requests.get(img_big).content
with open('small.jpg','wb') as f:
    f.write(image_result)

slid_ing = browser.find_element_by_class_name("yidun_slider")
y = captcha_tools.get_distance('small.jpg','big.jpg')

distance = y/2 - 27.5
print(distance)

tracks=captcha_tools.get_tracks(distance)
captcha_tools.move(browser,tracks,slid_ing)

time.sleep(5)


