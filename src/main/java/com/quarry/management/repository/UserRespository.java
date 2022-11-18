package com.quarry.management.repository;

import com.quarry.management.entity.Admin;
import com.quarry.management.entity.Employee;
import com.quarry.management.entity.Manager;
import com.quarry.management.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRespository extends JpaRepository<Users, Long> {

    Optional<Users> findByEmailAndIsActiveAndAdminIsNotNull(String email, boolean b);

    Optional<Users> findByEmailAndIsActiveAndEmployeeIsNotNull(String email, boolean b);

    Users findByUsername(String username);

    Optional<Users> findByAdmin(Admin admin);

    Optional<Users> findByEmailAndIsActiveAndManagerIsNotNull(String email, boolean b);

    Optional<Users> findByManager(Manager manager);

    Optional<Users> findByEmployee(Employee employee);
}
