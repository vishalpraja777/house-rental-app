package com.rentalapp.houserentalapp.controller;

import com.rentalapp.houserentalapp.model.entities.Property;
import com.rentalapp.houserentalapp.model.entities.Users;
import com.rentalapp.houserentalapp.service.AdminService;
import com.rentalapp.houserentalapp.util.Constants;
import com.rentalapp.houserentalapp.util.CustomResponseUtil;
import com.rentalapp.houserentalapp.util.ResponseObject;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/admin")
@Tag(name = "Admin APIs", description = "Admin APIs")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/get-all-users")
    @Operation(summary = "Get All Users")
    public ResponseEntity<ResponseObject<List<Users>>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "userId") String sortBy,
            @RequestParam(defaultValue = "true") boolean ascending
    ) {
        log.info("Get All User API called");
        Sort sort = ascending ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        return adminService.getAllUsers(pageable);
    }

    @PutMapping("/change-user-status-role/{userId}")
    @Operation(summary = "Change User Status Or Role")
    public ResponseEntity<ResponseObject<String>> changeUserStatusRole(@PathVariable Long userId, @RequestParam(required = false) Users.Status status, @RequestParam(required = false) Users.Role role) {
        log.info("Change User Status Role API called for User: {}", userId);
        if(!(status != null || role != null)) {
            log.info(Constants.ATLEAST_ONE_OF_STATUS_OR_ROLE_SHOULD_BE_SET);
            return CustomResponseUtil.getFailureResponse(Constants.ATLEAST_ONE_OF_STATUS_OR_ROLE_SHOULD_BE_SET, HttpStatus.BAD_REQUEST);
        }
        return adminService.changeUserStatusRole(userId, status, role);
    }

    @DeleteMapping("/delete-user/{userId}")
    @Operation(summary = "Delete User")
    public ResponseEntity<ResponseObject<String>> deleteUser(@PathVariable Long userId) {
        log.info("Delete User API called for User: {}", userId);
        return adminService.deleteUser(userId);
    }

    @GetMapping("/get-all-properties")
    @Operation(summary = "Get All Properties")
    public ResponseEntity<ResponseObject<List<Property>>> getAllProperties(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "propertyId") String sortBy,
            @RequestParam(defaultValue = "true") boolean ascending
    ) {
        log.info("Get All Properties API called");
        Sort sort = ascending ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        return adminService.getAllProperties(pageable);
    }

}
