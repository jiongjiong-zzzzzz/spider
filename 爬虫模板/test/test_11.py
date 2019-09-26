from selenium import webdriver
import time
from PIL import Image
import requests
from selenium.webdriver.common.by import By
from selenium.webdriver.common.action_chains import ActionChains
browser = webdriver.Chrome()
browser.maximize_window()
browser.get("http://glidedsky.com/level/web/crawler-sprite-image-1?page=2")
time.sleep(1)
browser.find_element_by_id("email").send_keys("315909056@qq.com")
browser.find_element_by_id("password").send_keys("5726615")
time.sleep(0.2)
browser.find_element_by_class_name("btn,btn-primary").click()
time.sleep(1)
browser.save_screenshot("111.png")

baidu = browser.find_element_by_class_name('row')
print(baidu)
left = baidu.location['x']
top = baidu.location['y']
elementWidth = baidu.location['x'] + baidu.size['width']
elementHeight = baidu.location['y'] + baidu.size['height']
picture = Image.open(r'111.png')
picture = picture.crop((left, top, elementWidth, elementHeight))
picture.save(r'222.png')
browser.quit()




