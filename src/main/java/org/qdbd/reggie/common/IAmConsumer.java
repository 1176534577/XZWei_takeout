package org.qdbd.reggie.common;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.qdbd.reggie.utils.ValidateCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class IAmConsumer {

    @Autowired
    private RedisTemplate redisTemplate;

    private final static String TOPIC_NAME = "telephone";


    /**
     * @param record record
     * @KafkaListener(groupId = "testGroup", topicPartitions = {
     * @TopicPartition(topic = "topic1", partitions = {"0", "1"}),
     * @TopicPartition(topic = "topic2", partitions = "0",
     * partitionOffsets = @PartitionOffset(partition = "1", initialOffset = "100"))
     * },concurrency = "6")
     * //concurrency就是同组下的消费者个数，就是并发消费数，必须小于等于分区总数
     */
    @KafkaListener(topics = TOPIC_NAME, groupId = "readerGroup")
    public void listenGroup1(ConsumerRecord<String, String> record, Acknowledgment ack) {
        String phone = record.value();

        String code = ValidateCodeUtils.generateValidateCode(4).toString();
        log.info("code=" + code);
        // SMSUtils.sendMessage("阿里云短信测试", "SMS_154950909", phone, code);

        // session.setAttribute(phone, code);
        // 生成验证码缓存到redis，并设置有效期5分钟
        redisTemplate.opsForValue().set(phone, code, 5, TimeUnit.MINUTES);

        System.out.println("Group1 message: " + phone);
        System.out.println("Group1 record: " + record);
        //手动提交offset，一般是提交一个banch，幂等性防止重复消息
        // === 每条消费完确认性能不好！
        // ack.acknowledge();
    }

    //配置多个消费组
    /* @KafkaListener(topics = TOPIC_NAME, groupId = "Group2")
    public void listenGroup2(ConsumerRecord<String, String> record, Acknowledgment ack) {
        String value = record.value();
        System.out.println("Group2 message: " + value);
        System.out.println("Group2 record: " + record);
        //手动提交offset
        ack.acknowledge();
    } */
}

