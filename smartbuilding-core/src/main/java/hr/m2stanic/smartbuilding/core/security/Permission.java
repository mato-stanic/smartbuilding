package hr.m2stanic.smartbuilding.core.security;


import lombok.Getter;

import java.io.Serializable;

@Getter
public enum Permission implements Serializable {

    ACCESS_ADMIN_CONSOLE("admin-console:access", "Web admin", "Can access web admin console"),

    MANAGE_USERS("users:manage", "User manager", "User that can manage other users"),
    MANAGE_OPERATORS("operator:manage", "Operator manager", "User that can manage operators");

    private String value;

    private String displayName;

    private String description;

    Permission(String value, String displayName, String description) {
        this.value = value;
        this.displayName = displayName;
        this.description = description;
    }


}
