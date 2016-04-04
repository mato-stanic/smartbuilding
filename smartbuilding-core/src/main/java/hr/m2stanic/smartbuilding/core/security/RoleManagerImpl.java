package hr.m2stanic.smartbuilding.core.security;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service
@Transactional
public class RoleManagerImpl implements RoleManager {

    @Autowired
    private RoleRepository roleRepository;


    public List<Role> getAllRoles() {
        return roleRepository.findAll(new PageRequest(0, 100, Sort.Direction.ASC, "id")).getContent();
    }

    public Role getRole(Long id) {
        return roleRepository.findOne(id);
    }

    public Role getRoleByName(String name) {
        return roleRepository.findByName(name);
    }

    public List<Permission> getAllPermissions() {
        return Arrays.asList(Permission.values());
    }

    public Role save(Role role) {
        return roleRepository.save(role);
    }

    public void deleteRole(Long roleId) {
        roleRepository.delete(roleId);
    }

    @Override
    public List<Role> getRoles(RoleScope scope) {
        return roleRepository.findByScope(scope);
    }
}
