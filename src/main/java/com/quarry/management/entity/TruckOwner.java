package com.quarry.management.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Data
@Table(name="truck_owner_details")
public class TruckOwner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "owner_id")
    private Long userId;

    @Column(name = "truck_owner_name")
    private String truckOwnerName;

    @Column(name = "truck_owner_mobile_no")
    private String truckMobileNo;

    @Column(name = "truck_owner_address")
    private String truckOwnerAddress;

    @Column(name = "created_at")
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private Timestamp createdDate;

    @Column(name = "updated_on")
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private Timestamp updatedDate;

    @ManyToOne
    @JoinColumn(name = "quarry_id", nullable = false)
    private Quarry quarry;
}
