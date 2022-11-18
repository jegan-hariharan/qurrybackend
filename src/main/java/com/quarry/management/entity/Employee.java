package com.quarry.management.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "employee")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Employee {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "employee_id")
        private Long employeeId;

        @Column(name = "name")
        private String name;

        @Column(name = "email")
        private String email;

        @Column(name = "phone")
        private String phone;

        @Column(name = "gender")
        private String gender;

        @Column(name = "is_active")
        private Boolean isActive;

        @Column(name = "created_by")
        protected String createdBy;

        @Column(name = "created_date")
        @CreationTimestamp
        @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
        protected Timestamp createdDate;

        @Column(name = "updated_by")
        protected String updatedBy;

        @Column(name = "updated_date", insertable = false)
        @UpdateTimestamp
        @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
        protected Timestamp updatedDate;

        @ManyToOne
        @JoinColumn(name = "quarry_id", nullable = false)
        private Quarry quarry;

}
