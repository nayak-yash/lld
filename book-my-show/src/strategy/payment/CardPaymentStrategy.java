package strategy.payment;

import models.Booking;

public class CardPaymentStrategy implements PaymentStrategy {
    public boolean pay(Booking booking) {
        // Implement Card payment logic here
        System.out.println("Paid " + booking.getAmount() + " via Card for booking ID: " + booking.getId());
        return true;
    }
}