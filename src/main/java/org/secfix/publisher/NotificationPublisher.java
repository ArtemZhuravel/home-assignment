package org.secfix.publisher;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class NotificationPublisher {

    private final RabbitTemplate rabbitTemplate;
    private final String exchange = "observed-repo-exchange";
    private final String routingKey = "observed-repo-routing-key";

    public NotificationPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publishNotification(String message) {
        rabbitTemplate.convertAndSend(exchange, routingKey, message);
        System.out.println("Notification Published: " + message);
    }
}
