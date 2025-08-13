package com.example.booking.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import com.example.booking.model.Accommodation;
import com.example.booking.model.Booking;
import com.example.booking.model.User;
import com.example.booking.repository.AccommodationRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class BookingService {

    private final AtomicLong counter = new AtomicLong();
    private final List<Booking> bookings = new ArrayList<>();

    @Autowired
    private AccommodationRepository accommodationRepo;

    @PostConstruct
    public void initData() {
        System.out.println("📊 目前資料庫有 " + accommodationRepo.count() + " 筆住宿資料");

        // ✅ 重新初始化資料
        accommodationRepo.deleteAll();

        List<Accommodation> initialData = List.of(
                new Accommodation(null, "Spring Hotel", "台北", "車站附近"),
                new Accommodation(null, "Sea Resort", "高雄", "海景第一排"),
                new Accommodation(null, "山城民宿", "台北", "超棒的飯店"),
                new Accommodation(null, "海景民宿", "花蓮", "面海第一排"),
                new Accommodation(null, "山景小屋", "南投", "被森林包圍")
        );

        accommodationRepo.saveAll(initialData);

        System.out.println("✅ 住宿資料已重新初始化，共 " + accommodationRepo.count() + " 筆");
    }

    public List<Accommodation> getAllAccommodations() {
        return accommodationRepo.findAll();
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
        String username = getLoggedInUsername();
        User user = new User();
        user.setUsername(username);

        Accommodation acc = accommodationRepo.findById(accommodationId)
                .orElseThrow(() -> new RuntimeException("找不到 ID=" + accommodationId + " 的住宿"));

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