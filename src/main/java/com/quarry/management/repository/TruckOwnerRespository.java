package com.quarry.management.repository;

import com.quarry.management.entity.Quarry;
import com.quarry.management.entity.TruckOwner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TruckOwnerRespository extends JpaRepository<TruckOwner, Long> {

    List<TruckOwner> findByQuarry(Quarry quarryById);

    List<TruckOwner> findByQuarryAndTruckMobileNoContaining(Quarry quarry, String truckNo);

    Optional<TruckOwner> findByTruckMobileNo(String truckOwnerMobileNo);
}
