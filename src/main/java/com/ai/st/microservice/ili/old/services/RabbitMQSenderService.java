package com.ai.st.microservice.ili.old.services;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.ai.st.microservice.ili.old.dto.IliProcessQueueDto;
import com.ai.st.microservice.ili.old.dto.ValidationDto;

@Service
public class RabbitMQSenderService {

    @Autowired
    private AmqpTemplate rabbitTemplate;

    @Value("${st.rabbitmq.queueIliOld.exchange}")
    public String exchangeIliName;

    @Value("${st.rabbitmq.queueIliOld.routingkey}")
    public String routingkeyIliName;

    @Value("${st.rabbitmq.queueResultValidationProducts.exchange}")
    public String exchangeResultValidationProductsName;

    @Value("${st.rabbitmq.queueResultValidationProducts.routingkey}")
    public String routingKeyResultValidationProductsName;

    public void sendStatsValidationQueueProducts(ValidationDto data) {
        rabbitTemplate.convertAndSend(exchangeResultValidationProductsName, routingKeyResultValidationProductsName,
                data);
    }

    public void sendDataToIliProcess(IliProcessQueueDto data) {
        rabbitTemplate.convertAndSend(exchangeIliName, routingkeyIliName, data);
    }

}
