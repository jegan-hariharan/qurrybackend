package com.quarry.management.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Data
@Table(name="truck_details")
public class TruckDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "truck_id")
    private Long truckId;

    @Column(name = "truck_no")
    private String truckNo;

    @Column(name = "truck_capacity")
    private Float truckCapacity;

    @Column(name = "entry_status")
    private Boolean entryStatus = true;

    @Column(name = "created_at")
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private Timestamp createdDate;

    @Column(name = "updated_on")
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private Timestamp updatedDate;

    @ManyToOne
    @JoinColumn(name = "quarry_id", nullable = false)
    private Quarry quarry;

    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private TruckOwner truckOwner;

}
