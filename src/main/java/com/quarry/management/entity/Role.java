package com.quarry.management.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name="role")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private Integer roleId;

    @Column(name = "name")
    private String name;

}
