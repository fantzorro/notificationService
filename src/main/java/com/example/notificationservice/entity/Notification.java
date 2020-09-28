package com.example.notificationservice.entity;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "NOTIFICATIONS")
public class Notification {
    @Id
    @Column(name = "EXTERNAL_ID")
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", strategy = "uuid")
    private String externalId;

    @Column(name = "MESSAGE")
    private String message;

    @Column(name = "TIME")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private Date time;

    @Column(name = "NOTIFICATION_TYPE")
    @Enumerated(EnumType.STRING)
    private NotificationType notificationType;

    @Column(name = "EXTRA_PARAMS")
    private String extraParams;

    public Notification() {
    }

    public Notification(String message, Date time, NotificationType notificationType, String extraParams) {
        this.message = message;
        this.time = time;
        this.notificationType = notificationType;
        this.extraParams = extraParams;
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public NotificationType getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(NotificationType notificationType) {
        this.notificationType = notificationType;
    }

    public String getExtraParams() {
        return extraParams;
    }

    public void setExtraParams(String extraParams) {
        this.extraParams = extraParams;
    }
}
