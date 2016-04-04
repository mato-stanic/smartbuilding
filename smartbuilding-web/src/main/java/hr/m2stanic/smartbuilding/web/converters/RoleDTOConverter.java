package hr.m2stanic.smartbuilding.web.converters;

import hr.m2stanic.smartbuilding.web.dto.DTOUtil;
import org.springframework.core.convert.converter.Converter;

import hr.m2stanic.smartbuilding.core.security.RoleManager;
import hr.m2stanic.smartbuilding.web.dto.RoleDTO;


public class RoleDTOConverter implements Converter<String, RoleDTO> {

    private final RoleManager roleManager;

    public RoleDTOConverter(RoleManager roleManager) {
        this.roleManager = roleManager;
    }

    @Override
    public RoleDTO convert(String id) {
        return DTOUtil.toDTO(roleManager.getRole(Long.parseLong(id)));
    }
}

