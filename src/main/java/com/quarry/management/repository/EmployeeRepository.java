package com.quarry.management.repository;

import com.quarry.management.entity.Employee;
import com.quarry.management.entity.Quarry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    List<Employee> findByQuarry(Quarry quarryById);
}
