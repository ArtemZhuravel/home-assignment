package org.secfix.listener;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class NotificationListener {

    @RabbitListener(queues = "observed-repo-queue")
    public void listenForNotification(String message) {
        System.out.println("Received Notification: " + message);
    }
}
