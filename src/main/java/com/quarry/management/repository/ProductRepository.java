package com.quarry.management.repository;

import com.quarry.management.entity.Product;
import com.quarry.management.entity.Quarry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findAllByQuarry(Quarry quarryById);

    List<Product> findAllByQuarryAndIsActive(Quarry quarryById, boolean b);

}
