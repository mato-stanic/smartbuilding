package hr.m2stanic.smartbuilding.web.dto;

import hr.m2stanic.smartbuilding.core.company.Admin;
import lombok.extern.slf4j.Slf4j;

import hr.m2stanic.smartbuilding.core.appuser.AppUser;
import hr.m2stanic.smartbuilding.core.company.Apartment;
import hr.m2stanic.smartbuilding.core.company.UserGroup;
import hr.m2stanic.smartbuilding.core.security.Role;
import org.joda.time.LocalDateTime;

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
                user.getPassword(), user.getEmail(), fromDTO(user.getCompany()), fromDTO(user.getRole()), null) : null;
    }

    public static Apartment fromDTO(CompanyDTO dto) {
        return (dto instanceof UserGroupDTO) ? fromDTO((UserGroupDTO) dto) : fromDTO((AgencyDTO) dto);
    }

    public static UserGroup fromDTO(UserGroupDTO dto) {
        return dto != null ? new UserGroup(dto.getId(), dto.getName()) : null;
    }


    public static CompanyDTO toDTO(Apartment apartment) {
        return apartment instanceof Admin ? toDTO((Admin) apartment) : toDTO((UserGroup) apartment);
    }


    public static AgencyDTO toDTO(Admin og) {
        return og != null ? new AgencyDTO(og.getId(), og.getName()) : null;
    }



    public static Admin fromDTO(AgencyDTO dto) {
        return dto != null ? new Admin(dto.getId(), dto.getName()) : null;
    }

    public static UserGroupDTO toDTO(UserGroup og) {
        return og != null ? new UserGroupDTO(og.getId(), og.getName()) : null;
    }

}
