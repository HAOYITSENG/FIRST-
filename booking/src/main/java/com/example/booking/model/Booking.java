package com.example.booking.model;

import java.time.LocalDate;

public class Booking {
    private Long id;
    private LocalDate checkIn;
    private LocalDate checkOut;
    private Accommodation accommodation;
    private User user;

    public Booking() {}

    public Booking(Long id, LocalDate checkIn, LocalDate checkOut, Accommodation accommodation, User user) {
        this.id = id;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.accommodation = accommodation;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getCheckIn() {
        return checkIn;
    }

    public void setCheckIn(LocalDate checkIn) {
        this.checkIn = checkIn;
    }

    public LocalDate getCheckOut() {
        return checkOut;
    }

    public void setCheckOut(LocalDate checkOut) {
        this.checkOut = checkOut;
    }

    public Accommodation getAccommodation() {
        return accommodation;
    }

    public void setAccommodation(Accommodation accommodation) {
        this.accommodation = accommodation;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
