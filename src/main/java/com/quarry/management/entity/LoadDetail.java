package com.quarry.management.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Data
@Table(name="load_details")
@AllArgsConstructor
@NoArgsConstructor
public class LoadDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "load_id")
    private Long loadId;

    @Column(name = "product_unit_cost")
    private Float productUnitCost;

    @Column(name = "challan_unit_cost")
    private Float challanUnitCost;

    @Column(name = "load_unit")
    private Float loadUnit;

    @Column(name = "load_amt")
    private Float loadAmt;

    @Column(name = "is_challan")
    private Boolean isChallan;

    @Column(name = "total_challan")
    private Long totalChallan;

    @Column(name = "total_challan_amt")
    private Float totalChallanAmt;

    @Column(name = "total_amt")
    private Float totalAmt;

    @Column(name = "entry_status")
    private Boolean entryStatus;

    @Column(name = "vehicle_in_time")
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private Timestamp vehicleInTime;

    @Column(name = "vehicle_out_time")
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private Timestamp vehicleOutTime;

    @Column(name = "loaded_at")
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private Timestamp loadDate;

    @Column(name = "load_status")
    private Boolean loadStatus;

    @Column(name = "payment_type")
    private Boolean paymentType;

    @Column(name = "payment_status")
    private Boolean paymentStatus;

    @Column(name = "payment_date")
    private Timestamp paymentDate;

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

    @ManyToOne
    @JoinColumn(name = "truck_id", nullable = false)
    private TruckDetail truckDetail;

    @ManyToOne
    @JoinColumn(name = "driver_id", nullable = false)
    private TruckDriverDetail truckDriverDetail;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = true)
    private Product product;
}
