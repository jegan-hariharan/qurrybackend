package com.quarry.management.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Data
@Table(name="truck_driver_details")
public class TruckDriverDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "driver_id")
    private Long driverId;

    @Column(name = "driver_name")
    private String driverName;

    @Column(name = "driver_mobile_no")
    private String driverMobileNo;

    @Column(name = "created_at")
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private Timestamp createdDate;

    @Column(name = "updated_on")
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private Timestamp updatedDate;

    @ManyToOne
    @JoinColumn(name = "truck_id", nullable = false)
    private TruckDetail truckDetail;
}
