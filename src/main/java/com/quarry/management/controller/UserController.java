package com.quarry.management.controller;

import com.quarry.management.DTO.*;
import com.quarry.management.entity.Users;
import com.quarry.management.service.UserService;
import com.quarry.management.utils.Constants;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping(value = "/{role}/login")
    public ResponseEntity<Object> generateToken(@PathVariable("role") String userType,  @RequestBody UserCredentialsReqDTO loginDTO) throws AuthenticationException {
        return userService.generateToken(userType, loginDTO);
    }

    @PostMapping(value = "/admin/register")
    public ResponseEntity<Object> registerAdmin(@RequestBody AdminReq adminReq) throws AuthenticationException {
        return userService.registerUser(Constants.ADMIN_LOWER_CASE, adminReq, null);
    }

    @PostMapping(value = "/{role}/userCreation")
    public ResponseEntity<Object> createUser(@PathVariable("role") String userType, @RequestBody UserReqDTO UserReqDTO) throws AuthenticationException {
        return userService.createUser(userType, UserReqDTO);
    }

    @PutMapping(value = "/userEdit/{id}")
    public ResponseEntity<Object> editUser(@PathVariable("id") Long id, @RequestBody UserReqDTO UserReqDTO) {
        return userService.editUser(id, UserReqDTO);
    }

    @GetMapping("/getUser/{userId}")
    public Users fetchUserById(@PathVariable("userId") Long userId) {
        return userService.fetchUserById(userId);
    }

    @PostMapping(value = "/{role}/change_password")
//    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_CUSTOMER')")
    public ResponseEntity<MessageDTO> changePassword(@PathVariable("role") String userType,  @RequestBody ChangePasswordReqDTO changePasswordDTO) throws AuthenticationException {
        return userService.changePassword(userType, changePasswordDTO);
    }

    @PostMapping("/{role}/forgot_password")
    public ResponseEntity<MessageDTO> forgotPassword(@PathVariable("role") String userType,  @RequestBody ForgotPasswordReqDTO forgotPasswordReqDTO) {
        return userService.forgotPassword(userType, forgotPasswordReqDTO);
    }

    @GetMapping("/admin/userList/{quarryId}")
    public ResponseEntity<UserResDTO> getUserList(@PathVariable("quarryId") Long quarryId) {
        return userService.getUserList(quarryId);
    }

    @GetMapping("/getUserDetail/{id}/{role}")
    public ResponseEntity<Object> getUserDetail(@PathVariable("id") Long id, @PathVariable("role") String role) {
        return userService.getUserDetail(id, role);
    }
}
