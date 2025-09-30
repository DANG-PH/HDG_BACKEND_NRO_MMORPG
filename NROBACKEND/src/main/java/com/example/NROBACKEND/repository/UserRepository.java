package com.example.NROBACKEND.repository;

import com.example.NROBACKEND.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.*;

public interface UserRepository extends JpaRepository<User, Long> {

    // ===== CÁCH 1: Dùng method name convention (Spring Data JPA tự sinh SQL) =====

    boolean existsByUsername(String username);
    // Spring phân tích tên hàm "existsByUsername"
    // -> Tự generate SQL:
    // SELECT CASE WHEN COUNT(*) > 0 THEN TRUE ELSE FALSE END
    // FROM users WHERE username = ?;
    // Trả về true/false.

    User findByUsername(String username);
    // Spring phân tích tên hàm "findByUsername"
    // -> Tự generate SQL:
    // SELECT * FROM users WHERE username = ?;
    // Nếu tìm thấy thì ánh xạ row -> User object, không thì trả về null.


    // ===== CÁCH 2: Dùng @Query (tự viết SQL/HQL) =====

    @Query("SELECT u FROM User u WHERE u.username = :username")
    User timNguoiTheoTen(@Param("username") String username);
    // Ở đây mình chủ động viết query HQL (giống SQL nhưng dùng tên entity và field).
    // Spring không dựa vào tên method nữa, mà chạy đúng query mình viết.
    // Query này tương đương SQL:
    // SELECT * FROM users WHERE username = ?;


    List<User> findTop10ByOrderBySucManhDesc();

    List<User> findTop10ByOrderByVangDesc();
}