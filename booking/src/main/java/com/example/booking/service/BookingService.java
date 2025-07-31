package com.example.booking.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import com.example.booking.model.Accommodation;
import com.example.booking.model.Booking;
import com.example.booking.model.User;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class BookingService {
    private final AtomicLong counter = new AtomicLong();
    private final List<Accommodation> accommodations = new ArrayList<>();
    private final List<Booking> bookings = new ArrayList<>();

    public BookingService() {
        // Sample accommodation data
        accommodations.add(new Accommodation(1L, "Spring Hotel", "Taipei", "Near station"));
        accommodations.add(new Accommodation(2L, "Sea Resort", "Kaohsiung", "Ocean view"));
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

    public Booking book(long accommodationId, LocalDate checkIn, LocalDate checkOut) {
        String username = getLoggedInUsername(); // 從登入資訊取得使用者名稱
        User user = new User();
        user.setUsername(username);

        Accommodation acc = accommodations.stream()
                .filter(a -> a.getId() == accommodationId)
                .findFirst()
                .orElseThrow();

        Booking booking = new Booking(counter.incrementAndGet(), checkIn, checkOut, acc, user);
        bookings.add(booking);
        return booking;
    }

    private String getLoggedInUsername() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails userDetails) {
            return userDetails.getUsername();
        } else {
            return principal.toString();
        }
    }
}
