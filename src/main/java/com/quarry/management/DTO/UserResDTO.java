package com.quarry.management.DTO;

import com.quarry.management.entity.Employee;
import com.quarry.management.entity.Manager;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResDTO {

    private List<Manager> managerList;
    private List<Employee> employeeList;

}
