package com.example.booking.repository;

import com.example.booking.model.Accommodation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.time.LocalDate;
import java.util.List;

@Repository
public interface AccommodationRepository extends JpaRepository<Accommodation, Long> {
    // Spring Data JPA 會自動產生以下方法：
    // - findAll()          → 查詢所有
    // - findById(Long id)  → 根據 ID 查詢
    // - save(entity)       → 新增或更新
    // - deleteById(Long id)→ 根據 ID 刪除
    // - count()            → 計算總數

    // 根據地點搜尋（忽略大小寫，模糊搜尋）
    List<Accommodation> findByLocationContainingIgnoreCase(String location);

    // 查詢在指定日期區間沒有被預訂的住宿
    @Query("SELECT a FROM Accommodation a WHERE a.id NOT IN " +
            "(SELECT DISTINCT b.accommodation.id FROM Booking b " +
            "WHERE (b.checkIn <= :checkOut) AND (b.checkOut >= :checkIn))")
    List<Accommodation> findAvailableAccommodations(
            @Param("checkIn") LocalDate checkIn,
            @Param("checkOut") LocalDate checkOut
    );
}