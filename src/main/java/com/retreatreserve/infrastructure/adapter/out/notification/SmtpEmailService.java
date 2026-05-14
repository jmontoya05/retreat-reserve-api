package com.retreatreserve.infrastructure.adapter.out.notification;

import com.retreatreserve.application.port.out.notification.EmailService;
import com.retreatreserve.infrastructure.adapter.out.notification.template.EmailTemplateEngine;
import com.retreatreserve.infrastructure.exception.notification.EmailSendingException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.io.UnsupportedEncodingException;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class SmtpEmailService implements EmailService {

    private final JavaMailSender mailSender;
    private final EmailTemplateEngine templateEngine;
    private static final String USER_NAME = "userName";
    
    @Value("${email.from}")
    private String fromEmail;
    
    @Value("${email.verification-base-url}")
    private String frontendUrl;
    
    @Override
    @Async
    public void sendVerificationEmail(String toEmail, String userName, String verificationToken) {
        String verificationLink = frontendUrl + "/verify-email?token=" + verificationToken;
        
        Map<String, String> variables = Map.of(
            USER_NAME, userName,
            "verificationLink", verificationLink
        );
        
        String htmlContent = templateEngine.processTemplate("verification-email.html", variables);
        
        sendEmail(toEmail, "Verify Your Email - Retreat Reserve", htmlContent);
    }
    
    @Override
    @Async
    public void sendReservationConfirmation(String toEmail, String userName, String cabinName,
                                           String checkInDate, String checkOutDate, String totalPrice) {
        Map<String, String> variables = Map.of(
            USER_NAME, userName,
            "cabinName", cabinName,
            "checkInDate", checkInDate,
            "checkOutDate", checkOutDate,
            "totalPrice", totalPrice
        );
        
        String htmlContent = templateEngine.processTemplate("reservation-confirmation.html", variables);
        
        sendEmail(toEmail, "Reservation Confirmed - Retreat Reserve", htmlContent);
    }
    
    @Override
    @Async
    public void sendReservationCancellation(String toEmail, String userName, String cabinName) {
        Map<String, String> variables = Map.of(
            USER_NAME, userName,
            "cabinName", cabinName
        );
        
        String htmlContent = templateEngine.processTemplate("reservation-cancellation.html", variables);
        
        sendEmail(toEmail, "Reservation Cancelled - Retreat Reserve", htmlContent);
    }
    
    @Override
    @Async
    public void sendPasswordResetEmail(String toEmail, String userName, String resetToken) {
        String resetLink = frontendUrl + "/reset-password?token=" + resetToken;
        
        Map<String, String> variables = Map.of(
            USER_NAME, userName,
            "resetLink", resetLink
        );
        
        String htmlContent = templateEngine.processTemplate("password-reset.html", variables);
        
        sendEmail(toEmail, "Reset Your Password - Retreat Reserve", htmlContent);
    }
    
    private void sendEmail(String to, String subject, String htmlContent) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            
            helper.setFrom(
                new InternetAddress(fromEmail, "Retreat Reserve")
            );
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);
            
            mailSender.send(message);
            log.info("Email sent successfully to: {}", to);
            
        } catch (MessagingException | UnsupportedEncodingException e) {
            log.error("Failed to send email to: {}", to, e);
            throw new EmailSendingException("Failed to send email", e);
        }
    }
}
