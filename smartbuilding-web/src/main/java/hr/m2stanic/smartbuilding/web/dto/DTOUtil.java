package hr.m2stanic.smartbuilding.web.dto;

import hr.m2stanic.smartbuilding.core.apartment.Admin;
import lombok.extern.slf4j.Slf4j;

import hr.m2stanic.smartbuilding.core.appuser.AppUser;
import hr.m2stanic.smartbuilding.core.apartment.Apartment;
import hr.m2stanic.smartbuilding.core.apartment.Tenants;
import hr.m2stanic.smartbuilding.core.security.Role;

import java.util.stream.Collectors;

@Slf4j
public class DTOUtil {



    public static RoleDTO toDTO(Role role) {
        return role != null ? new RoleDTO(role.getId(), role.getName(), role.getDisplayName(), role.getDescription(), role.getScope(),
                role.getPermissions().stream().collect(Collectors.toList())) : null;
    }

    public static Role fromDTO(RoleDTO dto) {
        return dto != null ? new Role(dto.getId(), dto.getName(), dto.getDisplayName(), dto.getDescription(), dto.getScope(),
                dto.getPermissions().stream().collect(Collectors.toSet())) : null;
    }


    public static UserDTO toDTO(AppUser appUser) {
        return appUser != null ? new UserDTO(appUser.getId(), appUser.getUsername(), appUser.isActive(), appUser.getFirstName(),
                appUser.getLastName(), appUser.getPassword(), appUser.getEmail(), toDTO(appUser.getRole()), toDTO(appUser.getApartment())) : null;
    }

    public static AppUser fromDTO(UserDTO user) {
        return user != null ? new AppUser(user.getId(), user.getUsername(), user.isActive(), user.getFirstName(), user.getLastName(),
                user.getPassword(), user.getEmail(), fromDTO(user.getApartment()), fromDTO(user.getRole()), null) : null;
    }

    public static Apartment fromDTO(ApartmentDTO dto) {
        return (dto instanceof TenantsDTO) ? fromDTO((TenantsDTO) dto) : fromDTO((AdminDTO) dto);
    }

    public static Tenants fromDTO(TenantsDTO dto) {
        return dto != null ? new Tenants(dto.getId(), dto.getName()) : null;
    }


    public static ApartmentDTO toDTO(Apartment apartment) {
        return apartment instanceof Admin ? toDTO((Admin) apartment) : toDTO((Tenants) apartment);
    }


    public static AdminDTO toDTO(Admin og) {
        return og != null ? new AdminDTO(og.getId(), og.getName()) : null;
    }



    public static Admin fromDTO(AdminDTO dto) {
        return dto != null ? new Admin(dto.getId(), dto.getName()) : null;
    }

    public static TenantsDTO toDTO(Tenants og) {
        return og != null ? new TenantsDTO(og.getId(), og.getName()) : null;
    }

}
