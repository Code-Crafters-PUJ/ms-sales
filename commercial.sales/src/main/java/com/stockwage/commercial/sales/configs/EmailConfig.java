package com.stockwage.commercial.sales.configs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Configuration
public class EmailConfig {

    @Value("${spring.mail.host}")
    private String Host;

    @Value("${spring.mail.port}")
    private int Port;

    @Value("${spring.mail.username}")
    private String Username;

    @Value("${spring.mail.password}")
    private String Password;

    @Value("${spring.mail.properties.mail.smtp.auth}")
    private boolean SmtpAuth;

    @Value("${spring.mail.properties.mail.smtp.starttls.enable}")
    private boolean SmtpStarttlsEnable;

    public boolean isMailSmtpAuth() {
        return SmtpAuth;
    }

    public boolean isMailSmtpStarttlsEnable() {
        return SmtpStarttlsEnable;
    }


    public EmailConfig(
            @Value("${spring.mail.username}") String username,
            @Value("${spring.mail.password}") String password) {
        this.Username = username;
        this.Password = password;
    }
}