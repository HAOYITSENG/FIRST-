package com.example.booking.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

@Entity
@Table(name = "bookings")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "check_in", nullable = false)
    private LocalDate checkIn;

    @Column(name = "check_out", nullable = false)
    private LocalDate checkOut;

    // 加入 @JsonIgnoreProperties 忽略 Hibernate 代理物件屬性
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "accommodation_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Accommodation accommodation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private User user;


    // 新增總價欄位
    @Column(precision = 10, scale = 2)
    private BigDecimal totalPrice;

    // 預設建構子（JPA 需要）
    public Booking() {}

    public Booking(Long id, LocalDate checkIn, LocalDate checkOut, Accommodation accommodation, User user) {
        this.id = id;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.accommodation = accommodation;
        this.user = user;
        this.calculateTotalPrice(); // 自動計算總價
    }

    // 計算總價的方法
    public void calculateTotalPrice() {
        if (accommodation != null && checkIn != null && checkOut != null) {
            long nights = ChronoUnit.DAYS.between(checkIn, checkOut);
            this.totalPrice = accommodation.getPricePerNight().multiply(BigDecimal.valueOf(nights));
        }
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
        calculateTotalPrice();
    }

    public LocalDate getCheckOut() {
        return checkOut;
    }

    public void setCheckOut(LocalDate checkOut) {
        this.checkOut = checkOut;
        calculateTotalPrice();
    }

    public Accommodation getAccommodation() { return accommodation; }
    public void setAccommodation(Accommodation accommodation) {
        this.accommodation = accommodation;
        calculateTotalPrice();
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public BigDecimal getTotalPrice() { return totalPrice; }

    public void setTotalPrice(BigDecimal totalPrice) { this.totalPrice = totalPrice; }
}
