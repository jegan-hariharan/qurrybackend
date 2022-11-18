package com.quarry.management.service;

import com.quarry.management.DTO.*;
import com.quarry.management.entity.Quarry;
import com.quarry.management.entity.Role;
import com.quarry.management.entity.Users;
import org.springframework.http.ResponseEntity;

public interface UserService {
//    public Users saveUserDetail(AdminReq adminReq);

    public Users fetchUserById(Long userId);

    Quarry findQuarryById(Long quarryId);

    Role findUserRolesById(Long roleId);
    
    ResponseEntity<Object> registerUser(String adminLowerCase, AdminReq adminReq, QuarryReqInfo employeeRequestInfo);

    ResponseEntity<Object> generateToken(String userType, UserCredentialsReqDTO loginDTO);

    Users getUserByUsername(String username);

    ResponseEntity<MessageDTO> changePassword(String userType, ChangePasswordReqDTO changePasswordDTO);

    ResponseEntity<MessageDTO> forgotPassword(String userType, ForgotPasswordReqDTO forgotPasswordReqDTO);

    ResponseEntity<Object> createUser(String userType, UserReqDTO userReqDTO);

    ResponseEntity<UserResDTO> getUserList(Long quarryId);

    ResponseEntity<Object> getUserDetail(Long id, String role);

    ResponseEntity<Object> editUser(Long id, UserReqDTO userReqDTO);
}
