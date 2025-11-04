package com.example.base.enums;

public enum Role {
    ADMIN,
    CUSTOMER,
    EMPLOYEE;

    public String asAuthority() {
        return "ROLE_" + this.name();
    }
}