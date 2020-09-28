package com.example.notificationservice.controller;

import com.example.notificationservice.entity.Notification;
import com.example.notificationservice.entity.NotificationType;
import com.example.notificationservice.repository.NotificationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.Date;
import java.util.Iterator;


@Controller
public class NotificationController {

    final static Logger logger = LoggerFactory.getLogger(NotificationController.class.getName());

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private NotificationRepository notificationRepository;

    @GetMapping("/notifications")
    public String getAllNotifications(Model model) {
        Iterable<Notification> notifications = notificationRepository.findAll();
        model.addAttribute("notificationsLists", notifications);
        return "notificationsPage";
    }

    @GetMapping("/notifyMe")
    public String notifyMe(Model model) {
        Notification notification = new Notification("", new Date(), NotificationType.values()[0], "");
        model.addAttribute("notification", notification);
        return "notifyMePage";
    }

    @PostMapping("/notifyMe")
    @ResponseStatus(HttpStatus.CREATED)
    public String notifyMe(Model model, Notification notification) {

        if (notification.getTime().before(new Date())) {
            model.addAttribute("errorMessage", "Введенная Вами дата уже прошла. Будильник не может напомнить Вам из прошлого!");
            return "notifyMePage";
        }

        if (notification.getExtraParams().isEmpty() || notification.getMessage().isEmpty()) {
            model.addAttribute("errorMessage", "Заполните все поля!");
            return "notifyMePage";
        }

        try {
            notificationRepository.save(notification);
            logger.info("Notification with ID=" + notification.getExternalId() + " created");
            model.addAttribute("success", "Напоминание создано!");
        } catch (DataAccessException e) {
            logger.error("Error saving notification with ID=" + notification.getExternalId(), e.getCause().getMessage());
        }

        return "notifyMePage";
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(Notification notification) {
        try {
            notificationRepository.deleteById(notification.getExternalId());
            logger.info("Notification with ID=" + notification.getExternalId() + " deleted");
        } catch (DataAccessException e) {
            logger.error("Error deleting notification with ID=" + notification.getExternalId(), e.getCause().getMessage());
        }

    }

    @Scheduled(fixedRate = 60000)
    private void notifyMeScheduled() {
        Iterator<Notification> notifications = notificationRepository.findAll(Sort.by(Sort.Direction.ASC, "time")).iterator();
        while (notifications.hasNext()) {
            Notification nextNotification = notifications.next();

            if ((nextNotification.getTime().before(new Date()) || nextNotification.getTime().equals(new Date())) && nextNotification.getNotificationType() == NotificationType.MAIL) {
                try {
                    SimpleMailMessage message = new SimpleMailMessage();
                    message.setTo(nextNotification.getExtraParams());
                    message.setSubject("Test Simple Email");
                    message.setText(nextNotification.getMessage());
                    this.javaMailSender.send(message);
                    logger.info("Mail notification with ID=" + nextNotification.getExternalId() + " sended");
                    deleteById(nextNotification);
                    notifications.remove();
                } catch (MailException mailException) {
                    logger.error("Mail notification send with ID=" + nextNotification.getExternalId() + " failed", mailException);
                }
            } else if ((nextNotification.getTime().before(new Date()) || nextNotification.getTime().equals(new Date())) && nextNotification.getNotificationType() == NotificationType.HTTP) {
                //Логика напоминания путем HTTP
            }
        }
    }


}
