package hr.m2stanic.smartbuilding.web.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import hr.m2stanic.smartbuilding.core.security.Permission;
import hr.m2stanic.smartbuilding.core.security.RoleScope;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RoleDTO {

    private Long id;

    private String name;

    private String displayName;

    private String description;

    private RoleScope scope = RoleScope.ADMIN;

    private List<Permission> permissions = new ArrayList<>();
}
