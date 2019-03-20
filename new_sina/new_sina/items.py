# -*- coding: utf-8 -*-

# Define here the models for your scraped items
#
# See documentation in:
# https://doc.scrapy.org/en/latest/topics/items.html

import scrapy


class NewSinaItem(scrapy.Item):
    # define the fields for your item here like:
    title = scrapy.Field()
    href = scrapy.Field()
    publish_date = scrapy.Field()
    concent = scrapy.Field()
    concent_img = scrapy.Field()

