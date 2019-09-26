import pytesseract
from PIL import Image
text = pytesseract.image_to_string(Image.open("222.png"),lang='eng')
print(text)