package com.quarry.management.repository;

import com.quarry.management.DTO.ProductLoadUnitChart;
import com.quarry.management.DTO.ProductTotalAmtChart;
import com.quarry.management.entity.LoadDetail;
import com.quarry.management.entity.Quarry;
import com.quarry.management.entity.TruckDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface LoadDetailRepository extends JpaRepository<LoadDetail, Long> {

    List<LoadDetail> findByTruckDetailInAndEntryStatus(List<TruckDetail> truckDetailList, boolean b);

    List<LoadDetail> findByQuarryOrderByLoadIdDesc(Quarry quarryById);

    @Query("SELECT new com.quarry.management.DTO.ProductLoadUnitChart(l.product, SUM(l.loadUnit)) FROM LoadDetail l WHERE l.loadDate BETWEEN :startDate AND :endDate GROUP BY l.product")
    List<ProductLoadUnitChart> findProductLoadUnitByLoadDateBetween(Date startDate, Date endDate);

    @Query("SELECT new com.quarry.management.DTO.ProductTotalAmtChart(l.product, SUM(l.totalAmt)) FROM LoadDetail l WHERE l.loadDate BETWEEN :startDate AND :endDate GROUP BY l.product")
    List<ProductTotalAmtChart> findProductTotalAmtByLoadDateBetween(Date startDate, Date endDate);
}
