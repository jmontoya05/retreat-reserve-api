package com.retreatreserve.infrastructure.adapter.out.notification;

import org.springframework.stereotype.Component;

import com.retreatreserve.application.port.out.notification.EmailService;

@Component
public class SmtpEmailService implements EmailService {

    @Override
    public void sendVerificationEmail(String toEmail, String userName, String verificationToken) {
        throw new UnsupportedOperationException("Unimplemented method 'sendVerificationEmail'");
    }

    @Override
    public void sendReservationConfirmation(String toEmail, String userName, String cabinName, String checkInDate,
            String checkOutDate, String totalPrice) {
        throw new UnsupportedOperationException("Unimplemented method 'sendReservationConfirmation'");
    }

    @Override
    public void sendReservationCancellation(String toEmail, String userName, String cabinName) {
        throw new UnsupportedOperationException("Unimplemented method 'sendReservationCancellation'");
    }

    @Override
    public void sendPasswordResetEmail(String toEmail, String userName, String resetToken) {
        throw new UnsupportedOperationException("Unimplemented method 'sendPasswordResetEmail'");
    }
}
