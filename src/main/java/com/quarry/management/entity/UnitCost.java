package com.quarry.management.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Data
@Table(name="unit_cost")
@AllArgsConstructor
@NoArgsConstructor
public class UnitCost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "unit_id")
    private Long unitId;

    @Column(name = "unit")
    private Float unit;

    @Column(name = "unit_cost")
    private Float unitCost;

    @Column(name = "datetime")
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private Timestamp createdDate;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne
    @JoinColumn(name = "quarry_id", nullable = false)
    private Quarry quarry;
}
