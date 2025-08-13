package com.example.booking.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
