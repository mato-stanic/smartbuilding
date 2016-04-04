package hr.m2stanic.smartbuilding.core.security;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface RoleRepository extends JpaRepository<Role, Long> {

    Role findByName(String name);

    List<Role> findByScope(RoleScope scope);

}
