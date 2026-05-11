package com.retreatreserve.application.port.out.notification;

/**
 * Output port for email service.
 */
public interface EmailService {
    
    /**
     * Sends email verification link to user.
     */
    void sendVerificationEmail(String toEmail, String userName, String verificationToken);
    
    /**
     * Sends reservation confirmation email to user.
     */
    void sendReservationConfirmation(String toEmail, String userName, String cabinName, 
                                     String checkInDate, String checkOutDate, String totalPrice);
    
    /**
     * Sends reservation cancellation email.
     */
    void sendReservationCancellation(String toEmail, String userName, String cabinName);
    
    /**
     * Sends password reset email.
     */
    void sendPasswordResetEmail(String toEmail, String userName, String resetToken);
}
