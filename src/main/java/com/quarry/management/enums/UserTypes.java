package com.quarry.management.enums;

public enum UserTypes {
    ADMIN,
    EMPLOYEE,
    MANAGER;

    @Override
    public String toString() {
        return name().toLowerCase();
    }
}
