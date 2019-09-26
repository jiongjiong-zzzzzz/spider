import cv2
import numpy as np
from PIL import Image as Im
otemp = '4.png'
oblk = '4.jpg'
target = cv2.imread(otemp, 0)
template = cv2.imread(oblk, 0)
w, h = target.shape[::-1]
temp = 'temp.jpg'
targ = 'targ.jpg'
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
print((y, x, y + w, x + h))

#调用PIL Image 做测试
# image = Im.open("image_test1.jpg")
#
# xy = (y+20, x+20, y + w-20, x + h-20)
# #切割
# imagecrop = image.crop(xy)
# #保存切割的缺口
# imagecrop.save("new_image.jpg")
