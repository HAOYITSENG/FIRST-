package com.example.booking.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Service;

import com.example.booking.model.Accommodation;
import com.example.booking.model.Booking;
import com.example.booking.model.User;

@Service
public class BookingService {
    private final AtomicLong counter = new AtomicLong();
    private final List<Accommodation> accommodations = new ArrayList<>();
    private final List<User> users = new ArrayList<>();
    private final List<Booking> bookings = new ArrayList<>();

    public BookingService() {
        // sample data
        accommodations.add(new Accommodation(1L, "Spring Hotel", "Taipei", "Near station"));
        accommodations.add(new Accommodation(2L, "Sea Resort", "Kaohsiung", "Ocean view"));

        users.add(new User("user", "password"));
    }

    public List<Accommodation> getAllAccommodations() {
        return accommodations;
    }

    public List<Booking> getBookingsForUser(String username) {
        List<Booking> result = new ArrayList<>();
        for (Booking b : bookings) {
            if (b.getUser().getUsername().equals(username)) {
                result.add(b);
            }
        }
        return result;
    }

    public Booking book(String username, long accommodationId, LocalDate checkIn, LocalDate checkOut) {
        User user = users.stream().filter(u -> u.getUsername().equals(username)).findFirst().orElseThrow();
        Accommodation acc = accommodations.stream().filter(a -> a.getId() == accommodationId).findFirst().orElseThrow();
        Booking booking = new Booking(counter.incrementAndGet(), checkIn, checkOut, acc, user);
        bookings.add(booking);
        return booking;
    }
}
