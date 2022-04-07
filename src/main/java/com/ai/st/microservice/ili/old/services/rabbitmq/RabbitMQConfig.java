package com.ai.st.microservice.ili.old.services.rabbitmq;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;

import org.springframework.beans.factory.annotation.Value;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Value("${st.rabbitmq.queueIliOld.queue}")
    public String queueIliName;

    @Value("${st.rabbitmq.queueIliOld.exchange}")
    public String exchangeIliName;

    @Value("${st.rabbitmq.queueIliOld.routingkey}")
    public String routingkeyIliName;

    @Value("${st.rabbitmq.queueResultValidationProducts.queue}")
    public String queueResultValidationProducts;

    @Value("${st.rabbitmq.queueResultValidationProducts.exchange}")
    public String exchangeResultValidationProducts;

    @Value("${st.rabbitmq.queueResultValidationProducts.routingkey}")
    public String routingKeyResultValidationProducts;

    @Bean
    public Queue queueIli() {
        return new Queue(queueIliName, false);
    }

    @Bean
    public DirectExchange exchangeIli() {
        return new DirectExchange(exchangeIliName);
    }

    @Bean
    public Binding bindingQueueIli() {
        return BindingBuilder.bind(queueIli()).to(exchangeIli()).with(routingkeyIliName);
    }

    @Bean
    public Queue QueueResultValidation() {
        return new Queue(queueResultValidationProducts, false);
    }

    @Bean
    public DirectExchange exchangeResultValidation() {
        return new DirectExchange(exchangeResultValidationProducts);
    }

    @Bean
    public Binding bindingQueueResultValidation() {
        return BindingBuilder.bind(QueueResultValidation()).to(exchangeResultValidation())
                .with(routingKeyResultValidationProducts);
    }

    @Bean
    public Jackson2JsonMessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public AmqpTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }

}
