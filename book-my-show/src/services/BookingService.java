package services;

import enums.BookingStatus;
import enums.PaymentType;
import factory.PaymentStrategyFactory;
import models.Booking;
import models.Seat;
import models.Show;
import models.User;
import repo.BookingRepository;
import strategy.locking.LockProvider;
import strategy.payment.PaymentStrategy;

import java.util.List;

public class BookingService {
    private final BookingRepository bookingRepository;
    private final LockProvider lockProvider;
    private static final long TTL = 5000;

    public BookingService(BookingRepository bookingRepository, LockProvider lockProvider) {
        this.bookingRepository = bookingRepository;
        this.lockProvider = lockProvider;
    }

    public Booking createBooking(User user, Show show, List<Seat> seats) {
        for (Seat seat : seats) {
            String key = show.getId() + " : " + seat.getId();
            if (!lockProvider.lock(key, user.getId(), TTL)) {
                throw new RuntimeException("Seat " + seat.getId() + " is already locked. Please try again.");
            }
        }
        double totalPrice = 0.0;
        for (Seat seat : seats) {
            if (show.getScreen().getSeats().contains(seat))  {
                totalPrice += seat.getPrice();
            }
        }
        Booking booking = new Booking(user, show, seats, BookingStatus.CREATED, null, totalPrice);
        bookingRepository.save(booking);
        return booking;
    }

    public void confirmBooking(Booking booking, PaymentType paymentType) {
        if (booking.getBookingStatus() != BookingStatus.CREATED) {
            throw new RuntimeException("Booking is not in CREATED status. Cannot confirm booking.");
        }
        for (Seat seat: booking.getSeats()) {
            String key = booking.getShow().getId() + " : " + seat.getId();
            if (lockProvider.isLockExpired(key) || !lockProvider.isLockedBy(key, booking.getUser().getId())) {
                throw new RuntimeException("Seat " + seat.getId() + " is locked. Please try again.");
            }
        }
        booking.setPaymentType(paymentType);
        PaymentStrategy paymentStrategy = PaymentStrategyFactory.getStrategy(booking.getPaymentType());
        paymentStrategy.pay(booking);
        for (Seat seat: booking.getSeats()) {
            String key = booking.getShow().getId() + " : " + seat.getId();
            lockProvider.unlock(key);
        }
        booking.setBookingStatus(BookingStatus.CONFIRMED);
        System.out.println("Booking " + booking.getId() + " confirmed successfully.");
    }
}