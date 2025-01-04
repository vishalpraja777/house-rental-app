package com.rentalapp.houserentalapp.dao;

import com.rentalapp.houserentalapp.model.entities.Users;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<Users, Long> {
    Users findByUsername(String username);

    Users findByEmail(String email);

    Users findByPhone(String phone);

    @Transactional
    @Modifying
    @Query(value = "update users set status=?1 where user_id=?2", nativeQuery = true)
    Integer updateStatus(@Param("status") String status, @Param("id") Long id);

    @Transactional
    @Modifying
    @Query(value = "update users set password=?1 where user_id=?2", nativeQuery = true)
    Integer changePassword(String encodedPassword, Long userId);
}
