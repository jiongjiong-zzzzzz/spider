#!/usr/bin/env python
# encoding: utf-8

# -*- coding: utf-8 -*-
# @contact: ybsdeyx@foxmail.com
# @software: PyCharm
# @time: 2019/4/25 16:39
# @author: Paulson●Wier
# @file: captcha_qq.py
# @desc:
import numpy as np
import random
import requests
from selenium.webdriver import ActionChains
import time
from selenium import webdriver
from PIL import Image
import os
from selenium.webdriver.support.ui import WebDriverWait
import cv2


class Login(object):
    """
    腾讯防水墙滑动验证码破解
    使用OpenCV库
    成功率大概90%左右：在实际应用中，登录后可判断当前页面是否有登录成功才会出现的信息：比如用户名等。循环
    https://open.captcha.qq.com/online.html
    破解 腾讯滑动验证码
    腾讯防水墙
    python + seleniuum + cv2
    """
    def __init__(self):
        # 如果是实际应用中，可在此处账号和密码
        self.url = "https://open.captcha.qq.com/online.html"
        self.driver = webdriver.Chrome()

    @staticmethod
    def show(name):
        cv2.imshow('Show', name)
        cv2.waitKey(0)
        cv2.destroyAllWindows()

    @staticmethod
    def webdriverwait_send_keys(dri, element, value):
        """
        显示等待输入
        :param dri: driver
        :param element:
        :param value:
        :return:
        """
        WebDriverWait(dri, 10, 5).until(lambda dr: element).send_keys(value)

    @staticmethod
    def webdriverwait_click(dri, element):
        """
        显示等待 click
        :param dri: driver
        :param element:
        :return:
        """
        WebDriverWait(dri, 10, 5).until(lambda dr: element).click()

    @staticmethod
    def get_postion(blk, bkg):
        """
        判断缺口位置
        :param chunk: 缺口图片是原图
        :param canves:
        :return: 位置 x, y
        """
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
        result = cv2.matchTemplate(block, template, cv2.TM_CCOEFF_NORMED)
        x, y = np.unravel_index(result.argmax(), result.shape)
        print(x,y)
        # 这里就是下图中的绿色框框
        cv2.rectangle(template, (y + 20, x + 20), (y + 136 - 25, x + 136 - 25), (7, 249, 151), 2)
        # 之所以加20的原因是滑块的四周存在白色填充
        print('x坐标为：%d' % (y + 20))
        return y, block


    @staticmethod
    def get_track(distance,dis):
        v = 0
        t = 0.3
        # 保存0.3内的位移
        tracks = []
        current = 0

        mid = distance * 4 / 5
        while current <= dis:
            if current < mid:
                a = 2
            else:
                a = -3
            v0 = v
            s = v0 * t + 0.5 * a * (t ** 2)
            current += s
            tracks.append(round(s))
            v = v0 + a * t
        return tracks

    @staticmethod
    def urllib_download(imgurl, imgsavepath):
        """
        下载图片
        :param imgurl: 图片url
        :param imgsavepath: 存放地址
        :return:
        """
        from urllib.request import urlretrieve
        urlretrieve(imgurl, imgsavepath)

    def after_quit(self):
        """
        关闭浏览器
        :return:
        """
        self.driver.quit()

    def login_main(self):
        # ssl._create_default_https_context = ssl._create_unverified_context
        driver = self.driver
        driver.maximize_window()
        driver.get(self.url)

        click_keyi_username = driver.find_element_by_xpath("//div[@class='wp-onb-tit']/a[text()='可疑用户']")
        self.webdriverwait_click(driver, click_keyi_username)

        login_button = driver.find_element_by_id('code')
        self.webdriverwait_click(driver, login_button)
        time.sleep(1)

        driver.switch_to.frame(driver.find_element_by_id('tcaptcha_iframe'))  # switch 到 滑块frame
        time.sleep(0.5)
        bk_block = driver.find_element_by_xpath('//img[@id="slideBg"]')  # 大图
        web_image_width = bk_block.size
        web_image_width = web_image_width['width']
        bk_block_x = bk_block.location['x']

        slide_block = driver.find_element_by_xpath('//img[@id="slideBlock"]')  # 小滑块
        slide_block_x = slide_block.location['x']

        bk_block = driver.find_element_by_xpath('//img[@id="slideBg"]').get_attribute('src')       # 大图 url
        slide_block = driver.find_element_by_xpath('//img[@id="slideBlock"]').get_attribute('src')  # 小滑块 图片url
        slid_ing = driver.find_element_by_xpath('//div[@id="tcaptcha_drag_thumb"]')  # 滑块

        os.makedirs('./image/', exist_ok=True)
        self.urllib_download(bk_block, './image/bkBlock.png')
        self.urllib_download(slide_block, './image/slideBlock.png')
        time.sleep(0.5)
        img_bkblock = Image.open('./image/bkBlock.png')
        real_width = img_bkblock.size[0]
        width_scale = float(real_width) / float(web_image_width)
        distance, template = self.get_postion('./image/bkBlock.png', './image/slideBlock.png')

        double_distance = int(distance - 70 + 20)
        print('移动距离: '+ str(double_distance))
        tracks = self.get_track(distance,double_distance)
        tracks.append(-(sum(tracks) - double_distance))
        # real_position = position[1] / width_scale
        # real_position = real_position - (slide_block_x - bk_block_x)
        # track_list = self.get_track(real_position - 4)


        ActionChains(driver).click_and_hold(on_element=slid_ing).perform()  # 点击鼠标左键，按住不放
        time.sleep(0.2)
        ActionChains(driver).move_by_offset(xoffset=double_distance, yoffset=0).perform()
        # print('第二步,拖动元素')
        # for track in tracks:
        #     ActionChains(driver).move_by_offset(xoffset=track, yoffset=0).perform()  # 鼠标移动到距离当前位置（x,y）
        #     time.sleep(0.002)
        # ActionChains(driver).move_by_offset(xoffset=-random.randint(0, 1), yoffset=0).perform()   # 微调，根据实际情况微调
        time.sleep(1)
        # print('第三步,释放鼠标')
        ActionChains(driver).release(on_element=slid_ing).perform()
        time.sleep(1)

        print('登录成功')
        self.after_quit()


if __name__ == '__main__':
    phone = "****"
    login = Login()
    login.login_main()
