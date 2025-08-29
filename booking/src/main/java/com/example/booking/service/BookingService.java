package com.example.booking.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import com.example.booking.model.Accommodation;
import com.example.booking.model.Booking;
import com.example.booking.model.User;
import com.example.booking.repository.AccommodationRepository;
import com.example.booking.repository.BookingRepository;
import com.example.booking.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class BookingService {



    @Autowired
    private AccommodationRepository accommodationRepo;

    @Autowired
    private BookingRepository bookingRepo;

    @Autowired
    private UserRepository userRepo;

    @PostConstruct
    public void initData() {
        System.out.println("🔊 開始初始化資料...");
        System.out.println("住宿資料: " + accommodationRepo.count() + " 筆");
        System.out.println("用戶資料: " + userRepo.count() + " 筆");

        // ✅ 清空所有資料（按正確順序）
        bookingRepo.deleteAll();  // 先刪除訂房（子表）
        accommodationRepo.deleteAll();  // 再刪除住宿
        userRepo.deleteAll();  // 最後刪除用戶

        // ✅ 初始化用戶資料（明文密碼，配合 NoOpPasswordEncoder）
        List<User> initialUsers = List.of(
                createUser("admin", "password"),
                createUser("user1", "123456"),
                createUser("user2", "123456"),
                createUser("test", "test")
        );
        userRepo.saveAll(initialUsers);
        System.out.println("✅ 用戶資料已初始化，共 " + userRepo.count() + " 筆");

        // ✅ 初始化住宿資料
        List<Accommodation> initialAccommodations = List.of(
                new Accommodation(null, "Spring Hotel", "台北", "車站附近", new BigDecimal("2500")),
                new Accommodation(null, "Sea Resort", "高雄", "海景第一排", new BigDecimal("3800")),
                new Accommodation(null, "山城民宿", "台北", "超棒的飯店", new BigDecimal("1800")),
                new Accommodation(null, "海景民宿", "花蓮", "面海第一排", new BigDecimal("2200")),
                new Accommodation(null, "山景小屋", "南投", "被森林包圍", new BigDecimal("1500"))
        );
        accommodationRepo.saveAll(initialAccommodations);
        System.out.println("✅ 住宿資料已初始化，共 " + accommodationRepo.count() + " 筆");

        System.out.println("🎉 所有資料初始化完成！");
        System.out.println("📝 可用的測試帳號:");
        System.out.println("   - admin / password");
        System.out.println("   - user1 / 123456");
        System.out.println("   - user2 / 123456");
        System.out.println("   - test / test");
    }

    // 輔助方法：建立用戶
    private User createUser(String username, String password) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);  // 明文密碼（因為使用 NoOpPasswordEncoder）
        return user;
    }

    public List<Accommodation> getAllAccommodations() {
        return accommodationRepo.findAll();
    }

    public List<Booking> getBookingsForUser(String username) {
        return bookingRepo.findByUserUsername(username);
    }
    //使用資料庫存儲訂房
    public Booking book(long accommodationId, LocalDate checkIn, LocalDate checkOut) {
        String username = getLoggedInUsername();

        // 從資料庫查詢用戶
        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("找不到用戶：" + username));

        // 從資料庫查詢住宿
        Accommodation acc = accommodationRepo.findById(accommodationId)
                .orElseThrow(() -> new RuntimeException("找不到 ID=" + accommodationId + " 的住宿"));

        // ✅ 新增：檢查日期衝突
        List<Booking> conflictingBookings = bookingRepo.findConflictingBookings(accommodationId, checkIn, checkOut);
        if (!conflictingBookings.isEmpty()) {
            throw new RuntimeException("此住宿在指定日期已被預訂");
        }

        // 建立新的訂房記錄（不需要手動設定 ID）
        Booking booking = new Booking(null, checkIn, checkOut, acc, user);

        // ✅ 儲存到資料庫
        return bookingRepo.save(booking);
    }
    private String getLoggedInUsername() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails userDetails) {
            return userDetails.getUsername();
        } else {
            return principal.toString();
        }
    }

    public List<Accommodation> searchByLocation(String location) {
        return accommodationRepo.findByLocationContainingIgnoreCase(location);
    }

    public List<Accommodation> getAvailableAccommodations(LocalDate checkIn, LocalDate checkOut) {
        return accommodationRepo.findAvailableAccommodations(checkIn, checkOut);
    }
}