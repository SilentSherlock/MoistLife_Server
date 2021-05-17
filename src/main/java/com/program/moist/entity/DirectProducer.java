package com.program.moist.entity;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalTime;

/**
 * Date: 2021/5/12
 * Author: SilentSherlock
 * Description: describe the class features
 */
@Component
public class DirectProducer {


    @Resource
    private AmqpTemplate amqpTemplate;

    public void sendMessageDirect(String message) {
        amqpTemplate.convertAndSend("DirectExchange", "DirectRouting", "Greet from Producer: send message by direct fuckddddddddddddddddddddd" + LocalTime.now().toString());
    }
}
