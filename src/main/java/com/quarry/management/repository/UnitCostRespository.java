package com.quarry.management.repository;

import com.quarry.management.entity.Product;
import com.quarry.management.entity.Quarry;
import com.quarry.management.entity.UnitCost;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UnitCostRespository extends JpaRepository<UnitCost, Long> {

    public List<UnitCost> findAllByOrderByUnitIdDesc();

    List<UnitCost> findByQuarryAndProductOrderByUnitIdDesc(Quarry quarry, Product product);

    Optional<UnitCost> findTop1ByQuarryAndProductOrderByUnitIdDesc(Quarry quarryById, Product productById);
}
