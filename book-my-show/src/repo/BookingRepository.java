package repo;

import models.Booking;

import java.util.HashMap;
import java.util.Map;

public class BookingRepository {
    private Map<String, Booking> bookingMap = new HashMap<>();

    public void save(Booking booking) {
        bookingMap.put(booking.getId(), booking);
    }
}
