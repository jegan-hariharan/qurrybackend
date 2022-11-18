package com.quarry.management.repository;

import com.quarry.management.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRespository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String role);
}
