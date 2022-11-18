package com.quarry.management.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name="quarry_details")
public class Quarry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "quarry_id")
    private Long quarryId;

    @Column(name = "quarry_name")
    private String quarryName;

    @Column(name = "quarry_owner_name")
    private String quarryOwnerName;

    @Column(name = "quarry_mobile_no")
    private String quarryMobileNumber;

    @Column(name = "quarry_email")
    private String quarryEmail;

    @Column(name = "quarry_address")
    private String quarryAddress;

    @Column(name = "quarry_gstn")
    private String quarryGstn;

    @Column(name = "quarry_license")
    private String quarryLicense;

    @Column(name = "quarry_validity")
    private Date quarryValidity;

    @ManyToOne
    @JoinColumn(name = "admin_id", nullable = false)
    private Admin admin;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "quarry_employee", joinColumns = {
            @JoinColumn(name = "quarry_id", referencedColumnName = "quarry_id") }, inverseJoinColumns = {
            @JoinColumn(name = "employee_id", referencedColumnName = "employee_id") })
    private List<Employee> employees;
}
