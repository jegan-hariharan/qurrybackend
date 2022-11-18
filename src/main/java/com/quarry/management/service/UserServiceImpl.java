package com.quarry.management.service;

import com.quarry.management.DTO.*;
import com.quarry.management.entity.*;
import com.quarry.management.enums.UserTypes;
import com.quarry.management.exception.BadRequestException;
import com.quarry.management.exception.InternalServerErrorException;
import com.quarry.management.exception.RecordNotFoundException;
import com.quarry.management.jwt.TokenProvider;
import com.quarry.management.repository.*;
import com.quarry.management.service.email.EmailService;
import com.quarry.management.service.email.Mail;
import com.quarry.management.utils.AppUtils;
import com.quarry.management.utils.Constants;
import com.quarry.management.utils.ResponseMessages;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.sql.Timestamp;

@Service
public class UserServiceImpl implements UserService{

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private RoleRespository roleRespository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private UserRespository userRepository;

    @Autowired
    private EmailService emailService;


    @Autowired
    private UserVerificationRepo userVerificationRepo;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private QuarryRespository quarryRespository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenProvider jwtTokenUtil;

    @Autowired
    private ManagerRespository managerRespository;

    @Override
    public Users fetchUserById(Long userId) {
        return userRepository.findById(userId).get();
    }

    @Override
    public Quarry findQuarryById(Long quarryId) {
        return quarryRespository.findById(quarryId).get();
    }

    @Override
    public Role findUserRolesById(Long roleId) {
        return roleRespository.findById(roleId).get();
    }

    @Override
    public ResponseEntity<Object> registerUser(String userType, AdminReq adminReq, QuarryReqInfo employeeRequestInfo) {
        Optional<Users> user;
        if (userType.equals(UserTypes.ADMIN.toString())) {
            user = findAdminByEmail(adminReq.getEmail());
        }  else {
            throw new BadRequestException(ResponseMessages.INVALID_USER_TYPE);
        }

        if (user.isPresent()) {
            throw new BadRequestException(ResponseMessages.USER_ALREADY_EXISTS);
        }
        Users registeredUser = new Users();
        String password = "";
        try {
            if (userType.equals(UserTypes.ADMIN.toString())) {
                registeredUser = createAdmin(adminReq);
                password = adminReq.getPassword();
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new InternalServerErrorException();
        }
        return setResponse(userType, registeredUser, password);
    }


    @Override
    public ResponseEntity<Object> generateToken(String userType, UserCredentialsReqDTO loginDTO) {
        Optional<Users> user;
        if (userType.equals(UserTypes.ADMIN.toString())) {
            user = findAdminByEmail(loginDTO.getEmail());
        } else if (userType.equals(UserTypes.EMPLOYEE.toString())) {
            user = findEmployeeByEmail(loginDTO.getEmail());
        } else if (userType.equals(UserTypes.MANAGER.toString())) {
            user = findManagerByEmail(loginDTO.getEmail());
        } else {
            throw new BadRequestException(ResponseMessages.INVALID_USER_TYPE);
        }

        if (!user.isPresent()) {
            throw new BadRequestException(ResponseMessages.USER_NOT_EXISTS);
        }
        return setResponse(userType, user.get(), loginDTO.getPassword());
    }

    @Override
    public Users getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public ResponseEntity<MessageDTO> changePassword(String userType, ChangePasswordReqDTO changePasswordDTO) {
        Optional<Users> userOptional = null;
        if (userType.equals(UserTypes.ADMIN.toString())) {
            Admin admin = findAdminById(changePasswordDTO.getUserId());
            userOptional = userRepository.findByAdmin(admin);
        } else if(userType.equals(UserTypes.MANAGER.toString())) {
            Manager manager = findManagerById(changePasswordDTO.getUserId());
            userOptional = userRepository.findByManager(manager);
        } else if(userType.equals(UserTypes.EMPLOYEE.toString())) {
            Employee employee = findEmployeeById(changePasswordDTO.getUserId());
            userOptional = userRepository.findByEmployee(employee);
        }
        if (userOptional.isPresent()) {
            Users user = userOptional.get();
            if (bCryptPasswordEncoder.matches(changePasswordDTO.getCurrentPassword(), user.getPassword())) {
                user.setUpdatedDate(new Timestamp(new Date().getTime()));
                user.setPassword(bCryptPasswordEncoder.encode(changePasswordDTO.getNewPassword()));
                userRepository.save(user);
            } else {
                throw new BadRequestException(ResponseMessages.PASSWORD_MISMATCH);
            }
        } else {
            throw new BadRequestException(ResponseMessages.USER_NOT_EXISTS);
        }
        return ResponseEntity.ok(new MessageDTO(true, ResponseMessages.PASSWORD_CHANGED_MSG));
    }

    private Employee findEmployeeById(Long employeeId) {
        return employeeRepository.findById(employeeId).get();
    }

    private Manager findManagerById(Long managerId) {
        return managerRespository.findById(managerId).get();
    }

    public Admin findAdminById(Long adminId) {
        return adminRepository.findById(adminId).get();
    }

    @Override
    public ResponseEntity<MessageDTO> forgotPassword(String userType, ForgotPasswordReqDTO forgotPasswordReqDTO) {
        Optional<Users> userOptional = null;

        if (userType.equals(UserTypes.ADMIN.toString())) {
            userOptional = findAdminByEmail(forgotPasswordReqDTO.getEmail());
        } else if (userType.equals(UserTypes.MANAGER.toString())) {
            userOptional = findManagerByEmail(forgotPasswordReqDTO.getEmail());
        } else if (userType.equals(UserTypes.EMPLOYEE.toString())) {
            userOptional = findEmployeeByEmail(forgotPasswordReqDTO.getEmail());
        } else {
            throw new BadRequestException(ResponseMessages.INVALID_USER_TYPE);
        }

        if (!userOptional.isPresent()) {
            throw new BadRequestException(ResponseMessages.USER_NOT_EXISTS);
        }
        if (StringUtils.isNotBlank(forgotPasswordReqDTO.getEmail()) && StringUtils.isNotBlank(forgotPasswordReqDTO.getPassword())) {
            return updatePassword(userOptional.get(), forgotPasswordReqDTO);
        } else if (StringUtils.isNotBlank(forgotPasswordReqDTO.getEmail()) && StringUtils.isNotBlank(forgotPasswordReqDTO.getVerificationCode())) {
            return forgotPasswordVerification(userOptional.get(), forgotPasswordReqDTO);
        } else {
            return sendVerificationCode(userType, userOptional.get());
        }
    }

    @Override
    public ResponseEntity<Object> createUser(String userType, UserReqDTO userReqDTO) {
        Optional<Users> user;
        if (userType.equals(UserTypes.MANAGER.toString())) {
            user = findManagerByEmail(userReqDTO.getEmail());
        }  else if (userType.equals(UserTypes.EMPLOYEE.toString())) {
            user = findEmployeeByEmail(userReqDTO.getEmail());
        } else {
            throw new BadRequestException(ResponseMessages.INVALID_USER_TYPE);
        }

        if (user.isPresent()) {
            throw new BadRequestException(ResponseMessages.USER_ALREADY_EXISTS);
        }
        Users registeredUser = new Users();
        String password = "";
        try {
            if (userType.equals(UserTypes.EMPLOYEE.toString())) {
                return createEmployee(userReqDTO);
            } else if (userType.equals(UserTypes.MANAGER.toString())) {
                return createManager(userReqDTO);
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new InternalServerErrorException();
        }
        return ResponseEntity.ok(new MessageDTO(true, ResponseMessages.USER_CREATED));
    }

    @Override
    public ResponseEntity<UserResDTO> getUserList(Long quarryId) {
        try {
            List<Manager> manager = managerRespository.findByQuarry(findQuarryById(quarryId));
            List<Employee> employee = employeeRepository.findByQuarry(findQuarryById(quarryId));
            return ResponseEntity.ok(new UserResDTO(manager, employee));
        } catch(Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new InternalServerErrorException();
        }
    }

    @Override
    public ResponseEntity<Object> getUserDetail(Long id, String role) {
        if(role.equals("manager")) {
            Manager manager = managerRespository.findById(id).get();
            return ResponseEntity.ok(manager);
        } else {
            Employee employee = employeeRepository.findById(id).get();
            return ResponseEntity.ok(employee);
        }
    }

    @Override
    public ResponseEntity<Object> editUser(Long id, UserReqDTO userReqDTO) {
        if(userReqDTO.getRole().equals("manager")) {
            Manager manager = managerRespository.findById(id).get();
            try{
                manager.setName(userReqDTO.getName());
                manager.setEmail(userReqDTO.getEmail());
                manager.setPhone(userReqDTO.getPhoneNumber());
                manager.setGender(userReqDTO.getGender());
                manager.setIsActive(userReqDTO.getIsActive());
                manager = managerRespository.save(manager);

                Users users = userRepository.findByManager(managerRespository.findById(manager.getId()).get()).get();
                users.setEmail(userReqDTO.getEmail());
                users.setIsActive(userReqDTO.getIsActive());
                users.setPhone(userReqDTO.getPhoneNumber());
                users = userRepository.save(users);
                return ResponseEntity.ok(new MessageDTO(true, ResponseMessages.USER_UPDATED));
            } catch(Exception e) {
                throw new InternalServerErrorException();
            }
        } else {
            Employee employee = employeeRepository.findById(id).get();
            try {
                employee.setName(userReqDTO.getName());
                employee.setEmail(userReqDTO.getEmail());
                employee.setPhone(userReqDTO.getPhoneNumber());
                employee.setGender(userReqDTO.getGender());
                employee.setIsActive(userReqDTO.getIsActive());
                employee = employeeRepository.save(employee);

                Users users = userRepository.findByEmployee(employeeRepository.findById(employee.getEmployeeId()).get()).get();
                users.setEmail(userReqDTO.getEmail());
                users.setIsActive(userReqDTO.getIsActive());
                users.setPhone(userReqDTO.getPhoneNumber());
                users = userRepository.save(users);
                return ResponseEntity.ok(new MessageDTO(true, ResponseMessages.USER_UPDATED));
            } catch (Exception e) {
                throw new InternalServerErrorException();
            }
        }
    }

    private ResponseEntity<MessageDTO> forgotPasswordVerification(Users user, ForgotPasswordReqDTO forgotPasswordReqDTO) {
        Long timeInSecs = Calendar.getInstance().getTimeInMillis();
        Optional<UserVerification> userVerificationOptional = userVerificationRepo.findTop1ByUserIdOrderByIdDesc(user);
        if (userVerificationOptional.isPresent()) {
            UserVerification userVerification = userVerificationOptional.get();
            if(userVerification.getStatus().equals("A")) {
                if (userVerification.getCodeSentOn().after(new Date(timeInSecs - (3 * 60 * 1000))) && userVerification.getVerificationCode().equals(forgotPasswordReqDTO.getVerificationCode())) {
                    userVerification.setStatus("V");
                    userVerificationRepo.save(userVerification);
                } else if (!userVerification.getVerificationCode().equals(forgotPasswordReqDTO.getVerificationCode())) {
                    userVerification.setStatus("M");
                    userVerificationRepo.save(userVerification);
                    throw new BadRequestException(ResponseMessages.GIVEN_CODE_INCORRECT_MSG);
                } else if (userVerification.getCodeSentOn().before(new Date(timeInSecs - (3 * 60 * 1000)))) {
                    userVerification.setStatus("E");
                    userVerificationRepo.save(userVerification);
                    throw new BadRequestException(ResponseMessages.CODE_VALIDITY_EXPIRED_MSG);
                }
            } else{
                throw new BadRequestException(ResponseMessages.INVALID_CODE);
            }
        }
        return ResponseEntity.ok(new MessageDTO(true, ResponseMessages.CODE_VERIFY_SUCCESS_MSG));
    }

    private ResponseEntity<MessageDTO> sendVerificationCode(String userType, Users user) {
        String code = AppUtils.getRandomDigits().toString();
        try {
            Map<String, String> model = new HashMap<>();
            if (userType.equals(UserTypes.ADMIN.toString())) {
                model.put(Constants.USERNAME, user.getAdmin().getName());
            } else if (userType.equals(UserTypes.MANAGER.toString())) {
                model.put(Constants.USERNAME, user.getManager().getName());
            } else if (userType.equals(UserTypes.ADMIN.toString())) {
                model.put(Constants.USERNAME, user.getEmployee().getName());
            }
            model.put(Constants.MESSAGE, Constants.FORGOT_PWD_MSG);
            model.put(Constants.CODE, code);
            model.put(Constants.NOTE_MSG, Constants.FORGOT_PWD_NOTE_MSG);

            Mail mail = new Mail();
            mail.setSubject(Constants.FORGOT_PWD_SUBJECT_MSG);
            mail.setTo(user.getEmail());
            mail.setModel(model);
            emailService.sendSimpleMessageByTemplate(mail, Constants.FORGOT_PASSWORD_TEMPLATE);

            UserVerification userVerification = new UserVerification();
            userVerification.setUserId(user);
            userVerification.setVerificationCode(code);
            userVerification.setStatus("A");
            userVerificationRepo.save(userVerification);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new InternalServerErrorException();
        }
        return ResponseEntity.ok(new MessageDTO(true, ResponseMessages.CODE_SEND_MSG + code));
    }

    private ResponseEntity<MessageDTO> updatePassword(Users user, ForgotPasswordReqDTO forgotPasswordReqDTO) {
        try {
            user.setPassword(bCryptPasswordEncoder.encode(forgotPasswordReqDTO.getPassword()));
            saveUser(user);
            return ResponseEntity.ok(new MessageDTO(true, ResponseMessages.PASSWORD_CHANGED_MSG));
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new InternalServerErrorException();
        }
    }

    private ResponseEntity<Object> setResponse(String userType, Users user, String password) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                user.getUsername(),
                password
        );
        final Authentication authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        try {
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String token = jwtTokenUtil.generateToken(authentication);
            if (userType.equals(UserTypes.ADMIN.toString())) {
                return ResponseEntity.ok(new AuthAdminResDTO(user, token));
            } else if(userType.equals(UserTypes.EMPLOYEE.toString())) {
                return ResponseEntity.ok(new AuthEmployeeResDTO(user, token));
            } else {
                return ResponseEntity.ok(new AuthManagerResDTO(user, token));
            }
        } catch (
                Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new InternalServerErrorException();
        }
    }

    public Optional<Users> findAdminByEmail(String email) {
        return userRepository.findByEmailAndIsActiveAndAdminIsNotNull(email, true);
    }

    public Optional<Users> findManagerByEmail(String email) {
        return userRepository.findByEmailAndIsActiveAndManagerIsNotNull(email, true);
    }

    private Optional<Users> findEmployeeByEmail(String email) {
        return userRepository.findByEmailAndIsActiveAndEmployeeIsNotNull(email, true);
    }
    private Users createAdmin(AdminReq adminReq) {
        Admin admin = new Admin(
                null,
                adminReq.getName(),
                adminReq.getEmail(),
                adminReq.getPhone(),
                new Timestamp(new Date().getTime()),null
        );
       admin = adminRepository.save(admin);

        Users user = new Users();
        user.setAdmin(admin);
        user.setEmail(admin.getEmail());
        user.setPassword(bCryptPasswordEncoder.encode(adminReq.getPassword()));
        user.setPhone(admin.getPhone());
        user.setIsActive(true);
        user.setRoles(Arrays.asList(findRoleByName(Constants.ADMIN)));
        user.setUsername(Constants.ADMIN + "_" + admin.getName());
        user.setAddress(adminReq.getAddress());
        return saveUser(user);
    }

    private Role findRoleByName(String role) {
        Optional<Role> roleOptional = roleRespository.findByName(role);
        if (!roleOptional.isPresent()) {
            throw new RecordNotFoundException(ResponseMessages.ROLE_NOT_FOUND);
        }
        return roleOptional.get();
    }

    public Users saveUser(Users user) {
        return userRepository.save(user);
    }

    private ResponseEntity<Object> createEmployee(UserReqDTO userReqDTO) throws IOException {
        Quarry quarry = findQuarryById(userReqDTO.getQuarryId());
        Employee employee = employeeRepository.save(new Employee(null,
                userReqDTO.getName(),
                userReqDTO.getEmail(),
                userReqDTO.getPhoneNumber(),
                userReqDTO.getGender(),
                userReqDTO.getIsActive(),
                "admin",
                new Timestamp(new Date().getTime()),
                null,
                null,
                quarry
        ));

        employee = employeeRepository.save(employee);
        String password = AppUtils.generateRandomPassword(10);
        Users user = new Users();
        user.setEmployee(employee);
        user.setEmail(employee.getEmail());
        user.setPassword(bCryptPasswordEncoder.encode(password));
        user.setPhone(userReqDTO.getPhoneNumber());
        user.setIsActive(true);
        user.setRoles(Arrays.asList(findRoleByName(Constants.EMPLOYEE)));
        user.setUsername(employee.getName());
        saveUser(user);
        if (employee.getEmail() != null) {
            // send password notification
            emailService.sendEmployeePasswordNotification(employee, password);
        }
        return ResponseEntity.ok(new MessageDTO(true, ResponseMessages.EMPLOYEE_CREATED_MSG));
    }

    private ResponseEntity<Object> createManager(UserReqDTO userReqDTO) throws IOException {
        Quarry quarry = findQuarryById(userReqDTO.getQuarryId());
        Manager manager = managerRespository.save(new Manager(null,
                userReqDTO.getName(),
                userReqDTO.getEmail(),
                userReqDTO.getPhoneNumber(),
                userReqDTO.getGender(),
                userReqDTO.getIsActive(),
                "admin",
                new Timestamp(new Date().getTime()),
                null,
                null,
                quarry
        ));

        manager = managerRespository.save(manager);
        String password = AppUtils.generateRandomPassword(10);
        Users user = new Users();
        user.setManager(manager);
        user.setEmail(manager.getEmail());
        user.setPassword(bCryptPasswordEncoder.encode(password));
        user.setPhone(userReqDTO.getPhoneNumber());
        user.setIsActive(true);
        user.setRoles(Arrays.asList(findRoleByName(Constants.MANAGER)));
        user.setUsername(manager.getName());
        saveUser(user);
        if (manager.getEmail() != null) {
            // send password notification
            emailService.sendManagerPasswordNotification(manager, password);
        }
        return ResponseEntity.ok(new MessageDTO(true, ResponseMessages.MANAGER_CREATED_MSG));
    }
}
