package com.example.base.enums;

public enum Role {
    ADMIN,
    EMPLOYEE,
    CUSTOMER;

    public String asAuthority() {
        return "ROLE_" + this.name();
    }
}