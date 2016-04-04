package hr.m2stanic.smartbuilding.core.security;

import java.util.List;


public interface RoleManager {

    List<Role> getAllRoles();

    Role getRole(Long id);

    Role getRoleByName(String name);

    List<Permission> getAllPermissions();

    Role save(Role role);

    void deleteRole(Long roleId);

    List<Role> getRoles(RoleScope scope);

}
