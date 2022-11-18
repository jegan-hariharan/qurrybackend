package com.quarry.management.service;

import com.quarry.management.DTO.QuarryReqInfo;
import com.quarry.management.DTO.MessageDTO;
import com.quarry.management.entity.*;
import com.quarry.management.exception.BadRequestException;
import com.quarry.management.exception.InternalServerErrorException;
import com.quarry.management.exception.RecordNotFoundException;
import com.quarry.management.repository.*;
import com.quarry.management.utils.Constants;
import com.quarry.management.utils.ResponseMessages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class QuarryServiceImpl implements QuarryService{

    @Autowired
    private QuarryRespository quarryRespository;

    @Autowired
    private UserRespository userRespository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private RoleRespository roleRespository;
    @Autowired
    private AdminRepository adminRespository;

    @Autowired
    private ManagerRespository managerRespository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public Quarry saveQuarryDetails(QuarryReqInfo quarryReqInfo) {
        Optional<Quarry> existingQuarryEmail = quarryRespository.findByQuarryEmail(quarryReqInfo.getEmail());
        if(existingQuarryEmail.isPresent()) {
            throw new BadRequestException("Email Already Exists");
        }
        Optional<Quarry> existingQuarryMobileNo = quarryRespository.findByQuarryMobileNumber(quarryReqInfo.getQuarryMobileNumber());
        if(existingQuarryMobileNo.isPresent()) {
            throw new BadRequestException("Mobile Number Already Exists");
        }
        Quarry quarry = new Quarry();
        quarry.setQuarryName(quarryReqInfo.getQuarryName());
        quarry.setQuarryOwnerName(quarryReqInfo.getQuarryOwnerName());
        quarry.setQuarryEmail(quarryReqInfo.getQuarryEmail());
        quarry.setQuarryAddress(quarryReqInfo.getQuarryAddress());
        quarry.setQuarryMobileNumber(quarryReqInfo.getQuarryMobileNumber());
        quarry.setQuarryGstn(quarryReqInfo.getQuarryGstn());
        quarry.setQuarryLicense(quarryReqInfo.getQuarryLicense());
        quarry.setQuarryValidity(quarryReqInfo.getQuarryValidity());
        quarry.setAdmin(findAdminById(quarryReqInfo.getAdminId()));
        quarry = quarryRespository.save(quarry);
        return quarry;
    }

    private Admin findAdminById(Long adminId) {
        Optional<Admin> adminOptional = adminRespository.findById(adminId);
        if (!adminOptional.isPresent()) {
            throw new RecordNotFoundException(ResponseMessages.ROLE_NOT_FOUND);
        }
        return adminOptional.get();
    }

    private Users saveUser(Users user) {
        return userRespository.save(user);
    }

    private Quarry findQuarryById(Long quarryId) {
        return quarryRespository.findById(quarryId).get();
    }

    @Override
    public List<Quarry> fetchAllQuarry(String role, Long id) {
        return quarryRespository.findByAdmin(adminRespository.findById(id).get());
    }

    @Override
    public Quarry fetchQuarryById(Long quarryId) {
        return quarryRespository.findById(quarryId).get();
    }

    @Override
    public Quarry updateQuarryDetail(Long quarryId, Quarry quarry) {
        Quarry quarryDB = quarryRespository.findById(quarryId).get();
        try {
            quarryDB.setQuarryName(quarry.getQuarryName());
            quarryDB.setQuarryOwnerName(quarry.getQuarryOwnerName());
            quarryDB.setQuarryMobileNumber(quarry.getQuarryMobileNumber());
            quarryDB.setQuarryEmail(quarry.getQuarryEmail());
            quarryDB.setQuarryAddress(quarry.getQuarryAddress());
            quarryDB.setQuarryGstn(quarry.getQuarryGstn());
            quarryDB.setQuarryLicense(quarry.getQuarryLicense());
            quarryDB.setQuarryValidity(quarry.getQuarryValidity());
            return quarryRespository.save(quarryDB);
        } catch (Exception e) {
            throw new InternalServerErrorException();
        }

    }

    @Override
    public Manager getQuarryByManager(String role, Long id) {
            return managerRespository.findById(id).get();
    }

    @Override
    public Employee getQuarryByEmployee(String role, Long id) {
        return employeeRepository.findById(id).get();
    }

    private Role findRoleByName(String role) {
        Optional<Role> roleOptional = roleRespository.findByName(role);
        if (!roleOptional.isPresent()) {
            throw new RecordNotFoundException(ResponseMessages.ROLE_NOT_FOUND);
        }
        return roleOptional.get();
    }
}
