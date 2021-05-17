package com.program.moist.service;

import org.springframework.stereotype.Service;

/**
 * Date: 2021/5/12
 * Author: SilentSherlock
 * Description: describe the class features
 */
@Service
public class MessageService {
    private String queueName;
    private String exchangeName;

    public void setQueueName(String queueName) {
        this.queueName = queueName;
    }

    public void setExchangeName(String exchangeName) {
        this.exchangeName = exchangeName;
    }


}
