package com.example.notification.Service;

import com.example.notification.entity.Notification;
import com.example.notification.repository.NotificationRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @KafkaListener(topics = "orders", groupId = "notification-service-group")
    public void handleOrderMessage(String message) {
        Notification notification = new Notification();
        notification.setOrderId(extractOrderIdFromMessage(message));
        notification.setMessage(message);
        notification.setNotificationDate(LocalDateTime.now());


        notificationRepository.save(notification);

        System.out.println("Notification saved: " + notification.getMessage());
    }

    private Long extractOrderIdFromMessage(String message) {
        String[] parts = message.split(":");
        return Long.parseLong(parts[1].trim());
    }
}

