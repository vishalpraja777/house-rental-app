package com.rentalapp.houserentalapp.service.impl;

import com.rentalapp.houserentalapp.model.RegisterUserDto;
import com.rentalapp.houserentalapp.model.entities.Users;
import com.rentalapp.houserentalapp.util.SecurityUtil;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;

public class UserServiceBaseImpl {

    protected void addUserFiledsToRegister(Users userToSave, RegisterUserDto user) {
        userToSave.setFirstName(user.getFirstName());
        userToSave.setLastName(user.getLastName());
        userToSave.setUsername(user.getUsername());
        userToSave.setEmail(user.getEmail());
        userToSave.setPhone(user.getPhone());
        userToSave.setRole(user.getRole());
        userToSave.setStatus(Users.Status.ACTIVE);
    }

    protected void updateUserFields(Users userToUpdate, Users oldUser) {

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

    }

}
