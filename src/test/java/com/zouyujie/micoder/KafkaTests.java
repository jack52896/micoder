package com.zouyujie.micoder;

import org.apache.commons.lang3.builder.ToStringExclude;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.KafkaListeners;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = MicoderApplication.class)
public class KafkaTests {
    @Autowired
    KafkaProducer kafkaProducer;
    @Test
    public void kafka() {
        kafkaProducer.send("test", "能收到消息嘛");
        kafkaProducer.send("test", "可以扣一");
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
@Component
class KafkaProducer{
    @Autowired
    private KafkaTemplate kafkaTemplate;

    /*
           topic 消息标题
           content 发送的消息内容主题
     */
    public void send(String topic, String content){
        kafkaTemplate.send(topic,content);
    }
}
@Component
class KafkaComsumer{

    @KafkaListener(topics = {"test"})
    public void consumer(ConsumerRecord consumerRecord){
        System.out.println(consumerRecord.value());
    }
}