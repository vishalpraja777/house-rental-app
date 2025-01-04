package com.rentalapp.houserentalapp.service.impl;

import com.rentalapp.houserentalapp.model.entities.Users;
import com.rentalapp.houserentalapp.util.SecurityUtil;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;

public class UserServiceBaseImpl {

    protected static boolean isUserAuthorized(Users oldUser) {

        UserDetails currentUser = SecurityUtil.getCurrentUser();

        if (currentUser == null || oldUser == null || !Users.Status.ACTIVE.equals(oldUser.getStatus())) {
            return false;
        }

        if (currentUser.getUsername().equals(oldUser.getUsername())) {
            return true;
        }
        return currentUser.getAuthorities().contains(new SimpleGrantedAuthority("ADMIN"));
    }

    protected Users updateUserFields(Users userToUpdate, Users oldUser) {

        if (oldUser.getUsername() != null) {
            userToUpdate.setUsername(oldUser.getUsername());
        }
        if (oldUser.getFirstName() != null) {
            userToUpdate.setFirstName(oldUser.getFirstName());
        }
        if (oldUser.getLastName() != null) {
            userToUpdate.setLastName(oldUser.getLastName());
        }
        if (oldUser.getEmail() != null) {
            userToUpdate.setEmail(oldUser.getEmail());
        }
        if (oldUser.getPhone() != null) {
            userToUpdate.setPhone(oldUser.getPhone());
        }
        userToUpdate.setUpdatedAt(LocalDateTime.now());

        return userToUpdate;
    }

}
