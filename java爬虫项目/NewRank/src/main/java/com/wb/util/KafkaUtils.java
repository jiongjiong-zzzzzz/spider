package com.wb.util;

import java.util.Arrays;
import java.util.Date;
import java.util.Properties;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class KafkaUtils {
	private final static Logger LOGGER = LoggerFactory.getLogger(KafkaUtils.class);
	private static Producer<String, String> producer;
	private static Consumer<String, String> consumer;

	private KafkaUtils() {
	}

	/**
	 * 生产者，注意kafka生产者不能够从代码上生成主题，只有在服务器上用命令生成     
	 */
	static {
		Properties props = new Properties();
		props.put("bootstrap.servers", "192.168.3.69:9092");// 服务器ip:端口号，集群用逗号分隔
		props.put("acks", "all");
		props.put("retries", 0);
		props.put("batch.size", 16384);
		props.put("linger.ms", 1);
		props.put("buffer.memory", 33554432);
		props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		producer = new KafkaProducer<String, String>(props);
	}

	/**
	 * 发送对象消息 至kafka上,调用json转化为json字符串，应为kafka存储的是String
	 * 
	 * @param msg
	 */
	public static void sendMsgToKafka(String msg) {
		producer.send(new ProducerRecord<String, String>("news", String.valueOf(new Date().getTime()), msg));
	}

	/**
	 * 发送对象消息 至kafka上,调用json转化为json字符串，应为kafka存储的是String
	 * 
	 * @param msg
	 */
	public static void sendMsgToKafkaAccount(String msg) {
		producer.send(new ProducerRecord<String, String>("account", String.valueOf(new Date().getTime()), msg));
	}

	public static void closeKafkaProducer() {
		producer.close();
	}

}
