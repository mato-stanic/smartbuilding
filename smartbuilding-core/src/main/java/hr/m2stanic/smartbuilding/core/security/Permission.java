package hr.m2stanic.smartbuilding.core.security;


import lombok.Getter;

import java.io.Serializable;

@Getter
public enum Permission implements Serializable {

    ACCESS_ADMIN_CONSOLE("admin-console:access", "Web admin", "Can access web admin console"),

    MANAGE_USERS("users:manage", "User manager", "User that can manage other users"),
    MANAGE_OPERATORS("operator:manage", "Operator manager", "User that can manage operators"),

    MANAGE_TARIFFS_OPERATOR("tariff:manage-operator", "Operator tariff manager", "User that can manage tariffs of operator he belongs to"),
    MANAGE_TARIFFS_ALL("tariff:manage-all", "Global tariff manager", "User that can manage tariffs of any operator"),

    MANAGE_PACKETS_OPERATOR("packet:manage-operator", "Operator packet manager", "User that can manage packets of operator he belongs to"),
    MANAGE_PACKETS_ALL("packet:manage-all", "Global packet manager", "User that can manage packets of any operator"),

    MANAGE_OPTIONS_OPERATOR("option:manage-operator", "Operator option manager", "User that can manage options of operator he belongs to"),
    MANAGE_OPTIONS_ALL("option:manage-all", "Global option manager", "User that can manage options of any operator"),

    MANAGE_ZONES_OPERATOR("zone:manage-operator", "Operator zone manager", "User that can manage zones of operator he belongs to"),
    MANAGE_ZONES_ALL("zone:manage-all", "Global zone manager", "User that can manage zones of any operator"),;

    private String value;

    private String displayName;

    private String description;

    Permission(String value, String displayName, String description) {
        this.value = value;
        this.displayName = displayName;
        this.description = description;
    }


}
