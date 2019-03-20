#!/bin/bash
source /etc/profile
export JAVA_HOME=/usr/java/jdk1.8.0_144
export CLASSPATH=.:$JAVA_HOME/lib/dt.jar:$JAVA_HOME/lib/tools.jar
export PATH=$PATH:$JAVA_HOME/bin

echo "paper-starttime:" `date` >> /usr/java/spider.log


$JAVA_HOME/bin/java -jar /usr/java/Spider_SinaNew.jar >> /usr/java/log/Spider_SinaNew.log 1>&- 2>&-
echo "Spider_SinaNew" >> /usr/java/spider.log

echo "paper-endtime: " `date`  >> /usr/java/spider.log
echo "======" >> /usr/java/spider.log

