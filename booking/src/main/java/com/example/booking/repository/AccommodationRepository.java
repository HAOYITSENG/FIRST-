package com.example.booking.repository;

import com.example.booking.model.Accommodation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccommodationRepository extends JpaRepository<Accommodation, Long> {
    // Spring Data JPA 會自動產生以下方法：
    // - findAll()          → 查詢所有
    // - findById(Long id)  → 根據 ID 查詢
    // - save(entity)       → 新增或更新
    // - deleteById(Long id)→ 根據 ID 刪除
    // - count()            → 計算總數
}