package strategy.payment;

import models.Booking;

public class UpiPaymentStrategy implements PaymentStrategy {
    public boolean pay(Booking booking) {
        // Implement UPI payment logic here
        System.out.println("Paid " + booking.getAmount() + " via UPI for booking ID: " + booking.getId());
        return true;
    }
}
