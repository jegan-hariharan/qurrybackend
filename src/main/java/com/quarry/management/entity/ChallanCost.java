package com.quarry.management.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Data
@Table(name="challan_cost")
@AllArgsConstructor
@NoArgsConstructor
public class ChallanCost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "challan_id")
    private Long challanId;

    @Column(name = "challan_cost")
    private Float challanCost;

    @Column(name = "datetime")
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private Timestamp createdDate;

    @ManyToOne
    @JoinColumn(name = "quarry_id", nullable = false)
    private Quarry quarry;
}
