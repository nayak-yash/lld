package strategy.payment;

import models.Booking;

public interface PaymentStrategy {
    boolean pay(Booking booking);
}
