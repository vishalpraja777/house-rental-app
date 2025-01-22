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
    @Query("update Users set status=:status where userId=:userId")
    Integer updateStatus(@Param("status") Users.Status status, @Param("userId") Long userId);

    @Transactional
    @Modifying
    @Query("update Users set role=:role where userId=:userId")
    Integer updateRole(@Param("role") Users.Role role, @Param("userId") Long userId);

    @Transactional
    @Modifying
    @Query("update Users set password=:encodedPassword where userId=:userId")
    Integer changePassword(String encodedPassword, Long userId);
}
