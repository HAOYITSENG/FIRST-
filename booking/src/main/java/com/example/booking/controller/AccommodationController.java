package com.example.booking.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import com.example.booking.model.Accommodation;
import com.example.booking.service.BookingService;

@RestController
@RequestMapping("/api/accommodations")
public class AccommodationController {

    private final BookingService bookingService;

    public AccommodationController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @GetMapping
    public List<Accommodation> list() {
        return bookingService.getAllAccommodations();
    }

    @GetMapping("/{id}")
    public Accommodation getById(@PathVariable Long id) {
        return bookingService.getAllAccommodations()
                .stream()
                .filter(acc -> acc.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    // 新增：根據地點搜尋住宿
    @GetMapping("/search")
    public List<Accommodation> searchByLocation(@RequestParam String location) {
        return bookingService.searchByLocation(location);
    }

    // 新增：查詢指定日期可用的住宿
    @GetMapping("/available")
    public List<Accommodation> getAvailable(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkIn,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkOut) {
        return bookingService.getAvailableAccommodations(checkIn, checkOut);
    }
}
