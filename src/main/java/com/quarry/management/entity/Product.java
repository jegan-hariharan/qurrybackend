package com.quarry.management.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name="products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long productId;

    @Column(name = "product_name")
    private String productName;

    @Column(name="is_active")
    private Boolean isActive;

    @Column(name = "created_at")
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private Timestamp createdDate;

    @Column(name = "updated_on")
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private Timestamp updatedDate;

    @ManyToOne
    @JoinColumn(name = "quarry_id")
    private Quarry quarry;
}
