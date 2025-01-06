package com.rentalapp.houserentalapp.util;

import com.rentalapp.houserentalapp.model.entities.Users;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

public class SecurityUtil {

    public static UserDetails getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            return (UserDetails) authentication.getPrincipal();
        }
        return null; // Anonymous or unauthenticated user
    }

    public static boolean isUserAuthorized(Users oldUser) {

        UserDetails currentUser = SecurityUtil.getCurrentUser();

        if (currentUser == null || !isUserActive(oldUser)) {
            return false;
        }

        if (currentUser.getUsername().equals(oldUser.getUsername())) {
            return true;
        }
        return currentUser.getAuthorities().contains(new SimpleGrantedAuthority("ADMIN"));
    }

    public static boolean isUserActive(Users user) {
        if(user == null) {
            return false;
        }
        return user.getStatus().equals(Users.Status.ACTIVE);
    }

}
