package models;

import enums.BookingStatus;
import enums.PaymentType;

import java.util.List;
import java.util.UUID;

public class Booking {
    private final String id;
    private final double amount;
    private final User user;
    private final Show show;
    private final List<Seat> seats;
    private BookingStatus bookingStatus;
    private PaymentType paymentType;

    public Booking(User user, Show show, List<Seat> seats, BookingStatus bookingStatus, PaymentType paymentType, double amount) {
        this.id = UUID.randomUUID().toString();
        this.user = user;
        this.show = show;
        this.seats = seats;
        this.bookingStatus = bookingStatus;
        this.paymentType = paymentType;
        this.amount = amount;
    }

    public String getId() {
        return id;
    }

    public double getAmount() {
        return amount;
    }

    public User getUser() {
        return user;
    }

    public Show getShow() {
        return show;
    }

    public List<Seat> getSeats() {
        return seats;
    }

    public BookingStatus getBookingStatus() {
        return bookingStatus;
    }

    public PaymentType getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(PaymentType paymentType) {
        this.paymentType = paymentType;
    }

    public void setBookingStatus(BookingStatus bookingStatus) {
        this.bookingStatus = bookingStatus;
    }
}
