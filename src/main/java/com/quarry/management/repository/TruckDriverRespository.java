package com.quarry.management.repository;

import com.quarry.management.entity.TruckDetail;
import com.quarry.management.entity.TruckDriverDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TruckDriverRespository extends JpaRepository<TruckDriverDetail, Long> {

    TruckDriverDetail findByTruckDetail(TruckDetail truckId);

    Optional<TruckDriverDetail> findByDriverMobileNo(String truckDriverMobile);
}
