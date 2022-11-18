package com.quarry.management.repository;

import com.quarry.management.entity.Quarry;
import com.quarry.management.entity.TruckDetail;
import com.quarry.management.entity.TruckOwner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TruckDetailRespository extends JpaRepository<TruckDetail, Long> {

    List<TruckDetail> findByTruckNoStartsWith(String truckNo);

    List<TruckDetail> findByTruckNoStartsWithAndQuarryAndEntryStatus(String truckNo, Quarry quarryById, boolean b);

    List<TruckDetail> findByTruckOwnerInAndQuarryAndEntryStatus(List<TruckOwner> truckOwners, Quarry quarry, boolean b);

    Optional<TruckDetail> findByTruckNo(String truckNo);
}
