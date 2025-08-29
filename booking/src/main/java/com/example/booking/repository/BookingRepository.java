package com.example.booking.repository;

import com.example.booking.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    // 根據用戶名稱查詢訂房記錄
    @Query("SELECT b FROM Booking b WHERE b.user.username = :username")
    List<Booking> findByUserUsername(@Param("username") String username);

    // 查詢指定住宿在特定日期範圍內的訂房（用於檢查是否衝突）
    @Query("SELECT b FROM Booking b WHERE b.accommodation.id = :accommodationId " +
            "AND ((b.checkIn <= :checkOut) AND (b.checkOut >= :checkIn))")
    List<Booking> findConflictingBookings(
            @Param("accommodationId") Long accommodationId,
            @Param("checkIn") LocalDate checkIn,
            @Param("checkOut") LocalDate checkOut
    );
}