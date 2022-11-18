package com.quarry.management.repository;

import com.quarry.management.entity.Admin;
import com.quarry.management.entity.LoadDetail;
import com.quarry.management.entity.Quarry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QuarryRespository extends JpaRepository<Quarry, Long> {

    Optional<Quarry> findByQuarryEmail(String email);

    Optional<Quarry> findByQuarryMobileNumber(String quarryMobileNumber);

    List<Quarry> findByAdmin(Admin admin);

}

