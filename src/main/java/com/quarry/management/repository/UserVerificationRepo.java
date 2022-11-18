package com.quarry.management.repository;


import com.quarry.management.entity.UserVerification;
import com.quarry.management.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UserVerificationRepo extends JpaRepository<UserVerification, Long> {

    Optional<UserVerification> findTop1ByUserIdOrderByIdDesc(Users user);

    Optional<UserVerification> findTop1ByEmailOrderByIdDesc(String email);
}