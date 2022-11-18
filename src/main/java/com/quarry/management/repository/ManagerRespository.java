package com.quarry.management.repository;

import com.quarry.management.entity.Manager;
import com.quarry.management.entity.Quarry;
import com.quarry.management.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ManagerRespository extends JpaRepository<Manager, Long> {
    List<Manager> findByQuarry(Quarry quarryById);
    
}
