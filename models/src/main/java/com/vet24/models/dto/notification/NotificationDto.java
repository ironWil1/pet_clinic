package com.vet24.models.dto.notification;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.math.BigInteger;
import java.sql.Date;


@AllArgsConstructor
@NoArgsConstructor
public class NotificationDto {

    @NotBlank
    private BigInteger id;

    @Email
    private String email;

    @NotBlank
    private String content;

    @NotBlank
    private Date event_date;

    public Long getId() {
        return id.longValue();
    }

    public String getEmail() {
        return email;
    }

    public String getContent() {
        return content;
    }

    public Date getEvent_date() {
        return event_date;
    }

    public void setId(BigInteger id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setEvent_date(Date event_date) {
        this.event_date = event_date;
    }

    @Override
    public String toString() {
        return "NotificationDto{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", content='" + content + '\'' +
                ", event_date=" + event_date +
                '}';
    }
}
