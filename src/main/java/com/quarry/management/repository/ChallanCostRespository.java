package com.quarry.management.repository;

import com.quarry.management.entity.ChallanCost;
import com.quarry.management.entity.Quarry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChallanCostRespository extends JpaRepository<ChallanCost, Long> {

    List<ChallanCost> findByQuarryOrderByChallanIdDesc(Quarry quarry);

    Optional<ChallanCost> findTop1ByQuarryOrderByChallanIdDesc(Quarry quarry);
}
